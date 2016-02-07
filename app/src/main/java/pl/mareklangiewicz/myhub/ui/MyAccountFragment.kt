package pl.mareklangiewicz.myhub.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.mh_basic_user_info.*
import kotlinx.android.synthetic.main.mh_fragment_my_account.*
import kotlinx.android.synthetic.main.mh_notes.view.*
import pl.mareklangiewicz.myfragments.MyFragment
import pl.mareklangiewicz.myhub.MHApplication
import pl.mareklangiewicz.myhub.MyAccountPresenter
import pl.mareklangiewicz.myhub.R
import javax.inject.Inject

class MyAccountFragment : MyFragment() {

    // TODO LATER: implement AutoCompleteTextView instead of EditText for login field (propose users already saved in local db)

    @Inject lateinit var presenter: MyAccountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as MHApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflateHeader(R.layout.mh_notes)
        return inflater.inflate(R.layout.mh_fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val v = AMyAccountView(
                mh_fma_pb_progress,
                mh_fma_tv_status,
                mh_fma_ed_login,
                mh_fma_ed_password,
                mh_fma_ed_otp,
                mh_fma_b_login,
                mh_bui_iv_avatar,
                mh_bui_tv_name,
                mh_bui_tv_description,
                header!!.mh_n_rv_notes
        )
        presenter.view = v
    }

    override fun onDestroyView() {
        presenter.view = null
        super.onDestroyView()
    }
}
