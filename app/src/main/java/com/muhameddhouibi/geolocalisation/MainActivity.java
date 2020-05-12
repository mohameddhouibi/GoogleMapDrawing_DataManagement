package com.muhameddhouibi.geolocalisation;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{
    GoogleMap map;
    Button btDraw, btClear ,drawLine,drawPoint;
    int click_nbr ;
    int btn_pressed = 0;
    Polygon polygon = null ;
    Polyline polyline = null ;

    List<LatLng> latLngListPolyGone = new ArrayList<>();
    List<LatLng> latLngListPoints = new ArrayList<>();
    List<LatLng> latLngListLines = new ArrayList<>();
    List<PolygonOptions> polygonOptionsList = new ArrayList<>();


    List<Marker> markerList = new ArrayList<>();

    int red=0,green=0,blue=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btDraw = findViewById(R.id.btndraw);
        btClear = findViewById(R.id.btnclear);
        drawLine = findViewById(R.id.drawLine);
        drawPoint = findViewById(R.id.drawPoint);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        drawLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_pressed = 2;
                if(latLngListLines.size() == 2){
                PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngListLines).clickable(true);
                polyline = map.addPolyline(polylineOptions);
                polyline.setColor(Color.rgb(red,green,blue));
                latLngListLines.clear();
            }else
                {
                    latLngListLines.clear();
                    Toast toast=Toast. makeText(getApplicationContext(),"Select twice on the screen",Toast. LENGTH_SHORT);
                    toast. show();
                }
            }
        });

        btDraw.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               btn_pressed = 1;
              // latLngListPolyGone.clear();
               if (!latLngListPolyGone.isEmpty()){
               PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngListPolyGone).clickable(true);
               polygonOptionsList.add(polygonOptions);
               polygon = map.addPolygon(polygonOptions);
               polygon.setFillColor(Color.rgb(red,green,blue));
               map.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                   @Override
                   public void onPolygonClick(Polygon polygon) {
                       InformationAlertBuilder ();
                   }
               });
               latLngListPolyGone.clear();
           }
               else
                   {
                       Toast toast=Toast. makeText(getApplicationContext(),"Select the point on the map",Toast. LENGTH_SHORT);
                       toast. show();
                   }
           }
       });
       btClear.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (polygon != null) polygon.remove();
               for ( Marker marker : markerList) marker.remove();
               latLngListPolyGone.clear();
               markerList.clear();

           }
       });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap ;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
              //  MarkerOptions markerOptions = new MarkerOptions().position(latLng);
               // Marker marker  = map.addMarker(markerOptions);
                if (btn_pressed == 1 ){
                    latLngListPolyGone.add(latLng);
                } else if  (btn_pressed == 2 ){
                    latLngListLines.add(latLng);
                }else if (btn_pressed == 3 ){
                  //  latLngListPolyGone.add(latLng);
                }
               // markerList.add(marker);
            }
        });
    }
    private void InformationAlertBuilder ()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add information") ;
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        EditText Info1 = new EditText(this);
        Info1.setHint("superficie");
        EditText Info2 = new EditText(this);
        Info2.setHint("Etat : Vente / location ");
        EditText Info3 = new EditText(this);
        Info3.setHint("proprietaire");
        linearLayout.addView(Info1);
        linearLayout.addView(Info2);
        linearLayout.addView(Info3);


        builder.setPositiveButton("Ajouter les informations", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast=Toast. makeText(getApplicationContext(),"Informations enregistrées merci :) ",Toast. LENGTH_SHORT);
                toast. show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setView(linearLayout);
        builder.create().show();
    }

}
