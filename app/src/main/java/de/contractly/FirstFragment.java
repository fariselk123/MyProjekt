package de.contractly;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.contractly.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private AppDatabase db;
    private ExecutorService executorService;
    private ContractAdapter contractAdapter;
    private TextView totalCostTextView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisiere die Datenbank und den Executor
        db = AppDatabase.getDatabase(requireContext());
        executorService = Executors.newSingleThreadExecutor();

        // Initialisiere RecyclerView
        RecyclerView recyclerView = binding.recyclerViewContracts;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        contractAdapter = new ContractAdapter();
        recyclerView.setAdapter(contractAdapter);

        // Initialisiere TextView für die Gesamtkosten
        totalCostTextView = binding.textViewTotalCost; // Verwende Binding für sicheren Zugriff

        // Lade die Daten, wenn das Fragment erstellt wird
        loadContractsFromDatabase();
    }



    private void loadContractsFromDatabase() {
        executorService.execute(() -> {
            try {
                // Lade die Liste der Verträge
                List<Contract> contracts = db.contractDao().getAllContracts();

                // Lade die Gesamtkosten
                double totalCost = db.contractDao().getTotalMonthlyCost();

                // Kehre zum Haupt-Thread zurück, um die UI zu aktualisieren
                requireActivity().runOnUiThread(() -> {
                    contractAdapter.setContracts(contracts);
                    updateTotalCost(totalCost);
                });
            } catch (Exception e) {
                Log.e("FirstFragment", "Fehler beim Laden der Verträge: " + e.getMessage());
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Fehler beim Laden der Verträge.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void updateTotalCost(double totalCost) {
        // Implementiere die Logik für die Anzeige der Gesamtkosten
        totalCostTextView.setText(String.format("Gesamtkosten: %.2f€", totalCost));
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