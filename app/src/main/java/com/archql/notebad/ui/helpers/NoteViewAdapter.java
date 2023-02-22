package com.archql.notebad.ui.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.archql.notebad.databinding.NoteViewBinding;
import com.archql.notebad.entities.StoredNote;

import java.util.List;

public class NoteViewAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    // interface for handling item clicks
    public interface OnNoteClickListener{
        void onNoteClick(StoredNote n);
    }

    private Context ctx;
    private OnNoteClickListener noteClickListener;

    public NoteViewAdapter(Context ctx, OnNoteClickListener clickListener) {
        this.ctx = ctx;
        this.noteClickListener = clickListener;
    }

    public void add(StoredNote model) {
        sortedList.add(model);
    }

    public void remove(StoredNote model) {
        sortedList.remove(model);
    }

    public void add(List<StoredNote> models) {
        sortedList.addAll(models);
    }

    public void remove(List<StoredNote> models) {
        sortedList.beginBatchedUpdates();
        for (StoredNote model : models) {
            sortedList.remove(model);
        }
        sortedList.endBatchedUpdates();
    }

    public void replaceAll(List<StoredNote> models) {
        sortedList.beginBatchedUpdates();
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            final StoredNote model = sortedList.get(i);
            if (!models.contains(model)) {
                sortedList.remove(model);
            }
        }
        sortedList.addAll(models);
        sortedList.endBatchedUpdates();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final NoteViewBinding binding = NoteViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final StoredNote n = sortedList.get(position);
        holder.bind(n);
        holder.binding.setNoteClickListener(noteClickListener);
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    // search
    private final SortedList<StoredNote> sortedList = new SortedList<>(StoredNote.class, new SortedList.Callback<StoredNote>() {

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public int compare(StoredNote a, StoredNote b) {
            // TODO replace with comparator
            return a.getStored().getDateCreated().compareTo(b.getStored().getDateCreated()); //mComparator.compare(a, b);
        }

        @Override
        public boolean areContentsTheSame(StoredNote oldItem, StoredNote newItem) {
            return oldItem.equals(newItem); // TODO
        }

        @Override
        public boolean areItemsTheSame(StoredNote item1, StoredNote item2) {
            return item1.getId() == item2.getId();
        }
    });
}
