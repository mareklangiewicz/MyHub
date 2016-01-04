package pl.mareklangiewicz.mygithub.ui;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import pl.mareklangiewicz.mygithub.data.Note;
import pl.mareklangiewicz.mygithub.R;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private @Nullable List<Note> mNotes;

    @Inject
    public NotesAdapter() { }

    public NotesAdapter(@Nullable List<Note> notes) {
        this.mNotes = notes;
    }

    public void setNotes(@Nullable List<Note> notes) {
        this.mNotes = notes;
        notifyDataSetChanged();
    }

    public @Nullable List<Note> getNotes() { return mNotes; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mg_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //noinspection ConstantConditions
        Note note = mNotes.get(position);
        holder.mLabelTextView.setText(note.label);
        holder.mTextTextView.setText(note.text);
    }

    @Override
    public int getItemCount() {
        return mNotes == null ? 0 : mNotes.size();
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
