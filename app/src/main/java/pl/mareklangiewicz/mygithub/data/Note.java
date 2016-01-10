package pl.mareklangiewicz.mygithub.data;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Note extends RealmObject {

    @Required private String label;
    private String text;

    public Note(String label, String text) {
        this.label = label;
        this.text = text;
    }

    public Note() { }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
