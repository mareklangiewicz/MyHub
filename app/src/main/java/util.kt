import android.widget.TextView
import com.noveogroup.android.log.MyLogger
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by Marek Langiewicz on 29.01.16.
 * Some utilities
 */

operator fun TextView.setValue(obj: Any?, property: Any?, arg: CharSequence) {
    text = arg
}

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
                override fun onError(e: Throwable?) {
                    if(logOnError != null)
                        log.e(e, logOnError, e?.message ?: "")
                }

                override fun onCompleted() {
                    if(logOnCompleted != null)
                        log.v(logOnCompleted)
                }

                override fun onNext(item: T) {
                    onNext(item)
                }
            }
    )
}

fun <T> Observable<T>.lsubscribe(
        log: MyLogger,
        logOnError: String? = "[SNACK]Error %s",
        logOnCompleted: String? = "completed",
        logOnNext: String? = "next %s"
): Subscription = lsubscribe(log, logOnError, logOnCompleted) { if(logOnNext != null) log.v(logOnNext, it.toString()) }

