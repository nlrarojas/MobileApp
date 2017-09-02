package com.melodicmusic.mobileapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.melodicmusic.mobileapp.MainActivity;
import com.melodicmusic.pruebas.R;
import com.melodicmusic.mobileapp.utility.IConstants;

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
        if((Button)v == noLoginBtn) {
            editor.putBoolean(ACTIVITY_EXECUTED, true);
            editor.putBoolean(IS_LOGIN, false);
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if ((Button) v == loginBtn) {
            editor.putBoolean(ACTIVITY_EXECUTED, true);
            editor.putBoolean(IS_LOGIN, true);
            editor.commit();

            //Cambiar este intent por el activity de login
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if ((Button) v == createAcount){
            //Implementar el intent de registrar cuenta
        }
    }
}
