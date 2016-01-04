package pl.mareklangiewicz.mygithub;

import android.support.annotation.Nullable;

import java.util.List;

import pl.mareklangiewicz.mygithub.data.Note;

public interface NotesMvpView extends MvpView {
    void setNotes(@Nullable List<Note> notes);
    @Nullable List<Note> getNotes();
}
