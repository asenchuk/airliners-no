package net.taviscaron.airliners.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.model.SearchParams;
import net.taviscaron.airliners.util.UIUtil;

/**
 * Search fragment
 * @author Andrei Senchuk
 */
public class SearchFragment extends Fragment {
    public interface OnSearchListener {
        public void onSearch(SearchParams params);
    }

    private EditText aircraftEditText;
    private EditText airlineEditText;
    private EditText placeEditText;
    private EditText countryEditText;
    private EditText dateEditText;
    private EditText yearEditText;
    private EditText regEditText;
    private EditText cnEditText;
    private EditText codeEditText;
    private EditText remarkEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_view, container, false);

        aircraftEditText = (EditText)view.findViewById(R.id.search_param_aircraft);
        airlineEditText = (EditText)view.findViewById(R.id.search_param_airline);
        placeEditText = (EditText)view.findViewById(R.id.search_param_place);
        countryEditText = (EditText)view.findViewById(R.id.search_param_country);
        dateEditText = (EditText)view.findViewById(R.id.search_param_date);
        yearEditText = (EditText)view.findViewById(R.id.search_param_year);
        regEditText = (EditText)view.findViewById(R.id.search_param_reg);
        cnEditText = (EditText)view.findViewById(R.id.search_param_cn);
        codeEditText = (EditText)view.findViewById(R.id.search_param_code);
        remarkEditText = (EditText)view.findViewById(R.id.search_param_remark);

        view.findViewById(R.id.search_button_go).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchButtonTapped();
            }
        });

        view.findViewById(R.id.search_button_clear).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearButtonTapped();
            }
        });

        return view;
    }

    private void searchButtonTapped() {
        UIUtil.hideSoftKeyboard(getActivity());

        Activity activity = getActivity();
        if(activity instanceof OnSearchListener) {
            SearchParams sp = searchParamsFromForm();
            if(!sp.isEmpty()) {
                ((OnSearchListener)activity).onSearch(sp);
            } else {
                Toast.makeText(getActivity(), R.string.search_empty_params_toast, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearButtonTapped() {
        UIUtil.hideSoftKeyboard(getActivity());
        aircraftEditText.setText(null);
        airlineEditText.setText(null);
        placeEditText.setText(null);
        countryEditText.setText(null);
        dateEditText.setText(null);
        yearEditText.setText(null);
        regEditText.setText(null);
        cnEditText.setText(null);
        codeEditText.setText(null);
        remarkEditText.setText(null);
    }

    private SearchParams searchParamsFromForm() {
        SearchParams sp = new SearchParams();
        sp.setAircraft(aircraftEditText.getText().toString());
        sp.setAirline(airlineEditText.getText().toString());
        sp.setPlace(placeEditText.getText().toString());
        sp.setCountry(countryEditText.getText().toString());
        sp.setDate(dateEditText.getText().toString());
        sp.setYear(yearEditText.getText().toString());
        sp.setReg(regEditText.getText().toString());
        sp.setCn(cnEditText.getText().toString());
        sp.setCode(codeEditText.getText().toString());
        sp.setRemark(remarkEditText.getText().toString());
        return sp;
    }
}
