package net.taviscaron.airliners.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.adapters.SearchResultsAdapter;
import net.taviscaron.airliners.model.AircraftSearchResult;
import net.taviscaron.airliners.model.SearchParams;

/**
 * Search results fragment
 * Shows aircraft search result items
 * @author Andrei Senchuk
 */
public class SearchResultsFragment extends Fragment {
    private static final String LOADER_TYPE_KEY = "loaderType";
    private static final String SEARCH_PARAMS_KEY = "searchParams";

    private static final int DEFAULT_ITEMS_PER_PAGE = 50;

    public interface OnShowAircraftInfoListener {
        public void showAircraftInfo(String id);
    }

    private ProgressBar progressBar;
    private GridView searchListView;
    private SearchResultsAdapter adapter;

    private final SearchResultsAdapter.SearchResultsAdapterListener searchResultsAdapterListener = new SearchResultsAdapter.SearchResultsAdapterListener() {
        public void searchResultItemThumbClicked(AircraftSearchResult result, int position) {
            if(result.getId() != null && getActivity() instanceof OnShowAircraftInfoListener) {
                ((OnShowAircraftInfoListener)getActivity()).showAircraftInfo(result.getId());
            }
        }

        public void loadStarted() {
            if(adapter.getCount() == 0) {
                searchListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        public void loadFinished() {
            if(adapter.getCount() == 0) {
                searchListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    public static SearchResultsFragment newInstance(SearchResultsAdapter.LoaderType loaderType, SearchParams params) {
        Bundle bundle = new Bundle();
        bundle.putInt(LOADER_TYPE_KEY, loaderType.ordinal());
        bundle.putSerializable(SEARCH_PARAMS_KEY, params);

        SearchResultsFragment fragment = new SearchResultsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            adapter = new SearchResultsAdapter(getActivity(), searchResultsAdapterListener, savedInstanceState);
        } else {
            Bundle args = getArguments();
            SearchResultsAdapter.LoaderType loaderType = SearchResultsAdapter.LoaderType.values()[args.getInt(LOADER_TYPE_KEY)];
            SearchParams params = (SearchParams)args.getSerializable(SEARCH_PARAMS_KEY);
            adapter = new SearchResultsAdapter(getActivity(), searchResultsAdapterListener, loaderType, params, DEFAULT_ITEMS_PER_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_results, container, false);

        progressBar = (ProgressBar)view.findViewById(R.id.search_progress_view);
        searchListView = (GridView)view.findViewById(R.id.search_results_list_view);

        searchListView.setAdapter(adapter);
        searchListView.setOnScrollListener(adapter);

        adapter.performInitialLoadIfNeeded();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }
}
