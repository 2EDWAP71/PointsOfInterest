package com.example.eds.geolocation;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this,
                PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);
        mv = (MapView) findViewById(R.id.map1);
        mv.getController().setCenter(new GeoPoint(50.90, -1.40));
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        Button load = (Button) findViewById(R.id.load);
        load.setOnClickListener(this);

        markerGestureListener=new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()

        {
            public boolean onItemLongPress ( int i, OverlayItem item){
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

        };

        items = new ItemizedIconOverlay<>(this, new
                ArrayList<OverlayItem>(), markerGestureListener);


    }

    public void onClick (View view) {


        if (view.getId() == R.id.save)
        {

            try {
                BufferedWriter pw = new BufferedWriter(new FileWriter(Environment
                        .getExternalStorageDirectory().getAbsolutePath() + "/file1.txt"));
                for (int i = 0; i < items.size(); i++) {
                    OverlayItem item = items.getItem(i);
                    pw.write(item.getTitle() + "\n," + item.getSnippet() + "," + item.getPoint() + "");
                }
                pw.close();

            } catch (IOException e) {
                System.out.println("I/O Error" + e);
            }
            Toast.makeText(MainActivity.this,"Marker(s) Saved to file",
                Toast.LENGTH_SHORT).show();

        }

        else if (view.getId() == R.id.load){
            try{
                BufferedReader reader = new BufferedReader(new FileReader("/file.txt"));
                String line;

                while ((line = reader.readLine()) !=null){
                    String[] components = line.split(",");
                    if (components.length==4){
                        OverlayItem Item = new OverlayItem(components[0],  components[1],
                                new GeoPoint(Double.parseDouble(components[2]),
                                Double.parseDouble(components[3])));
                                items.addItem(Item);
                                mv.getOverlays().add(items);


                    }
                }

            }
            catch (IOException e){
            new AlertDialog.Builder(this).setMessage("I/O Error" + e);
            }
            Toast.makeText(MainActivity.this,"Marker(s) successfully loaded from file",
                    Toast.LENGTH_SHORT).show();
        }

    }






    public void onResume() {
        super.onResume();


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                    (getApplicationContext());
            //HttpURLConnection conn = null;


            String file = preferences.getString("File", "none");

            if (file.equals("Save")) {
                Toast.makeText(MainActivity.this,"Working(Saved)",
                        Toast.LENGTH_SHORT).show();

            }
            if (file.equals("NoSave")) {
                Toast.makeText(MainActivity.this,"Working(No save)",
                        Toast.LENGTH_SHORT).show();
            }


}

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == R.id.addaninterest){
                Intent intent = new Intent(this,AddInterest.class);
                startActivityForResult(intent, 1);
                return true;
        }
        if (item.getItemId() == R.id.prefs){
           Intent intent = new Intent(this,MyPrefsActivity.class);
           startActivityForResult(intent, 2);
            return true;
        }

        return false;

    }







    public void onActivityResult (int requestCode, int resultCode,Intent intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {


                Bundle extras = intent.getExtras();


                String name = extras.getString("com.example.tx1");
                String type = extras.getString("com.example.tx2");
                String des = extras.getString("com.example.tx3");




                double latitude =  mv.getMapCenter().getLatitude();
                double longitude = mv.getMapCenter().getLongitude();


                    OverlayItem Item = new OverlayItem(name, des,
                            new GeoPoint(latitude, longitude));


                    items.addItem(Item);
                    mv.getOverlays().add(items);





                }

            }

        }


    public void onDestroy(){
        super.onDestroy();
            try {
                BufferedWriter pw = new BufferedWriter(new FileWriter(Environment
                        .getExternalStorageDirectory().getAbsolutePath() + "/file1.txt", true));
                for (int i = 0; i < items.size(); i++) {
                       OverlayItem Item = items.getItem(i);
                    pw.write(Item.getTitle() + "\n"  + Item.getPoint() + "," + Item.getSnippet() + "");
                }
                pw.close();
            } catch (IOException e) {
                new AlertDialog.Builder(this).setMessage("ERROR" + e);
            }

        finish();
    }


}
