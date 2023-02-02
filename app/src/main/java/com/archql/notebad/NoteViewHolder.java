package com.archql.notebad;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archql.notebad.databinding.NoteViewBinding;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    protected NoteViewBinding binding;

    public NoteViewHolder(@NonNull NoteViewBinding noteViewBinding) {
        super(noteViewBinding.getRoot());

        binding = noteViewBinding;
    }

    public void bind(Note n) {
        //binding.setVariable(BR.note, n);
        binding.setNote(n);
        binding.executePendingBindings();
    }

    public void noteClicked(Note n) {
        Toast.makeText(binding.getRoot().getContext(), "You clicked " + n.text,
                Toast.LENGTH_LONG).show();
    }
}
