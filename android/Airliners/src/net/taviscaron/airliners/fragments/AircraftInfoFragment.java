package net.taviscaron.airliners.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.data.AircraftPhotoLoader;
import net.taviscaron.airliners.data.BaseLoader;
import net.taviscaron.airliners.data.ImageLoader;
import net.taviscaron.airliners.model.AircraftPhoto;

/**
 * Aircraft Info Fragment
 * @author Andrei Senchuk
 */
public class AircraftInfoFragment extends Fragment {
    public static final String SAVED_AIRCRAFT_KEY = "savedAircraft";

    private AircraftPhoto aircraftPhoto;
    private AircraftPhotoLoader photoLoader;
    private ImageLoader imageLoader;

    private final ImageLoader.ImageLoaderCallback imageLoaderCallback = new ImageLoader.ImageLoaderCallback() {
        @Override
        public void imageLoaded(ImageLoader loader, String url, Bitmap bitmap) {
            View view = getView();
            if(view != null) {
                ImageView imageView = (ImageView)view.findViewById(R.id.aircraft_info_photo_view);
                imageView.setVisibility(View.VISIBLE);
                view.findViewById(R.id.aircraft_info_photo_progress_bar).setVisibility(View.GONE);
                imageView.setImageBitmap(bitmap);
                updateImageViewSize();
            }
        }

        @Override
        public void imageLoadFailed(ImageLoader loader, String url) {
            View view = getView();
            if(view != null) {
                view.findViewById(R.id.aircraft_info_photo_progress_bar).setVisibility(View.GONE);
                showImageLoadingError();
            }
        }

        @Override
        public void imageLoadFromNetworkStarted(ImageLoader loader, String url) {
            View view = getView();
            if(view != null) {
                view.findViewById(R.id.aircraft_info_photo_view).setVisibility(View.GONE);
                view.findViewById(R.id.aircraft_info_photo_progress_bar).setVisibility(View.VISIBLE);
            }
        }
    };

    private final AircraftPhotoLoader.BaseLoaderCallback<AircraftPhoto> aircraftPhotoLoader = new AircraftPhotoLoader.BaseLoaderCallback<AircraftPhoto>() {
        @Override
        public void loadStarted(BaseLoader<AircraftPhoto> loader) {
            View view = getView();
            if(view != null) {
                view.findViewById(R.id.aircraft_info_progress_bar).setVisibility(View.VISIBLE);
                view.findViewById(R.id.aircraft_info_layout).setVisibility(View.GONE);
            }
        }

        @Override
        public void loadFinished(BaseLoader<AircraftPhoto> loader, AircraftPhoto obj) {
            View view = getView();
            if(view != null) {
                view.findViewById(R.id.aircraft_info_progress_bar).setVisibility(View.GONE);
                if(obj != null) {
                    aircraftPhoto = obj;
                    updateView();
                } else {
                    showLoadingError();
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            aircraftPhoto = (AircraftPhoto)savedInstanceState.getSerializable(SAVED_AIRCRAFT_KEY);
        }

        photoLoader = new AircraftPhotoLoader(getActivity());
        imageLoader = new ImageLoader(getActivity(), ImageLoader.IMAGE_CACHE_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aircraft_info, container, false);
        view.findViewById(R.id.aircraft_info_layout).setVisibility(View.GONE);
        view.findViewById(R.id.aircraft_info_progress_bar).setVisibility(View.GONE);
        view.findViewById(R.id.aircraft_info_photo_progress_bar).setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(aircraftPhoto != null) {
            outState.putSerializable(SAVED_AIRCRAFT_KEY, aircraftPhoto);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(aircraftPhoto != null) {
            updateView();
        }
    }

    public void loadAircraftInfo(String id) {
        photoLoader.load(id, aircraftPhotoLoader);
    }

    protected void updateImageViewSize() {
        ImageView imageView = (ImageView)getView().findViewById(R.id.aircraft_info_photo_view);
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.height = (int)((float)imageView.getDrawable().getIntrinsicHeight() / imageView.getDrawable().getIntrinsicWidth() * imageView.getWidth());
        imageView.setLayoutParams(lp);
    }

    protected void updateView() {
        View view = getView();
        view.findViewById(R.id.aircraft_info_layout).setVisibility(View.VISIBLE);

        // thumb
        imageLoader.loadImage(aircraftPhoto.getImageUrl(), imageLoaderCallback);

        // airline
        TextView airlineText = (TextView)view.findViewById(R.id.aircraft_info_airline);
        airlineText.setText(aircraftPhoto.getAirline());

        // aircraft
        TextView aircraftText = (TextView)view.findViewById(R.id.aircraft_info_aircraft);
        aircraftText.setText(aircraftPhoto.getAircraft());

        // taken at
        TextView takenAtText = (TextView)view.findViewById(R.id.aircraft_info_taken_at);
        takenAtText.setText(aircraftPhoto.getTakenAt());

        // taken on
        TextView takenOnText = (TextView)view.findViewById(R.id.aircraft_info_taken_on);
        takenOnText.setText(aircraftPhoto.getTakenOn());

        // registration
        TextView regText = (TextView)view.findViewById(R.id.aircraft_info_registration);
        regText.setText(aircraftPhoto.fullReg());

        // author
        TextView authorText = (TextView)view.findViewById(R.id.aircraft_info_author);
        authorText.setText(aircraftPhoto.getAuthor());

        // remark
        TextView remarkTitle = (TextView)view.findViewById(R.id.aircraft_info_remark_title);
        TextView remarkText = (TextView)view.findViewById(R.id.aircraft_info_remark);
        if(aircraftPhoto.getRemark() != null) {
            remarkText.setText(aircraftPhoto.getRemark());
            remarkText.setVisibility(View.VISIBLE);
            remarkTitle.setVisibility(View.VISIBLE);
        } else {
            remarkText.setVisibility(View.GONE);
            remarkTitle.setVisibility(View.GONE);
        }
    }

    protected void showLoadingError() {
        // TODO: impl.
    }

    protected void showImageLoadingError() {
        // TODO: impl.
    }
}
