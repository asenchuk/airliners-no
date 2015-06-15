package net.taviscaron.airliners.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.data.BaseLoader;
import net.taviscaron.airliners.data.ImageLoader;
import net.taviscaron.airliners.data.SearchLoader;
import net.taviscaron.airliners.model.AircraftSearchResult;
import net.taviscaron.airliners.model.SearchParams;
import net.taviscaron.airliners.model.SearchResult;
import net.taviscaron.airliners.views.AspectConstantImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SearchResults table adapter
 * @author Andrei Senchuk
 */
public class SearchResultsAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private static final String LOADER_TYPE_KEY = "loaderType";
    private static final String SEARCH_PARAMS_KEY = "searchParams";
    private static final String RESULTS_KEY = "results";
    private static final String HAS_NEXT_PAGES_KEY = "hasNextPages";
    private static final String LAST_LOADED_PAGE = "lastLoadedPage";

    private Context context;
    private LoaderType loaderType;
    private SearchParams params;
    private ImageLoader imageLoader;
    private SearchLoader searchLoader;
    private List<AircraftSearchResult> results;
    private SearchResultsAdapterListener listener;
    private int lastLoadedPage;
    private boolean hasNext;
    private boolean isLoading;

    public enum LoaderType {
        TOP15, TOP, SEARCH
    }

    public interface SearchResultsAdapterListener {
        public void searchResultItemThumbClicked(AircraftSearchResult result, int position);
        public void loadStarted();
        public void loadFinished(boolean success);
    }

    private SearchLoader.BaseLoaderCallback<SearchParams, SearchResult> searchLoaderListener = new SearchLoader.BaseLoaderCallback<SearchParams, SearchResult>() {
        public void loadStarted(BaseLoader<SearchParams, SearchResult> loader) {
            listener.loadStarted();
        }

        public void loadFinished(BaseLoader<SearchParams, SearchResult> loader, SearchResult obj) {
            isLoading = false;

            if(obj != null) {
                lastLoadedPage = params.getPage();
                hasNext = obj.getTo() < obj.getTotal();

                AircraftSearchResult[] items = obj.getItems();
                if(items != null && items.length > 0) {
                    results.addAll(Arrays.asList(items));
                    notifyDataSetChanged();
                }
            }

            listener.loadFinished(obj != null);
        }
    };

    public SearchResultsAdapter(Context context, SearchResultsAdapterListener listener, LoaderType loaderType, SearchParams params, int itemsPerPage) {
        this.context = context.getApplicationContext();
        this.listener = listener;

        this.loaderType = loaderType;
        this.params = params;
        this.results = new ArrayList<AircraftSearchResult>();

        this.lastLoadedPage = 1;
        this.params.setLimit(itemsPerPage);

        initLoaders();
    }

    public SearchResultsAdapter(Context context, SearchResultsAdapterListener listener, Bundle savedInstanceState) {
        this.context = context.getApplicationContext();
        this.listener = listener;

        this.loaderType = LoaderType.values()[savedInstanceState.getInt(LOADER_TYPE_KEY)];
        this.params = (SearchParams)savedInstanceState.getSerializable(SEARCH_PARAMS_KEY);
        this.results = new ArrayList<AircraftSearchResult>(Arrays.asList((AircraftSearchResult[])savedInstanceState.getSerializable(RESULTS_KEY)));

        this.lastLoadedPage = savedInstanceState.getInt(LAST_LOADED_PAGE);
        this.hasNext = savedInstanceState.getBoolean(HAS_NEXT_PAGES_KEY);

        initLoaders();
    }

    private void initLoaders() {
        this.imageLoader = new ImageLoader(context, ImageLoader.THUMB_CACHE_TAG);

        switch(loaderType) {
            case TOP:
                searchLoader = SearchLoader.createTopLoader(context);
                break;
            case TOP15:
                searchLoader = SearchLoader.createTop15Loader(context);
                break;
            case SEARCH:
                searchLoader = SearchLoader.createSearchLoader(context);
                break;
            default:
                throw new RuntimeException("Bad loader type");
        }
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public AircraftSearchResult getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.aircraft_search_result_item, parent, false);
        }

        final ViewHolder holder = ViewHolder.viewHolderOf(view);
        final int finalPosition = position;
        final AircraftSearchResult result = getItem(position);

        holder.imageLoadingProgressBar.setVisibility(View.GONE);

        // attributes
        updateTextViewValue(holder.aircraftLabel, result.getAircraft());
        updateTextViewValue(holder.airlineLabel, result.getAirline());

        // thumb
        holder.imageView.setOnClickListener(null);
        holder.imageView.setImageResource(android.R.color.transparent);
        imageLoader.loadImage(result.getThumbUrl(), new ImageLoader.ImageLoaderCallback() {
            public void imageLoaded(ImageLoader loader, String url, Bitmap bitmap, String imageCachePath) {
                holder.imageLoadingProgressBar.setVisibility(View.GONE);
                holder.imageView.setImageBitmap(bitmap);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.searchResultItemThumbClicked(result, finalPosition);
                    }
                });
            }

            public void imageLoadFailed(ImageLoader loader, String url) {
                holder.imageLoadingProgressBar.setVisibility(View.GONE);
            }

            public void imageLoadStarted(ImageLoader loader, String url) {
                holder.imageLoadingProgressBar.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // noop
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(hasNext && !isLoading && totalItemCount - firstVisibleItem - visibleItemCount < Math.max(visibleItemCount, params.getLimit() / 2)) {
            params.setPage(lastLoadedPage + 1);
            loadResults();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        AircraftSearchResult[] resultsArray = new AircraftSearchResult[results.size()];
        bundle.putSerializable(RESULTS_KEY, results.toArray(resultsArray));

        bundle.putSerializable(SEARCH_PARAMS_KEY, params);
        bundle.putInt(LOADER_TYPE_KEY, loaderType.ordinal());
        bundle.putBoolean(HAS_NEXT_PAGES_KEY, hasNext);
        bundle.putInt(LAST_LOADED_PAGE, lastLoadedPage);
    }

    public void performInitialLoadIfNeeded() {
        if(results.isEmpty()) {
            loadResults();
        }
    }

    private void loadResults() {
        if(!isLoading) {
            isLoading = true;
            searchLoader.load(params, searchLoaderListener);
        }
    }

    private void updateTextViewValue(TextView textView, String value) {
        if(value == null || value.isEmpty()) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(value);
        }
    }

    public LoaderType getLoaderType() {
        return loaderType;
    }

    private static class ViewHolder {
        final ProgressBar imageLoadingProgressBar;
        final AspectConstantImageView imageView;
        final TextView aircraftLabel;
        final TextView airlineLabel;

        private ViewHolder(View view) {
            imageLoadingProgressBar = (ProgressBar)view.findViewById(R.id.aircraft_search_result_item_image_progress);
            imageView = (AspectConstantImageView)view.findViewById(R.id.aircraft_search_result_item_image);
            aircraftLabel = (TextView)view.findViewById(R.id.aircraft_search_result_item_aircraft);
            airlineLabel = (TextView)view.findViewById(R.id.aircraft_search_result_item_airline);
        }

        public static ViewHolder viewHolderOf(View view) {
            ViewHolder holder = (ViewHolder)view.getTag();
            if(holder == null) {
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            return holder;
        }
    }
}
