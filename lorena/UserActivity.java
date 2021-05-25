package com.borjaunizar.demoubicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class UserActivity extends AppCompatActivity implements RoomListener, TextToSpeech.OnInitListener {

    //para el habla de móvil
    private static final int MY_DATA_CHECK_CODE = 1 ;
    private TextToSpeech mTts;

    //para la sala de chat de scaledrone
    private String channelID;
    private String user;
    private String phone;
    private String roomName = "observable-room";
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private final MemberData contacto = new MemberData("contacto", "blue");
    private final MemberData data = new MemberData("usuario", "blue");

    //para el arbol de decisiones
    private arbol tree ;
    private String inicio="null";

    //para la ubicación de usuario
    private String loc="null";
    private FusedLocationProviderClient fusedLocationProviderClient;

    //para el reconocedor de voz
    private final int REQ_CODE = 100;

    //boton para llamar a la policia
    private  FloatingActionButton fab;

    private SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private Handler handler;
    Runnable t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        preferences = this.getSharedPreferences("Shared", Context.MODE_PRIVATE);
        editor = preferences.edit();

        channelID = this.preferences.getString("ch","0");
        user = this.preferences.getString("user","user");
        phone = this.preferences.getString("phone","0");

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

        //para la sala de chat de scaledrone
        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        scaledrone = new Scaledrone(channelID, data);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone connection open");
                // Since the MainActivity itself already implement RoomListener we can pass it as a target
                scaledrone.subscribe(roomName, UserActivity.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);
            }
        });

        //para el arbol de decisiones + primera pregunta del arbol
        tree=new  arbol();
        inicio=tree.play("inicio");

        handler = new Handler();
        t = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "timeout", Toast.LENGTH_SHORT).show();
                Intent i =new Intent(Intent.ACTION_CALL, Uri.parse("tel:+34638725048"));
                if(ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "llamar policia", Toast.LENGTH_SHORT).show();
                }
                else {
                    ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 44);
                }
            }
        };
        handler.postDelayed(t, 10000);
        final Message message_ini = new Message(inicio, contacto, false);

        messageAdapter.add(message_ini);
        messagesView.setSelection(messagesView.getCount() - 1);

        //para la ubicación de usuario
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ubi();

        //para el reconocedor de voz
        ImageView speak = findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Te estoy escuchando :)");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //boton para llamar a la policia
        fab = findViewById(R.id.llamada);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Intent.ACTION_CALL, Uri.parse("tel:+34638725048"/*+MemoriaCompartida.getPhone()*/));
                if(ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "llamar policia", Toast.LENGTH_SHORT).show();
                }
                else {
                    ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 44);
                }
            }
        });
    }


    //envia la ubicacion
    public void sendUbi(String message ) {

        if (message.length() > 0 && !message.equals("null")) {
            scaledrone.publish("observable-room", message);

        }
    }

    //envia el mensaje de texto
    public void sendMsn(String message ) {

        if (message.length() > 0 && !message.equals("null")) {
            scaledrone.publish("observable-room", message);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //para el habla de móvil -> inicio/instalacion
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
        //para el reconocedor de voz
        if (requestCode ==REQ_CODE) {
            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sendMsn(result.get(0));

            }
        }


    }

    // Successfully connected to Scaledrone room
    @Override
    public void onOpen(Room room) {
        System.out.println("Conneted to room");

    }

    // Connecting to Scaledrone room failed
    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.err.println(ex);
    }


    //para la recepcion de un mensaje en la sala del chat
    @Override
    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
        final ObjectMapper mapper = new ObjectMapper();

        boolean belongsToCurrentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
        final Message message = new Message(receivedMessage.getData().asText(), data, belongsToCurrentUser);

        final Message message_send = new Message(receivedMessage.getData().asText(), data, belongsToCurrentUser);
        String pregunta = tree.play(message.getText()); //determinar la respuesta del arbol segun lo recibido

        if (receivedMessage.getData().asText().equals("si")) {
            handler.removeCallbacks(t);
            //handler = new Handler();
            t = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "timeout", Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(Intent.ACTION_CALL, Uri.parse("tel:+34638725048"));
                    if(ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "llamar policia", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 44);
                    }
                }
            };
            handler.postDelayed(t, 10000);
        }
        else if (receivedMessage.getData().asText().equals("no")) {
            handler.removeCallbacks(t);
            //handler = new Handler();
            t = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "timeout", Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(Intent.ACTION_CALL, Uri.parse("tel:+34638725048"));
                    if(ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "llamar policia", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 44);
                    }
                }
            };
            handler.postDelayed(t, 10000);
        }

        //si se recibe un mensaje que no tenga que ver con la ubicación
        Message message2 = null;
        if(!receivedMessage.getData().asText().contains("Latitud") ){
            if(!receivedMessage.getData().asText().contains("Ubicación no disponible")  ) {
                message2 = new Message(pregunta, contacto, false);
                mTts.speak(pregunta, TextToSpeech.QUEUE_FLUSH, null);// leer por voz el mensaje del arbol de decisiones
                ubi();
            }
        }


        Message finalMessage = message2;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                //mostrar mensajes por pantalla si no son la ubicacion
                if(!receivedMessage.getData().asText().contains("Latitud")) {
                    messageAdapter.add(message_send);
                    messagesView.setSelection(messagesView.getCount() - 1);

                    if( !receivedMessage.getData().asText().contains("Ubicación no disponible")  ) { //si la ubicacion no esta disponible no hay mensaje despues
                        messageAdapter.add(finalMessage);
                        messagesView.setSelection(messagesView.getCount() - 1);
                    }


                }

                //si el arbol decide llamar al contacto
                if(pregunta.equals("llamar contacto")){
                    Intent i =new Intent(Intent.ACTION_CALL, Uri.parse("tel:+34"+MemoriaCompartida.getPhone()));
                    if(ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "llamar contacto", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 44);
                    }
                }
                //si el arbol decide llamar a la policia
                else if (pregunta.equals("llamar policia")){
                    Intent i =new Intent(Intent.ACTION_CALL, Uri.parse("tel:+34638725048"));
                    if(ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "llamar policia", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 44);
                    }
                }

            }

        });
    }

    //para obtener la ubicacion del usuario
    public void ubi() {

        if (ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener((new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(UserActivity.this, Locale.getDefault());

                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            loc="Latitud: " + addresses.get(0).getLatitude()+"\nLongitud: "+ addresses.get(0).getLongitude()+"\nPaís: " + addresses.get(0).getCountryName()+"\nLocalidad: " + addresses.get(0).getLocality()+"\nDirección: " + addresses.get(0).getAddressLine(0);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Ubicación no disponible", Toast.LENGTH_SHORT).show();
                        loc="Ubicación no disponible";
                    }
                }
            }));
        } else {
            ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            loc="que es esto";
        }
        sendUbi(loc);
    }

    //para el habla del chat -> inicio
    @Override
    public void onInit(int status) {
        mTts.setLanguage(new Locale(Locale.getDefault().getLanguage()));
        mTts.speak(inicio, TextToSpeech.QUEUE_FLUSH, null);
        //se lee el primer mensaje del arbol de decisiones

    }
}
