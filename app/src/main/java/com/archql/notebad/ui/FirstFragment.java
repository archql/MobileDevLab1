package com.archql.notebad.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.archql.notebad.ui.helpers.NoteViewAdapter;
import com.archql.notebad.ui.helpers.NoteViewModel;
import com.archql.notebad.R;
import com.archql.notebad.entities.StoredNote;
import com.archql.notebad.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.List;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private NoteViewAdapter adapter;
    private NoteViewModel viewModel;
    private List<StoredNote> notes;

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
        // Called every time fragment is swapped
        // setup data model
        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        // create adapter
        adapter = new NoteViewAdapter(getContext(), new NoteViewAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(StoredNote n) {
                // check if selected note is encrypted
                if (n.getStored().isEncrypted()) {
                    checkBiometrics(n);
                } else {
                    navigateToNote(n);
                }
            }
        });

        notes = new ArrayList<>();
        // Load all notes from local storage
        viewModel.getLocalStorage().observe(getViewLifecycleOwner(), localStorage -> {
            List<StoredNote> loaded = localStorage.ReadAll();
            notes.addAll(loaded);
            adapter.add(loaded);
        });
        // Load all notes from sqlite
        viewModel.getSQLiteStorage().observe(getViewLifecycleOwner(), sqliteStorage -> {
            List<StoredNote> loaded = sqliteStorage.ReadAll();
            notes.addAll(loaded);
            adapter.add(loaded);
        });

        // setup fab
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send new note to edit
                StoredNote n = new StoredNote();
                adapter.add(n);
                viewModel.setSelectedNote(n);
                // navigate to edit note page + newly created note
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        // set recycler
        binding.recyclerNotes.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.recyclerNotes.setAdapter(adapter);

        // setup search
        binding.srchNote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter logics here
                final List<StoredNote> filteredNotesList = filter(notes, newText);
                adapter.replaceAll(filteredNotesList);
                binding.recyclerNotes.scrollToPosition(0);
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static List<StoredNote> filter(List<StoredNote> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<StoredNote> filteredModelList = new ArrayList<>();
        if (lowerCaseQuery.equals("")) {
            filteredModelList.addAll(models);
        } else {
            for (StoredNote model : models) {
                final String text = model.getStored().getText().toLowerCase();
                if (!model.getStored().isEncrypted() && text.contains(lowerCaseQuery)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    private void navigateToNote(StoredNote n) {
        // send new note to edit
        viewModel.setSelectedNote(n);
        // navigate to edit note page + selected note
        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }

    public static boolean checkBiometricsAccess(Context ctx) {
        BiometricManager biometricManager = BiometricManager.from(ctx);
        return biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)
                == BiometricManager.BIOMETRIC_SUCCESS;
    }

    private void checkBiometrics(StoredNote n) {

        if (!checkBiometricsAccess(getContext())) {
            Toast.makeText(getContext(), "Auth is impossible on this device!", Toast.LENGTH_LONG).show();
            return;
        }

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Auth")
                .setSubtitle("Log in to see note's content")
                .setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(getActivity(), ContextCompat.getMainExecutor(getContext()),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        Toast.makeText(getContext(), "Auth error!", Toast.LENGTH_LONG).show();
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        Toast.makeText(getContext(), "Auth OK", Toast.LENGTH_LONG).show();
                        navigateToNote(n);
                        super.onAuthenticationSucceeded(result);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        Toast.makeText(getContext(), "Auth failed!", Toast.LENGTH_LONG).show();
                        super.onAuthenticationFailed();
                    }
                });
        biometricPrompt.authenticate(promptInfo);
    }
}