package com.borjaunizar.demoubicacion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText usernameIn, passIn;
    private Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameIn = findViewById(R.id.username);
        passIn = findViewById(R.id.pass);
        login = findViewById(R.id.buttonlog);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username, password;
                username = String.valueOf(usernameIn.getText());
                password = String.valueOf(passIn.getText());


                if(!username.equals("") && !password.equals("")){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;

                            PutData putData = new PutData("http://192.168.86.126/loginregister/login.php","POST", field, data);
                            if(putData.startPut()){
                                if(putData.onComplete()){
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(getApplicationContext(),"Rellena ambos campos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

