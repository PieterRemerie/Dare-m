package be.nmct.howest.darem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class InviteDetailFragment extends Fragment {
    public InviteDetailFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View InviteDetailFragmentView = inflater.inflate(R.layout.fragment_invite_detail, container, false);
        return InviteDetailFragmentView;
    }
}
