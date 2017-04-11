package com.example.eds.geolocation;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity implements LocationListener {
    MapView mv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        org.osmdroid.config.Configuration.getInstance().load(this,
                PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);
        LocationManager mgr=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mv = (MapView)findViewById(R.id.map1);
        mv. getController().setCenter(new GeoPoint(51.05,-0.72));
        mv.setBuiltInZoomControls(true);
        mv. getController().setZoom(16);
        try {
            mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);




        } catch (SecurityException e){
            System.out.println("Error" + e);

        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;

    }
    public void updateloc(Location location){
        GeoPoint Location = new GeoPoint
                (location.getLatitude(), location.getLongitude());
        mv.getController().setCenter(Location);
    }


    public void onLocationChanged(Location newloc){
        updateloc(newloc);



    }

    public void onStatusChanged(String s, int status, Bundle  extras){


    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Provider" + provider + "disabled",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this,"Status changed:" + provider, Toast.LENGTH_SHORT).show();
    }
    public void onDestroy(){
        super.onDestroy();

    }


}
