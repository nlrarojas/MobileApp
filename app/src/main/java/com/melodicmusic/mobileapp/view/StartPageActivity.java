package com.melodicmusic.mobileapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.melodicmusic.mobileapp.MainActivity;
import com.melodicmusic.mobileapp.controller.CentralBankWebServicesConsumer;
import com.melodicmusic.mobileapp.controller.WebServicesConsumer;
import com.melodicmusic.pruebas.R;
import com.melodicmusic.mobileapp.utility.IConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StartPageActivity extends AppCompatActivity implements View.OnClickListener, IConstants{

    private Button noLoginBtn, loginBtn, createAcount;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        sharedPreferences = getSharedPreferences(LOGIN_SAVED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //editor.putBoolean(ACTIVITY_EXECUTED, false);
        //editor.commit();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(sharedPreferences.getBoolean(ACTIVITY_EXECUTED, true)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            noLoginBtn = (Button) findViewById(R.id.noLoginBtn);
            loginBtn = (Button) findViewById(R.id.loginBtn);
            createAcount = (Button) findViewById(R.id.newAcountBtn);

            noLoginBtn.setOnClickListener(this);
            loginBtn.setOnClickListener(this);
            createAcount.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if ((Button) v == loginBtn) {
            editor.putInt(SELECT_FORM_ENTER, 1);
            editor.commit();
            //Cambiar este intent por el activity de login
        } else if ((Button) v == createAcount){
            //Implementar el intent de registrar cuenta
            editor.putInt(SELECT_FORM_ENTER, 2);
            editor.commit();
        } else if((Button)v == noLoginBtn) {
            editor.putBoolean(ACTIVITY_EXECUTED, true);
            editor.putInt(SELECT_FORM_ENTER, 3);
            editor.commit();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
