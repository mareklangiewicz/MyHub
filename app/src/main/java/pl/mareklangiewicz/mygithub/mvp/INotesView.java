package pl.mareklangiewicz.mygithub.mvp;

import android.support.annotation.Nullable;

import java.util.List;

import pl.mareklangiewicz.mygithub.data.Note;

public interface INotesView extends IView {
    void setNotes(@Nullable List<Note> notes);
    @Nullable List<Note> getNotes();
}
