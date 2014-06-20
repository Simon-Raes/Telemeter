package be.simonraes.telemeter.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.simonraes.telemeter.R;

/**
 * Created by Simon Raes on 20/06/2014.
 */
public class FillerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filler_fragment_layout, null);
        return view;
    }
}
