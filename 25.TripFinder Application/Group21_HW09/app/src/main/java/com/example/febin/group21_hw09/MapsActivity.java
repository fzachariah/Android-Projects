package com.example.febin.group21_hw09;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, FetchData.IData {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener mLocListener;
    Location locationMain;
    LatLngBounds.Builder bounds;
    LatLng startLocation;
    ArrayList<LocationDetails> placesArraylist = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Finding the Shortest Round Trip Route");
        progressDialog.setCancelable(false);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));
        placesArraylist = (ArrayList<LocationDetails>) getIntent().getSerializableExtra("Location");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("Test", "AmDone");
        if (requestCode == 500) {
            Log.d("Test", "AmDone1");

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            mMap.setMyLocationEnabled(true);

            Location location = getLastKnownLocation();
            Log.d("Test123location",""+(location==null));
            if (location != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(15)
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Log.d("Inside", "LOC " + location.getLatitude() + "<>" + location.getLongitude());
                locationMain=location;
                bounds = new LatLngBounds.Builder();
                startLocation=new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(startLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
                bounds.include(startLocation);
                drawMapRoute(location);
            }

        }

        else
        {
            Log.d("222222222222222","reachedActivity");
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Flow1","2");
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        121);
            }
        } else {
            mMap.setMyLocationEnabled(true);

            Location location = getLastKnownLocation();
            if (location != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(15)
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Log.d("Inside", "LOC " + location.getLatitude() + "<>" + location.getLongitude());
                locationMain=location;
                bounds = new LatLngBounds.Builder();
                startLocation=new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(startLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
                bounds.include(startLocation);
                drawMapRoute(location);
            }
        }


    }

    private void drawMapRoute(Location location) {

        progressDialog.show();
        String waypoints = "";
        for (int i=0;i<placesArraylist.size();i++){
            LocationDetails loc = placesArraylist.get(i);
            waypoints = waypoints+loc.getLat()+","+loc.getLongt()+"|";
            LatLng latLng = new LatLng( Double.parseDouble(loc.getLat()), Double.parseDouble(loc.getLongt()));
            mMap.addMarker(new MarkerOptions().position(latLng).title(loc.getLocationName()));
            bounds.include(latLng);



        }
        Log.d("demo","waypoints "+waypoints.toString());
        String origin = location.getLatitude()+","+location.getLongitude();
        String dest = location.getLatitude()+","+location.getLongitude();
        String tripUrl = "https://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+dest+"&waypoints="+waypoints+"&key=AIzaSyCGr2VtmkxJCuB-_pMWIo-GJNR4HT0iPMk";
        Log.d("demo","trip URL = "+tripUrl);
        new FetchData(this).execute(tripUrl);

    }

    private Location getLastKnownLocation() {

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Log.d("Provider",provider);
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {

                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("GPS Not Enabled").setMessage("Would you like to enable GPS Settings?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Log.d("Flow1", "1");
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 500);
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
                        Log.d("demoLocation", "latlong " + location.getLongitude() + " " + location.getLatitude());
                        Log.d("demo", "place " + location.toString());
                        locationMain = location;
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
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 121: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    mMap.setMyLocationEnabled(true);
                    Location location = getLastKnownLocation();
                    if (location != null) {

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                .zoom(15)
                                .bearing(90)                // Sets the orientation of the camera to east
                                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        Log.d("Inside", "LOC " + location.getLatitude() + "<>" + location.getLongitude());
                        locationMain=location;
                        bounds = new LatLngBounds.Builder();
                        startLocation=new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(startLocation).title("Your Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
                        bounds.include(startLocation);
                        drawMapRoute(location);

                    } else {
                        Log.d("Not Inside", "Null");
                    }


                } else {
                    Log.d("demo","not granted (permission)");
                }
                return;
            }

        }
    }


    @Override
    public void setUpData(PolylineOptions data) {

        if(data!=null) {
            mMap.addPolyline(data);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 30));
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Road Trip is Not Possible",Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}

