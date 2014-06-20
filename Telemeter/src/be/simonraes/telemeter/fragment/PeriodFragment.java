package be.simonraes.telemeter.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import be.simonraes.telemeter.R;
import be.simonraes.telemeter.model.Period;

import java.text.SimpleDateFormat;

/**
 * Created by Simon Raes on 15/06/2014.
 */
public class PeriodFragment extends Fragment {

    private TextView txtPeriod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.period_fragment_layout, container, false);
        txtPeriod = (TextView) view.findViewById(R.id.txtPeriod);
        return view;
    }

    public void setPeriod(Period period) {
        String from = new SimpleDateFormat("dd/MM").format(period.getFrom());
        String till = new SimpleDateFormat("dd/MM").format(period.getTill());
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cooper.ttf");
        txtPeriod.setTypeface(tf);
        txtPeriod.setText("Dag " + period.getCurrentDay() + " (" + from + " - " + till + ")");
    }
}
