package de.contractly;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import de.contractly.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private static final String PREFS_NAME = "MyProfilePrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Daten beim Start des Fragments laden
        loadUserProfile();

        // Klick-Listener für den Speichern-Button
        binding.buttonSaveProfile.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserProfile() {
        // Shared Preferences laden
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Gespeicherte Daten abrufen
        String userName = sharedPref.getString("user_name", "");
        int defaultDuration = sharedPref.getInt("default_duration", 0);

        // Daten in die Eingabefelder setzen
        binding.editTextUserName.setText(userName);
        if (defaultDuration > 0) {
            binding.editTextDefaultDuration.setText(String.valueOf(defaultDuration));
        }
    }

    private void saveUserProfile() {
        // Shared Preferences zum Bearbeiten öffnen
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Daten aus den Eingabefeldern holen
        String userName = binding.editTextUserName.getText().toString();
        String durationText = binding.editTextDefaultDuration.getText().toString();
        int defaultDuration = durationText.isEmpty() ? 0 : Integer.parseInt(durationText);

        // Daten speichern
        editor.putString("user_name", userName);
        editor.putInt("default_duration", defaultDuration);
        editor.apply(); // Speichert die Daten asynchron

        // Feedback an den Benutzer mit einem Toast
        Toast.makeText(requireContext(), "Profil gespeichert!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}