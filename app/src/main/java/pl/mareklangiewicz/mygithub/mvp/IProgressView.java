package pl.mareklangiewicz.mygithub.mvp;

public interface IProgressView extends IView {
    int HIDDEN = -1;
    int INDETERMINATE = -2; // some moving state indicating that something is happening
    int MIN = 0;
    int MAX = 10000;

    void setProgress(int progress);
    int getProgress();
}
