package com.wks.nearby.data.places.source;

import android.support.annotation.NonNull;

import com.wks.nearby.dependencies.AppScope;

import static com.wks.nearby.utils.Preconditions.checkNotNull;

/**
 * Created by waqqassheikh on 13/10/2017.
 */
@AppScope
public class PlacesRepository implements PlacesDataSource {

    private final PlacesRemoteDataSource remoteDataSource;

    public PlacesRepository(@NonNull PlacesRemoteDataSource remoteDataSource){
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public void loadNearbyPlaces(double latitude,
                                 double longitude,
                                 double radius,
                                 @NonNull LoadNearbyPlacesCallback callback) {
        checkNotNull(callback);
        this.remoteDataSource.loadNearbyPlaces(latitude,longitude,radius,callback);
    }

    @Override
    public void loadPlaceDetails(@NonNull String placeId,
                                 @NonNull LoadPlaceDetailsCallback callback) {
        checkNotNull(placeId);
        checkNotNull(callback);
        this.remoteDataSource.loadPlaceDetails(placeId,callback);
    }
}
