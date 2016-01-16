package pl.mareklangiewicz.myhub.mvp;

import android.support.annotation.Nullable;

import java.util.List;

import pl.mareklangiewicz.myhub.data.Note;

public interface INotesView extends IView {
    void setNotes(@Nullable List<Note> notes);
    @Nullable List<Note> getNotes();
}
