package de.contractly;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.contractly.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisiere die Datenbank-Instanz und den Executor-Service
        db = AppDatabase.getDatabase(requireContext());
        executorService = Executors.newSingleThreadExecutor();

        // Klick-Listener für den "Vertrag speichern"-Button
        binding.buttonSaveContract.setOnClickListener(v -> saveContract());

    }

    private void saveContract() {
        // 1. Daten aus den Feldern auslesen
        String contractName = binding.editTextContractName.getText().toString().trim();
        String monthlyCostStr = binding.editTextMonthlyCost.getText().toString().trim();
        String startDate = binding.editTextDuration.getText().toString().trim();
        String endDate = binding.editTextCancellation.getText().toString().trim();
        String notes = binding.editTextNotes.getText().toString().trim();

        // 2. Überprüfe, ob die Pflichtfelder ausgefüllt sind
        if (contractName.isEmpty() || monthlyCostStr.isEmpty()) {
            Toast.makeText(requireContext(), "Name und Kosten dürfen nicht leer sein.", Toast.LENGTH_SHORT).show();
            return;
        }

        double monthlyCost = Double.parseDouble(monthlyCostStr);

        // 3. Erstelle ein neues Contract-Objekt
        Contract contract = new Contract();
        contract.contractName = contractName;
        contract.monthlyCost = monthlyCost;
        contract.startDate = startDate;
        contract.endDate = endDate;
        contract.notes = notes;

        // 4. Führe die Datenbank-Aktion in einem Hintergrund-Thread aus
        executorService.execute(() -> {
            try {
                db.contractDao().insertContract(contract);

                // Kehre zum Haupt-Thread zurück, um die UI zu aktualisieren (Toast-Nachricht)
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Vertrag gespeichert!", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                });
            } catch (Exception e) {
                // Zeige Fehler an, falls das Speichern fehlschlägt
                Log.e("SecondFragment", "Fehler beim Speichern des Vertrags: " + e.getMessage());
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Fehler beim Speichern.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}