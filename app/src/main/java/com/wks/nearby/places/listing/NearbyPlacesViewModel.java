package com.wks.nearby.places.listing;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wks.nearby.BR;
import com.wks.nearby.R;
import com.wks.nearby.data.places.Place;
import com.wks.nearby.data.places.source.PlacesDataSource;
import com.wks.nearby.data.places.source.PlacesRepository;
import com.wks.nearby.places.listing.adapter.PlaceItemViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.wks.nearby.utils.Preconditions.checkNotNull;

/**
 * Created by waqqassheikh on 14/10/2017.
 */

public class NearbyPlacesViewModel extends BaseObservable implements LocationRetrievedCallback{

    public final ObservableBoolean loadingLocation = new ObservableBoolean();

    public final ObservableList<Place> items = new ObservableArrayList<>();
    public final ObservableBoolean loadingPlaces = new ObservableBoolean();
    public final ObservableField<String> dataLoadingError = new ObservableField<>();

    Context context;
    PlacesRepository placesRepository;
    WeakReference<LocationController> locationController;
    NearbyPlacesNavigator navigator;

    String nextPageToken;

    public NearbyPlacesViewModel(Context context,
                                 PlacesRepository placesRepository){
        this.context = context;
        this.placesRepository = placesRepository;
    }

    //-- Getters & Setters

    public void setLocationController(@NonNull LocationController locationController) {
        checkNotNull(locationController);
        this.locationController = new WeakReference<LocationController>(locationController);
    }

    public void setNavigator(NearbyPlacesNavigator navigator) {
        this.navigator = navigator;
    }

    //-- Lifecycle

    public void onActivityDestroyed(){
        this.navigator = null;
    }

    public void refresh(){
        loadingPlaces.set(false);

        items.clear();
        nextPageToken = null;

        findLocation();
    }

    public void loadMore(){
        if(nextPageToken != null) {
            //token stores parameters of previous request so we dont need to send lat lng again
            loadNearbyPlaces(0, 0);
        }
    }

    //-- Load Nearby Places


    private void findLocation(){
        final LocationController controller = locationController.get();
        if(controller != null){
            controller.determineLocation(this);
            loadingLocation.set(true);
        }
    }

    private void loadNearbyPlaces(double latitude,
                                  double longitude) {
        loadingPlaces.set(true);
        placesRepository.loadNearbyPlaces(
                latitude,
                longitude,
                50000,
                nextPageToken,
                new PlacesDataSource.LoadNearbyPlacesCallback() {
                    @Override
                    public void onNearbyPlacesLoaded(@NonNull List<Place> places,
                                                     @Nullable String token) {
                        loadingPlaces.set(false);

                        items.addAll(places);
                        nextPageToken = token;

                        notifyPropertyChanged(BR.empty);
                    }

                    @Override
                    public void onDataNotAvailable(@Nullable String message) {
                        loadingPlaces.set(false);
                        dataLoadingError.set(message);
                        notifyPropertyChanged(BR.empty);
                    }
                }
        );
    }

    @Bindable
    public boolean isEmpty(){
        return items.isEmpty();
    }

    //-- LocationRetrievedCallback

    @Override
    public void onLocationRetrieved(double latitude, double longitude) {
        loadingLocation.set(false);
        loadNearbyPlaces(latitude,longitude);
    }

    @Override
    public void onLocationUnavailable() {
        loadingLocation.set(false);
        dataLoadingError.set(context.getString(R.string.location_not_available));
    }

    //-- Listing Places

    public int getItemCount(){
        return this.items.size();
    }

    public PlaceItemViewModel viewModelForItem(int index){
        int width = context.getResources().getDimensionPixelSize(R.dimen.place_item_image_width);
        int height = context.getResources().getDimensionPixelSize(R.dimen.place_item_image_height);

        final Place place = items.get(index);
        return new PlaceItemViewModel(place,placesRepository,navigator,width,height);
    }
}
