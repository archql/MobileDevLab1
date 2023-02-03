package com.archql.notebad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archql.notebad.databinding.NoteViewBinding;

import java.util.List;

public class NoteViewAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    // interface for handling item clicks
    public interface OnNoteClickListener{
        void onNoteClick(StoredNote n);
    }

    private Context ctx;
    private List<StoredNote> notes;
    private OnNoteClickListener noteClickListener;

    public NoteViewAdapter(Context ctx, List<StoredNote> notes, OnNoteClickListener clickListener) {
        this.ctx = ctx;
        this.notes = notes;
        this.noteClickListener = clickListener;
    }

    public void addNote(StoredNote n) {
        notes.add(n);
        notifyItemInserted(notes.size() - 1);
    }
    public void updateNote(StoredNote n) {
        int index = notes.indexOf(n);
        if (index != -1) {
            notifyItemChanged(index);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NoteViewBinding binding = NoteViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        StoredNote n = notes.get(position);
        holder.bind(n);
        holder.binding.setNoteClickListener(noteClickListener);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
