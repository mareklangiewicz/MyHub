import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.noveogroup.android.log.MyLogger
import com.squareup.picasso.Picasso
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by Marek Langiewicz on 29.01.16.
 * Some utilities
 */

operator fun TextView.setValue(obj: Any?, property: Any?, arg: CharSequence) { text = arg }

operator fun TextView.getValue(obj: Any?, property: Any?): String = text.toString()

operator fun CompositeSubscription.plusAssign(subscription: Subscription) = add(subscription)

inline fun <T> Observable<T>.lsubscribe(
        log: MyLogger,
        logOnError: String? = "[SNACK]Error %s",
        logOnCompleted: String? = "completed",
        crossinline onNext: (T) -> Unit
): Subscription {
    return subscribe(
            object : Observer<T> {
                override fun onError(e: Throwable?) { if(logOnError != null) log.e(e, logOnError, e?.message ?: "") }

                override fun onCompleted() { if(logOnCompleted != null) log.v(logOnCompleted) }

                override fun onNext(item: T) { onNext(item) }
            }
    )
}

fun <T> Observable<T>.lsubscribe(
        log: MyLogger,
        logOnError: String? = "[SNACK]Error %s",
        logOnCompleted: String? = "completed",
        logOnNext: String? = "next %s"
): Subscription = lsubscribe(log, logOnError, logOnCompleted) { if(logOnNext != null) log.v(logOnNext, it.toString()) }

fun ImageView.loadUrl(url: String) {
    Picasso.with(context).load(url).into(this)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(it) }
}

operator fun ViewGroup.get(pos: Int): View = getChildAt(pos)

val ViewGroup.views: List<View>
    get() = (0 until childCount).map { this[it] }


