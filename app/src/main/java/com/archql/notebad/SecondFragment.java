package com.archql.notebad;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.archql.notebad.databinding.FragmentSecondBinding;

import java.io.Console;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private NoteViewModel viewModel;
    private LocalCRUDStorage localStorage;
    private SQLiteCRUDStorage sqliteStorage;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // layout
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setup data model
        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        StoredNote sn = viewModel.getSelectedNote().getValue();
        binding.setNote(sn);

        // do not need live updates
        //viewModel.getSelectedNote().observe(getViewLifecycleOwner(), item -> {
        //    binding.setNote(item);
        //});
        localStorage = viewModel.getLocalStorage().getValue();
        sqliteStorage = viewModel.getSQLiteStorage().getValue();

        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send new note to edit
                //viewModel.setSelectedNote(binding.getNote());
                // TODO null check
                StoredNote n = binding.getNote();
                if (n.getStored().text.equals("")) {
                    Toast.makeText(getActivity(), "Note is empty, so nothing is saved!", Toast.LENGTH_LONG ).show();
                } else {
                    if (save()) {
                        Toast.makeText(getActivity(), "Note is saved successfully :)", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Note save failed :/", Toast.LENGTH_LONG).show();
                    }
                }
                goBack();
            }
        });

        binding.btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (save()) {
                    Toast.makeText(getActivity(), "Note is saved successfully :)", Toast.LENGTH_LONG ).show();
                } else {
                    Toast.makeText(getActivity(), "Note save failed :/", Toast.LENGTH_LONG ).show();
                }
            }
        });

        binding.btStorageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoredNote note = binding.getNote(); // TODO null check
                note.setStorageType(note.getStorageType().next());
                binding.invalidateAll(); // TODO find better
            }
        });

        binding.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoredNote note = binding.getNote(); // TODO null check
                if (note.storageType != STORAGE_TYPE.NO_STORAGE) {
                    ICRUDStorage<Note, StoredNote> storage = getStorage(note.storageType);
                    storage.Delete(note);
                }
                goBack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean save() {
        // TODO null check
        StoredNote note = binding.getNote();

        // Ignore default storage type
        if (note.storageType == STORAGE_TYPE.NO_STORAGE) {
            note.setStorageType(note.getStorageType().next());
            binding.invalidateAll(); // TODO find better
        }

        boolean result = false;
        ICRUDStorage<Note, StoredNote> storage = getStorage(note.storageType);
        if (note.lastStorageType != note.storageType) {
            // if note in new storage type
            long oldId = note.getId();
            result = storage.Create(note); // ID must be changed here! TODO make it compulsory
            Log.e("TAG", "Create: " + result);

            if (note.lastStorageType != STORAGE_TYPE.NO_STORAGE) {
                // delete from old storage if needed
                ICRUDStorage<Note, StoredNote> lastStorage = getStorage(note.lastStorageType);
                Log.e("TAG", "Delete: " + lastStorage.Delete(oldId));
            }
            // set type back!
            note.lastStorageType = note.storageType;
        } else {
            // if already existent note
            note.getStored().edited();
            result = storage.Update(note);
            Log.e("TAG", "Update: " + result);
        }
        return result;
    }

    //private void updateBtStorageSwitchColor(STORAGE_TYPE storageType) { // @{ContextCompat.getColor(context, btStorageSwitchColorId)
    //    switch (storageType) {
    //        case LOCAL: binding.setBtStorageSwitchColorId(R.color.purple_500);
    //        case SQLite: binding.setBtStorageSwitchColorId(R.color.teal_700);
    //        default: binding.setBtStorageSwitchColorId(R.color.black);
    //    }
    //}

    private ICRUDStorage<Note, StoredNote> getStorage(STORAGE_TYPE storageType) {
        switch (storageType) {
            case LOCAL: return localStorage;
            case SQLite: return sqliteStorage;
            default: return localStorage;
        }
    }

    private void goBack() {
        // go back
        NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

}