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

        // setup data model
        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        viewModel.getSelectedNote().observe(getViewLifecycleOwner(), item -> {
            // Perform some actions
        });

        // create adapter
        adapter = new NoteViewAdapter(getContext(), new ArrayList<>(), new NoteViewAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note n) {
                // send new note to edit
                viewModel.setSelectedNote(n);
                // navigate to edit note page + selected note
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        // logics setup here
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        // setup fab
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send new note to edit
                Note n = new Note();
                adapter.addNote(n);
                viewModel.setSelectedNote(n);
                // navigate to edit note page + newly created note
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        // TODO
        adapter.addNote(new Note("My first note", "blah blah"));
        adapter.addNote(new Note("My second note", "blah blah blah"));
        adapter.addNote(new Note("My third note", "blah blah blah b"));
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