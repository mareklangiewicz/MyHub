package pl.mareklangiewicz.myhub.ui;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import pl.mareklangiewicz.myhub.data.Note;
import pl.mareklangiewicz.myhub.R;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private @Nullable List<Note> notes;

    @Inject
    public NotesAdapter() { }

    public NotesAdapter(@Nullable List<Note> notes) {
        this.notes = notes;
    }

    public void setNotes(@Nullable List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public @Nullable List<Note> getNotes() { return notes; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mg_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //noinspection ConstantConditions
        Note note = notes.get(position);
        holder.mLabelTextView.setText(note.getLabel());
        holder.mTextTextView.setText(note.getText());
    }

    @Override
    public int getItemCount() {
        return notes == null ? 0 : notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mLabelTextView;
        public TextView mTextTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mLabelTextView = (TextView) itemView.findViewById(R.id.label);
            mTextTextView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
