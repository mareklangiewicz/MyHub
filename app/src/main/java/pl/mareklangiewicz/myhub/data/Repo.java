package pl.mareklangiewicz.myhub.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Repo extends RealmObject {

    @Required private String name;
    private String description;
    private long forks;
    private long watchers;
    private long stars;
    private RealmList<Note> notes = new RealmList<>();

    public Repo(@NonNull String name, @Nullable String description, long forks, long watchers, long stars) {
        this.name = name;
        this.description = description;
        this.forks = forks;
        this.watchers = watchers;
        this.stars = stars;
    }

    public Repo() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getForks() {
        return forks;
    }

    public void setForks(long forks) {
        this.forks = forks;
    }

    public long getWatchers() {
        return watchers;
    }

    public void setWatchers(long watchers) {
        this.watchers = watchers;
    }

    public long getStars() {
        return stars;
    }

    public void setStars(long stars) {
        this.stars = stars;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }

    public void setNotes(RealmList<Note> notes) {
        this.notes = notes;
    }
}
