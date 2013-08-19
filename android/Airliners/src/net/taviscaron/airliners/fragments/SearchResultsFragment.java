package net.taviscaron.airliners.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.adapters.SearchResultsAdapter;
import net.taviscaron.airliners.data.BaseLoader;
import net.taviscaron.airliners.data.SearchLoader;
import net.taviscaron.airliners.model.AircraftSearchResult;
import net.taviscaron.airliners.model.SearchResult;

import java.util.Arrays;

/**
 * Search results fragment
 * Shows aircraft search result items
 * @author Andrei Senchuk
 */
public class SearchResultsFragment extends Fragment {
    private static final String LOADER_TYPE_KEY = "loaderType";
    private static final String RESULT_KEY = "resultKey";

    public enum LoaderType {
        TOP15, TOP, SEARCH
    }

    public interface OnShowAircraftInfoListener {
        public void showAircraftInfo(String id);
    }

    private LoaderType loaderType;
    private SearchLoader loader;
    private ProgressBar progressBar;
    private ListView searchListView;
    private SearchResultsAdapter adapter;
    private SearchResult searchResult;
    private Object param;

    private final BaseLoader.BaseLoaderCallback<SearchResult> baseLoaderCallback = new BaseLoader.BaseLoaderCallback<SearchResult>() {
        public void loadStarted(BaseLoader<SearchResult> loader) {
            searchListView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        public void loadFinished(BaseLoader<SearchResult> loader, SearchResult obj) {
            searchListView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            publishResult(obj);
        }
    };

    private final SearchResultsAdapter.SearchResultsAdapterListener searchResultsAdapterListener = new SearchResultsAdapter.SearchResultsAdapterListener() {
        @Override
        public void searchResultItemThumbClicked(AircraftSearchResult result, int position) {
            if(result.getId() != null && getActivity() instanceof  OnShowAircraftInfoListener) {
                ((OnShowAircraftInfoListener)getActivity()).showAircraftInfo(result.getId());
            }
        }
    };

    public SearchResultsFragment() {
        // noop; need for instantiation by the system
    }

    public SearchResultsFragment(LoaderType loaderType) {
        this.loaderType = loaderType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SearchResultsAdapter(getActivity(), searchResultsAdapterListener);

        if(savedInstanceState != null) {
            loaderType = LoaderType.values()[savedInstanceState.getInt(LOADER_TYPE_KEY, LoaderType.SEARCH.ordinal())];

            SearchResult searchResult = (SearchResult)savedInstanceState.getSerializable(RESULT_KEY);
            if(searchResult != null) {
                publishResult(searchResult);
            }
        }

        switch(loaderType) {
            case TOP:
                loader = SearchLoader.createTopLoader(getActivity());
                break;
            case TOP15:
                loader = SearchLoader.createTop15Loader(getActivity());
                break;
            case SEARCH:
                loader = SearchLoader.createSearchLoader(getActivity());
                break;
            default:
                throw new RuntimeException("Bad loader type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_results, container, false);

        progressBar = (ProgressBar)view.findViewById(R.id.search_progress_view);
        searchListView = (ListView)view.findViewById(R.id.search_results_list_view);
        searchListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(searchResult == null) {
            loadResults();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LOADER_TYPE_KEY, loaderType.ordinal());

        if(searchResult != null) {
            outState.putSerializable(RESULT_KEY, searchResult);
        }
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public void loadResults(Object param) {
        this.param = param;
        loader.load(param, baseLoaderCallback);
    }

    public void loadResults() {
        loader.load(param, baseLoaderCallback);
    }

    protected void publishResult(SearchResult searchResult) {
        this.searchResult = searchResult;

        if(searchResult != null) {
            adapter.addAll(Arrays.asList(searchResult.getItems()));
        }
    }
}
