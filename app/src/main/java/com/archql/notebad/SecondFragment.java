package com.archql.notebad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.archql.notebad.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private NoteViewModel viewModel;
    private Note currentNote;

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
        viewModel.getSelectedNote().observe(getViewLifecycleOwner(), item -> {
            binding.setNote(item);
        });

        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send new note to edit
                viewModel.setSelectedNote(binding.getNote());
                // go back
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}