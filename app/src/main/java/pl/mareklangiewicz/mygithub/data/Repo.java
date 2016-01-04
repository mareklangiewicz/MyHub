package pl.mareklangiewicz.mygithub.data;

public class Repo {

    public Repo(String name, String description, long forks, long watchers, long stars) {
        this.name = name;
        this.description = description;
        this.forks = forks;
        this.watchers = watchers;
        this.stars = stars;
    }

    public String name;
    public String description;
    public long forks;
    public long watchers;
    public long stars;
}
