package pl.mareklangiewicz.mygithub.data;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Marek Langiewicz on 03.01.16.
 *
 */
public class Account {
    public Account(Uri avatar, String name, String description, Note... notes) {
        this.avatar = avatar;
        this.name = name;
        this.description = description;
        this.notes = new ArrayList<>(Arrays.asList(notes));
    }
    public Uri avatar;
    public String name;
    public String description;
    public List<Note> notes;
}
