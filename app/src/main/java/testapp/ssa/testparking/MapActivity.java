package testapp.ssa.testparking;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;


public class MapActivity extends FragmentActivity {
    Drawable drawable;
    Document document;
    GMapV2GetRouteDirection v2GetRouteDirection;
    GoogleMap mGoogleMap;
    MarkerOptions markerOptions;

    private double lat;
    private double longi;
    LatLng fromPosition;
    LatLng toPosition;
    LocationManager locationManager;
    private LatLng currentLoc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        v2GetRouteDirection = new GMapV2GetRouteDirection();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mGoogleMap = supportMapFragment.getMap();

        // Enabling MyLocation in Google Map
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        markerOptions = new MarkerOptions();
//        fromPosition = new LatLng(11.663837, 78.147297);
//        toPosition = new LatLng(11.723512, 78.466287);

        fromPosition = new LatLng(11.663837, 78.147297);
        toPosition = new LatLng(11.723512, 78.466287);

      locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(provider);
        currentLoc = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//        currentLoc=new LatLng(37.3353,-121.8813);//debugges sjsu location
        Bundle bundle = getIntent().getExtras();
        lat = bundle.getDouble("lat");
        longi = bundle.getDouble("longi");
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
        GetRouteTask getRoute = new GetRouteTask();
        getRoute.execute();
//        mapFragment.getMapAsync(this);
    }

   /* @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        map.setMyLocationEnabled(true);
//


        LatLng carPark = new LatLng(lat, longi);
//        LatLng carPark = new LatLng(37.22, -121.80);
        map.addMarker(new MarkerOptions().position(carPark).title("Parking"));
//        map.addMarker(currentLocationMarker);

        map.addMarker(new MarkerOptions().position(currentLoc).title("MyLocation"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(carPark, (float) 16.0));
        Polyline line = map.addPolyline(new PolylineOptions()
                .add(carPark, currentLoc)
                .width(5)
                .color(Color.RED));

    }*/
   private class GetRouteTask extends AsyncTask<String, Void, String> {
       LatLng carPark;
       private ProgressDialog Dialog;
       String response = "";
       @Override
       protected void onPreExecute() {
           Dialog = new ProgressDialog(MapActivity.this);
           Dialog.setMessage("Loading route...");
           Dialog.show();
       }

       @Override
       protected String doInBackground(String... urls) {
           //Get All Route values
           carPark = new LatLng(lat, longi);
           document = v2GetRouteDirection.getDocument(carPark, currentLoc, GMapV2GetRouteDirection.MODE_WALKING);
           response = "Success";
           return response;

       }

       @Override
       protected void onPostExecute(String result) {
           mGoogleMap.clear();
           if(response.equalsIgnoreCase("Success")){
               ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
               PolylineOptions rectLine = new PolylineOptions().width(10).color(
                       Color.RED);

               for (int i = 0; i < directionPoint.size(); i++) {
                   rectLine.add(directionPoint.get(i));
               }
               // Adding route on the map
               mGoogleMap.addPolyline(rectLine);
               markerOptions.position(currentLoc).title("MyLocation");
//               markerOptions.position(currentLoc);
               markerOptions.draggable(true);
               mGoogleMap.addMarker(markerOptions);
               mGoogleMap.addMarker(new MarkerOptions().position(carPark).title("Car"));
           }

           Dialog.dismiss();
       }
   }
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
