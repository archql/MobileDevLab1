package com.archql.notebad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.archql.notebad.databinding.FragmentFirstBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private NoteViewAdapter adapter;
    private NoteViewModel viewModel;

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
        adapter = new NoteViewAdapter(getContext(), new ArrayList<>(), new NoteViewAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(StoredNote n) {
                // send new note to edit
                viewModel.setSelectedNote(n);
                // navigate to edit note page + selected note
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        // Load all notes from local storage
        viewModel.getLocalStorage().observe(getViewLifecycleOwner(), localStorage -> {
            List<StoredNote> notesFromLS = localStorage.ReadAll();
            for (StoredNote o : notesFromLS) {
                adapter.addNote(o);
            }
        });
        // Load all notes from sqlite
        viewModel.getSQLiteStorage().observe(getViewLifecycleOwner(), sqliteStorage -> {
            List<StoredNote> notesFromLS = sqliteStorage.ReadAll();
            for (StoredNote o : notesFromLS) {
                adapter.addNote(o);
            }
        });

        // do not need live updates
        //viewModel.getSelectedNote().observe(getViewLifecycleOwner(), item -> {
        //    // Perform some actions
        //    adapter.updateNote(item);
        //});
        adapter.updateNote(viewModel.getSelectedNote().getValue());

        // setup fab
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send new note to edit
                StoredNote n = new StoredNote();
                adapter.addNote(n);
                viewModel.setSelectedNote(n);
                // navigate to edit note page + newly created note
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        // set recycler
        binding.recyclerNotes.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.recyclerNotes.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}