package com.archql.notebad.ui.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archql.notebad.databinding.NoteViewBinding;
import com.archql.notebad.entities.StoredNote;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    protected NoteViewBinding binding;

    public NoteViewHolder(@NonNull NoteViewBinding noteViewBinding) {
        super(noteViewBinding.getRoot());

        binding = noteViewBinding;
    }

    public void bind(StoredNote n) {
        //binding.setVariable(BR.note, n);
        binding.setNote(n);
        binding.executePendingBindings();
    }
}
