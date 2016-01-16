package pl.mareklangiewicz.myhub.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Marek Langiewicz on 03.01.16.
 *
 */
public class Account extends RealmObject {

    /**
     * When it was fetched from github - in miliseconds from 1.1.1970
     */
    @Index private long time;
    @PrimaryKey private String login;
    @Required @Index private String name;
    private String avatar;
    private String description;
    private RealmList<Note> notes = new RealmList<>();
    private RealmList<Repo> repos = new RealmList<>();

    public Account() { }

    public Account(long time, @NonNull String login, @NonNull String name, @Nullable String avatar, @Nullable String description) {
        this.time = time;
        this.login = login;
        this.avatar = avatar;
        this.name = name;
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

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

    public RealmList<Note> getNotes() {
        return notes;
    }

    public void setNotes(RealmList<Note> notes) {
        this.notes = notes;
    }

    public RealmList<Repo> getRepos() {
        return repos;
    }

    public void setRepos(RealmList<Repo> repos) {
        this.repos = repos;
    }
}

