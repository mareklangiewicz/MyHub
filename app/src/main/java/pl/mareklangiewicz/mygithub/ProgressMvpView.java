package pl.mareklangiewicz.mygithub;

public interface ProgressMvpView extends MvpView {
    int HIDDEN = -1;
    int UNKNOWN = -2; // some moving state indicating that something is happening
    int MIN = 0;
    int MAX = 10000;

    void setProgress(int progress);
    int getProgress();
}
