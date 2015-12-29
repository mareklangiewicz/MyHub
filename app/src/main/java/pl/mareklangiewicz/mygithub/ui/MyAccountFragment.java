package pl.mareklangiewicz.mygithub.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.mareklangiewicz.myfragments.MyFragment;
import pl.mareklangiewicz.mygithub.R;

public class MyAccountFragment extends MyFragment {
    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mg_fragment_my_account, container, false);
    }
}
