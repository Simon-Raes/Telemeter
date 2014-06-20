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
 * Created by Simon Raes on 15/06/2014.
 */
public class UsageFragment extends Fragment {

    private TextView txtUsageCurrent, txtUsageRemaining;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.usage_fragment_layout, container, false);
        txtUsageCurrent = (TextView) view.findViewById(R.id.txtUsageUsed);
        txtUsageRemaining = (TextView) view.findViewById(R.id.txtUsageRemaining);
        return view;
    }

    public void setUsage(Usage usage) {
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cooper.ttf");
        txtUsageCurrent.setTypeface(tf);
        txtUsageCurrent.setText("Gebruikt: " + usage.getTotalUsage() + usage.getUnit());
        txtUsageRemaining.setTypeface(tf);
        txtUsageRemaining.setText("Resterend: " + usage.getMinUsageRemaining() + usage.getUnit() + " - " + usage.getMaxUsageRemaining() + usage.getUnit());
    }


}
