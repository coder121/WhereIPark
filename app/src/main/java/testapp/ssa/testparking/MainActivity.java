package testapp.ssa.testparking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;

public class MainActivity extends AppCompatActivity {
    private TextView latitude;
    private TextView longitude;
    private TextView testing;
    private LocationManager locationManager;
    private double lat=0;
    private double longi=0;
    Button showButton;
    Button mapButton;
    private Bundle bundle;
    String provider;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
      /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
       provider= locationManager.getBestProvider(criteria, true);*/
        bundle = new Bundle();
        latitude = (TextView) findViewById(R.id.showLat);
        longitude = (TextView) findViewById(R.id.showLong);
        showButton = (Button) findViewById(R.id.okButton);
        mapButton=(Button) findViewById(R.id.mapButton);
        testing=(TextView) findViewById(R.id.testView);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider= locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 1000, 1, new MyLocationListener());
        final Context context  = this;
        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent( context, MapActivity.class);
                intent.putExtras(bundle);
                if(lat!=0&&longi!=0){
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"No parking location Found!",
                            Toast.LENGTH_SHORT).show();
                }

            }

        });



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
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new MyLocationListener());
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
//                testing.setText("Button Clicked:"+count);
                showCurrentLocation();

            }
        });
    }

    private void showCurrentLocation() {
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


        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            lat=location.getLatitude();
            longi=location.getLongitude();
            bundle.putDouble("lat",lat);
            bundle.putDouble("longi", longi);
            testing.setText("Location Accuracy:"+location.getAccuracy());
            latitude.setText("Lat:"+String.valueOf(location.getLatitude()));
            longitude.setText("Long: "+String.valueOf(location.getLongitude()));


        }
        else{
            Toast.makeText(MainActivity.this, "Location is off. Turn the location on!",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

        }
    }
 private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // Initialize the location fields
            lat=location.getLatitude();
            longi=location.getLongitude();
//            latitude.setText("Lat:"+String.valueOf(location.getLatitude()));
//            longitude.setText("Long: "+String.valueOf(location.getLongitude()));



        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MainActivity.this, provider + "'s status changed to "+status +"!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
