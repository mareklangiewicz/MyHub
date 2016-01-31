import android.widget.TextView

/**
 * Created by Marek Langiewicz on 29.01.16.
 * Some utilities
 */

operator fun TextView.setValue(obj: Any?, property: Any?, arg: CharSequence) {
    text = arg
}

operator fun TextView.getValue(obj: Any?, property: Any?): String = text.toString()
