package be.simonraes.telemeter.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import be.simonraes.telemeter.R;
import be.simonraes.telemeter.model.Usage;

/**
 * Created by Simon Raes on 20/06/2014.
 */
public class StatusFragment extends Fragment {

    private TextView txtStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.status_fragment_layout, container, false);
        txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        return view;
    }

    public void setStatus(String status) {
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cooper.ttf");
        txtStatus.setTypeface(tf);
        txtStatus.setText(status);
    }
}