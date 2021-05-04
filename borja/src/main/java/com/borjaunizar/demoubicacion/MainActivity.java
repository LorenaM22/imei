package com.borjaunizar.demoubicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btn_ubi,btn_map;
    TextView tv1, tv2, tv3, tv4, tv5;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_ubi = findViewById(R.id.btn_ubi);
        btn_map = findViewById(R.id.btn_map);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btn_ubi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener((new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {

                            Location location = task.getResult();
                            if (location != null){
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                                    tv1.setText("Latitud: " + addresses.get(0).getLatitude());
                                    tv2.setText("Longitud: " + addresses.get(0).getLongitude());
                                    tv3.setText("País: " + addresses.get(0).getCountryName());
                                    tv4.setText("Localidad: " + addresses.get(0).getLocality());
                                    tv5.setText("Dirección: " + addresses.get(0).getAddressLine(0));


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Ubicación no disponible",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }));
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MapActivity.class));
            }
        });
    }


}