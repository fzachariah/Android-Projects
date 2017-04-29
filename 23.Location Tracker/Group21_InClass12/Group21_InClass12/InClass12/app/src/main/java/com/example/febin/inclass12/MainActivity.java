package com.example.febin.inclass12;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener mLocListener;

    Location locationMain;

    LatLngBounds.Builder bounds;


    boolean check=false;

    Location startLocation;

    Location currentLocation;
    Location previousLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Tracking App");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);
        SupportMapFragment.newInstance(options);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnMapLongClickListener(this);

        mMap.setMyLocationEnabled(true);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if(location!=null)
        {
            Log.d("Inside","Null");
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(14)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else
        {
            Log.d("Not Inside","Null");
        }

    }


    @Override
    protected void onResume() {
        super.onResume();


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS Not Enabled").setMessage("Would you like to enable GPS Settings?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            mLocListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    Log.d("demo", location.getLatitude() + " " + location.getLongitude());
                    previousLocation=currentLocation;
                    locationMain=location;
                    currentLocation=location;

                    if(check) {
                        LatLng latLngPrev=new LatLng(previousLocation.getLatitude(),previousLocation.getLongitude());
                        LatLng latLngCurr=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                        Polyline polyline = mMap.addPolyline(new PolylineOptions().add(latLngPrev, latLngCurr));
                        polyline.setColor(Color.BLUE);
                        polyline.setWidth(8);
                        bounds.include(latLngCurr);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 200, mLocListener);

        }


    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        if(!check) {
            Toast.makeText(getApplicationContext(), "Start Location Tracking", Toast.LENGTH_LONG).show();
            LatLng sydney = new LatLng(locationMain.getLatitude(), locationMain.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Start Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            startLocation=locationMain;
            bounds = new LatLngBounds.Builder();
            bounds.include(sydney);
            check=true;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Stop Location Tracking", Toast.LENGTH_LONG).show();
            LatLng sydney = new LatLng(locationMain.getLatitude(), locationMain.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("End Location"));
            /*mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
            bounds.include(sydney);
            check=false;
        }


    }
}
