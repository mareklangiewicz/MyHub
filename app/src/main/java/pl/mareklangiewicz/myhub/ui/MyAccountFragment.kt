package pl.mareklangiewicz.myhub.ui

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.mg_basic_user_info.*
import kotlinx.android.synthetic.main.mg_fragment_my_account.*
import pl.mareklangiewicz.myfragments.MyFragment
import pl.mareklangiewicz.myhub.*
import pl.mareklangiewicz.myhub.data.Note
import javax.inject.Inject

class MyAccountFragment : MyFragment() {

    // TODO LATER: implement AutoCompleteTextView instead of EditText for login field (propose users already saved in local db)

    @Inject lateinit var presenter: MyAccountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as MGApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.mg_fragment_my_account, container, false)
        inflateHeader(R.layout.mg_notes)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_login.setOnClickListener { presenter.login() }
        val v = AMyAccountView(
                text_view_status,
                edit_text_login,
                edit_text_password,
                edit_text_otp,
                image_view_avatar,
                text_view_name,
                text_view_description,
                ANotesView(header.findViewById(R.id.notes_recycler_view) as RecyclerView),
                AProgressView(progress_bar, log)
        )
        v.notes = listOf(Note("No info", "Log in to get info"))
        presenter.view = v
    }

    override fun onDestroyView() {
        presenter.view = null
        super.onDestroyView()
    }
}
