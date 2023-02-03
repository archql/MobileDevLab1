package com.archql.notebad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.archql.notebad.databinding.FragmentSecondBinding;

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
        binding.setNote(viewModel.getSelectedNote().getValue());
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
                // go back
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
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
                binding.setBtStorageSwitchState(!binding.getBtStorageSwitchState());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sqliteStorage.close();
        binding = null;
    }

    private boolean save() {
        boolean isSaveToSQL = binding.getBtStorageSwitchState();
        StoredNote note = binding.getNote();
        if (isSaveToSQL) {
            return note.store(sqliteStorage);
        } else {
            return note.store(localStorage);
        }
    }

}