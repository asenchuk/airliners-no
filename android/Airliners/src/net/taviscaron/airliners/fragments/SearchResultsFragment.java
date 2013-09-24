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
import net.taviscaron.airliners.util.CommonUtil;

/**
 * Search results fragment
 * Shows aircraft search result items
 * @author Andrei Senchuk
 */
public class SearchResultsFragment extends Fragment {
    public static final String LOADER_TYPE_KEY = "loaderType";
    public static final String SEARCH_PARAMS_KEY = "searchParams";

    private static final int DEFAULT_ITEMS_PER_PAGE = 50;

    public interface OnShowAircraftInfoListener {
        public void showAircraftInfo(String id);
    }

    private View emptyView;
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
            if(isResumed() && adapter.getCount() == 0) {
                searchListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        }

        public void loadFinished(boolean success) {
            if(isResumed()) {
                searchListView.setVisibility(adapter.isEmpty() ? View.GONE : View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                emptyView.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);
            }

            if(!success) {
                searchLoadFailed();
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

        emptyView = view.findViewById(R.id.search_results_empty);
        emptyView.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);

        if(adapter.getLoaderType() != SearchResultsAdapter.LoaderType.SEARCH) {
            view.findViewById(R.id.search_results_empty_line1).setVisibility(View.GONE);
            view.findViewById(R.id.search_results_empty_line2).setVisibility(View.GONE);
            view.findViewById(R.id.search_results_empty_line3).setVisibility(View.GONE);
        }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        progressBar = null;
        searchListView = null;
        emptyView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
    }

    private void searchLoadFailed() {
        if(isResumed()) {
            int messageId = CommonUtil.isNetworkAvailable(getActivity()) ? R.string.search_failed_service_unavailable : R.string.search_failed_network_unavailable;
            if(adapter.isEmpty()) {
                AlertDialogFragment.createAlert(getActivity(), messageId).show(getFragmentManager());
            } else {
                Toast.makeText(getActivity(), messageId, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
