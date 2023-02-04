package com.archql.notebad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SortedList;

import com.archql.notebad.databinding.FragmentFirstBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

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
                // send new note to edit
                viewModel.setSelectedNote(n);
                // navigate to edit note page + selected note
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
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
        for (StoredNote model : models) {
            final String text = model.getStored().getText().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}