package com.melodicmusic.mobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.melodicmusic.mobileapp.controller.WebServicesConsumer;
import com.melodicmusic.mobileapp.utility.IConstants;
import com.melodicmusic.mobileapp.view.CreateAcountFragment;
import com.melodicmusic.mobileapp.view.LoginFragment;
import com.melodicmusic.mobileapp.view.NoLoginStartPageFragment;
import com.melodicmusic.mobileapp.view.PrincipalPage;
import com.melodicmusic.mobileapp.view.SearchFragment;
import com.melodicmusic.mobileapp.view.StartPageActivity;
import com.melodicmusic.pruebas.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NoLoginStartPageFragment.OnFragmentInteractionListener,
        PrincipalPage.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, CreateAcountFragment.OnFragmentInteractionListener, IConstants {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private WebServicesConsumer web;

    //View's components
    private EditText userName, passwordName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences(LOGIN_SAVED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        web = new WebServicesConsumer();

        if(sharedPreferences.getInt(SELECT_FORM_ENTER, 1) == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new LoginFragment()).commit();
        } else if (sharedPreferences.getInt(SELECT_FORM_ENTER, 2) == 2){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new CreateAcountFragment()).commit();
        } else if (sharedPreferences.getInt(SELECT_FORM_ENTER, 3) == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new NoLoginStartPageFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_log_out){
            if(sharedPreferences.getBoolean(IS_LOGIN, true)){
                editor.putBoolean(IS_LOGIN, false);
                editor.putBoolean(ACTIVITY_EXECUTED, false);
                editor.commit();
                Intent intent = new Intent(this, StartPageActivity.class);
                startActivity(intent);
            }else{
                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.contenedor), R.string.no_log_in, Snackbar.LENGTH_SHORT);
                mySnackbar.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = new NoLoginStartPageFragment();
        if(id == R.id.nav_home){
            if(sharedPreferences.getBoolean(IS_LOGIN, true)){
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new PrincipalPage()).commit();
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new NoLoginStartPageFragment()).commit();
            }
        } else if (id == R.id.nav_search) {
            fragment = new SearchFragment();
        } else if (id == R.id.nav_categories) {

        } else if (id == R.id.nav_shopping_cart){

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_acount) {

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //Funciones de los buttons
    public void searchBtnActivity(View view){
        Fragment fragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
    }

    public void searchBtnAction(View view){
        //Botón buscar implementar
        EditText searchEditText = (EditText) findViewById(R.id.search_edit_text);
        System.out.println(searchEditText.getText());
    }

    public void getAllProducts(View view){

    }

    public void getProductsByCategories(View view){

    }

    public void getProductsByBrand(View view){

    }

    public void loginBtnRedirect(View v){
        //Redireccionar a la página de inicio de sessión
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new LoginFragment()).commit();
    }

    public void createAcountBtnRedirect(View v){
        //Redireccionar a la página de registro
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new CreateAcountFragment()).commit();
    }

    public void loginBtnAction(View view){
        userName = (EditText) findViewById(R.id.etUsername);
        passwordName = (EditText) findViewById(R.id.etPassword);

        web.requestContent(URI_LOGIN, userName.getText() + "/" + passwordName.getText());
    }

    public void createAcountBtnAction(View view){

    }
}
