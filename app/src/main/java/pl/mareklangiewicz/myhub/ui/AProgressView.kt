package pl.mareklangiewicz.myhub.ui

import android.view.View
import android.widget.ProgressBar
import com.noveogroup.android.log.MyLogger
import pl.mareklangiewicz.myhub.mvp.IProgressView
import javax.inject.Named

/**
 * Created by Marek Langiewicz on 29.01.16.
 * Android implementation of IProgressView
 */
class AProgressView(private val bar: ProgressBar, @Named("UI") private val log: MyLogger) : IProgressView {

    override var progress = IProgressView.HIDDEN
        set(value) {
            var newvalue = value
            if (newvalue == IProgressView.INDETERMINATE) {
                bar.visibility = View.VISIBLE
                bar.progress = IProgressView.MIN
                bar.isIndeterminate = true
            } else if (newvalue == IProgressView.HIDDEN) {
                bar.visibility = View.INVISIBLE
                bar.progress = IProgressView.MIN
                bar.isIndeterminate = false
            } else {
                if (newvalue < IProgressView.MIN) {
                    log.w("correcting progress value from %d to %d", newvalue, IProgressView.MIN)
                    newvalue = IProgressView.MIN
                } else if (newvalue > IProgressView.MAX) {
                    log.w("correcting progress value from %d to %d", newvalue, IProgressView.MAX)
                    newvalue = IProgressView.MAX
                }
                bar.visibility = View.VISIBLE
                bar.progress = newvalue
                bar.isIndeterminate = false
            }
            field = newvalue
        }

    init {
        progress = progress // to synchronize bar
    }
}
