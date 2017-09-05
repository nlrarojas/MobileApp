package com.melodicmusic.mobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.melodicmusic.mobileapp.controller.AdapterListView;
import com.melodicmusic.mobileapp.controller.CentralBankWebServicesConsumer;
import com.melodicmusic.mobileapp.controller.PostRequestWebServicesConsumer;
import com.melodicmusic.mobileapp.controller.UserUpdater;
import com.melodicmusic.mobileapp.controller.WebServicesConsumer;
import com.melodicmusic.mobileapp.model.Product;
import com.melodicmusic.mobileapp.model.User;
import com.melodicmusic.mobileapp.utility.IConstants;
import com.melodicmusic.mobileapp.view.AllProductsFragment;
import com.melodicmusic.mobileapp.view.CatalogueFragment;
import com.melodicmusic.mobileapp.view.CategoriesFragment;
import com.melodicmusic.mobileapp.view.CreateAcountFragment;
import com.melodicmusic.mobileapp.view.LoginFragment;
import com.melodicmusic.mobileapp.view.NoLoginStartPageFragment;
import com.melodicmusic.mobileapp.view.PrincipalPage;
import com.melodicmusic.mobileapp.view.ProductDetailFragment;
import com.melodicmusic.mobileapp.view.SearchByPriceFragment;
import com.melodicmusic.mobileapp.view.SearchFragment;
import com.melodicmusic.mobileapp.view.StartPageActivity;
import com.melodicmusic.mobileapp.view.UpdateAcountFragment;
import com.melodicmusic.pruebas.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NoLoginStartPageFragment.OnFragmentInteractionListener,
        PrincipalPage.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, CreateAcountFragment.OnFragmentInteractionListener,
        CatalogueFragment.OnFragmentInteractionListener, CategoriesFragment.OnFragmentInteractionListener,
        SearchByPriceFragment.OnFragmentInteractionListener, AllProductsFragment.OnFragmentInteractionListener,
        UpdateAcountFragment.OnFragmentInteractionListener, ProductDetailFragment.OnFragmentInteractionListener, IConstants, Observer {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private WebServicesConsumer web;
    private double dolarExchangeColon;
    private String userID;
    private Product currentProduct;
    private boolean doubleBackToExitPressedOnce;
    private Snackbar mySnackbar;
    private List<Product> products;
    private List<Product> shoppingCart;

    //View's components
    private EditText userName, passwordName, userLastName, userEmail, cardNumber;
    private ListView productList;

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

        web = new WebServicesConsumer(this);
        this.doubleBackToExitPressedOnce = false;
        this.shoppingCart = new LinkedList<>();

        Calendar calendar = Calendar.getInstance();
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
        CentralBankWebServicesConsumer consumer = new CentralBankWebServicesConsumer(date, date);
        consumer.addObserver(this);
        consumer.run();

        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

        if (sharedPreferences.contains(EMAIL)) {
            Map<String, ?> map = sharedPreferences.getAll();
            String userEmail = map.get(EMAIL) + "";
            String userPassword = map.get(PASSWORD) + "";

            User user = web.validateIfUserExists(URI_LOGIN, userEmail + "/" + userPassword);
            if (user != null) {
                editor.putString(USER_ID, user.getId());
                editor.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new PrincipalPage()).commit();
            } else {
                editor.putBoolean(ACTIVITY_EXECUTED, false);
                editor.putBoolean(IS_LOGIN, false);
                editor.remove(PASSWORD);
                editor.remove(EMAIL);
                editor.remove(ROLE);
                editor.commit();
                Intent intent = new Intent(this, StartPageActivity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }else{
                    finish();
                }
                showMessage(R.string.error_login);
            }
        } else {
            if (sharedPreferences.getBoolean(IS_LOGIN, true)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new PrincipalPage()).commit();
            } else {
                if (sharedPreferences.getInt(SELECT_FORM_ENTER, 1) == 1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new LoginFragment()).commit();
                } else if (sharedPreferences.getInt(SELECT_FORM_ENTER, 2) == 2) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new CreateAcountFragment()).commit();
                } else if (sharedPreferences.getInt(SELECT_FORM_ENTER, 3) == 3) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new NoLoginStartPageFragment()).commit();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            hideKeyBoard();

            this.doubleBackToExitPressedOnce = true;
            showMessage(R.string.exit_message);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
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

        hideKeyBoard();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            updateAccount();
        } else if (id == R.id.action_log_out) {
            if (sharedPreferences.getBoolean(IS_LOGIN, true)) {
                editor.putBoolean(IS_LOGIN, false);
                editor.putBoolean(ACTIVITY_EXECUTED, false);
                editor.remove(NAME);
                editor.remove(EMAIL);
                editor.remove(ROLE);
                editor.commit();
                Intent intent = new Intent(this, StartPageActivity.class);
                startActivity(intent);
                finish();
            } else {
                showMessage(R.string.no_log_in);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        hideKeyBoard();

        Fragment fragment = null;
        if (id == R.id.nav_home) {
            if (sharedPreferences.getBoolean(IS_LOGIN, true)) {
                fragment = new PrincipalPage();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
            } else {
                fragment = new NoLoginStartPageFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
            }
        } else if (id == R.id.nav_search) {
            fragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
        } else if (id == R.id.nav_catalogue) {
            fragment = new CatalogueFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
        } else if (id == R.id.nav_shopping_cart) {

        } else if (id == R.id.nav_acount) {
            updateAccount();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //Funciones de los buttons
    public void searchBtnActivity(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new SearchFragment()).commit();
    }

    public void searchBtnAction(View view) {
        //Botón buscar implementar
        EditText searchEditText = (EditText) findViewById(R.id.search_edit_text);
        products = web.getProductsByName(searchEditText.getText().toString());
        productList = (ListView) findViewById(R.id.lisByName);

        final AdapterListView adapterProducts = new AdapterListView(products, this, dolarExchangeColon);
        if(adapterProducts == null){
            showMessage(R.string.error_search);
        }
        productList.setAdapter(adapterProducts);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Product product = (Product) adapterProducts.getItem(position);
                    onClickImageProduct(product);
                } catch (Exception e) {

                }
            }
        });
        hideKeyBoard();
    }

    public void getAllProducts(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new AllProductsFragment()).commit();
        getSupportFragmentManager().executePendingTransactions();
        products = web.getAllProducts();
        productList = (ListView) findViewById(R.id.listViewProducts);

        final AdapterListView adapterProducts = new AdapterListView(products, this, dolarExchangeColon);
        productList.setAdapter(adapterProducts);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Product product = (Product) adapterProducts.getItem(position);
                    onClickImageProduct(product);
                } catch (Exception e) {

                }
            }
        });
    }

    public void getProductsByCategories(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new CategoriesFragment()).commit();
    }

    public void getAllStringProducts(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new AllProductsFragment()).commit();
        getSupportFragmentManager().executePendingTransactions();

        products = web.getProductsByCategory(STRING_CATEGORY);
        productList = (ListView) findViewById(R.id.listViewProducts);

        final AdapterListView adapterProducts = new AdapterListView(products, this, dolarExchangeColon);
        productList.setAdapter(adapterProducts);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Product product = (Product) adapterProducts.getItem(position);
                    onClickImageProduct(product);
                } catch (Exception e) {

                }
            }
        });
    }

    public void getAllWindProducts(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new AllProductsFragment()).commit();
        getSupportFragmentManager().executePendingTransactions();

        products = web.getProductsByCategory(WIND_CATEGORY);
        productList = (ListView) findViewById(R.id.listViewProducts);

        final AdapterListView adapterProducts = new AdapterListView(products, this, dolarExchangeColon);
        productList.setAdapter(adapterProducts);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Product product = (Product) adapterProducts.getItem(position);
                    onClickImageProduct(product);
                } catch (Exception e) {

                }
            }
        });
    }

    public void getAllElectricsProducts(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new AllProductsFragment()).commit();
        getSupportFragmentManager().executePendingTransactions();

        products = web.getProductsByCategory(ELECTRIC_CATEGORY);
        productList = (ListView) findViewById(R.id.listViewProducts);

        final AdapterListView adapterProducts = new AdapterListView(products, this, dolarExchangeColon);
        productList.setAdapter(adapterProducts);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Product product = (Product) adapterProducts.getItem(position);
                    onClickImageProduct(product);
                } catch (Exception e) {

                }
            }
        });
    }

    public void getAllPercussionProducts(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new AllProductsFragment()).commit();
        getSupportFragmentManager().executePendingTransactions();

        products = web.getProductsByCategory(PERCUSION_CATEGORY);
        productList = (ListView) findViewById(R.id.listViewProducts);

        final AdapterListView adapterProducts = new AdapterListView(products, this, dolarExchangeColon);
        productList.setAdapter(adapterProducts);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Product product = (Product) adapterProducts.getItem(position);
                    onClickImageProduct(product);
                } catch (Exception e) {

                }
            }
        });
    }

    public void getAllOtherProducts (View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new AllProductsFragment()).commit();
        getSupportFragmentManager().executePendingTransactions();

        products = web.getProductsByCategory(OTHER_CATEGORY);
        productList = (ListView) findViewById(R.id.listViewProducts);

        final AdapterListView adapterProducts = new AdapterListView(products, this, dolarExchangeColon);
        productList.setAdapter(adapterProducts);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Product product = (Product) adapterProducts.getItem(position);
                    onClickImageProduct(product);
                } catch (Exception e) {

                }
            }
        });
    }

    public void getProductByPrice(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new SearchByPriceFragment()).commit();
    }

    public void getProductByPriceBtnAction(View view) {
        EditText lowerPrice = (EditText) findViewById(R.id.minPrice);
        EditText higherPrice = (EditText) findViewById(R.id.maxPrice);

        int lowerPriceValue = Integer.parseInt(lowerPrice.getText().toString());
        int higherPriceValue = Integer.parseInt(higherPrice.getText().toString());

        System.out.println("Min: " + lowerPriceValue + " max: " + higherPriceValue);
        if (lowerPriceValue > higherPriceValue) {
            showMessage(R.string.error_prices);
        } else {
            products = web.getProductsByPrice(lowerPriceValue + "", higherPriceValue + "");
            productList = (ListView) findViewById(R.id.listViewProductByPrice);

            final AdapterListView adapter = new AdapterListView(products, this, dolarExchangeColon);
            productList.setAdapter(adapter);
            productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Product product = (Product) adapter.getItem(position);
                        onClickImageProduct(product);
                    } catch (Exception e) {

                    }
                }
            });
        }
        hideKeyBoard();
    }

    public void loginBtnRedirect(View v) {
        //Redireccionar a la página de inicio de sessión
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new LoginFragment()).commit();
    }

    public void createAcountBtnRedirect(View v) {
        //Redireccionar a la página de registro
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new CreateAcountFragment()).commit();
    }

    public void loginBtnAction(View view) {
        userName = (EditText) findViewById(R.id.etUsername);
        passwordName = (EditText) findViewById(R.id.etPassword);

        User user = web.validateIfUserExists(URI_LOGIN, userName.getText() + "/" + passwordName.getText());
        if (user != null) {
            editor.putBoolean(ACTIVITY_EXECUTED, true);
            editor.putBoolean(IS_LOGIN, true);
            editor.putString(USER_ID, user.getId());
            editor.putString(NAME, user.getName() + " " + user.getLastName());
            editor.putString(EMAIL, user.getEmail());
            editor.putString(ROLE, user.getRole());
            editor.putString(PASSWORD, user.getPassword());
            editor.commit();

            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new PrincipalPage()).commit();
        } else {
            showMessage(R.string.error_login);
        }
        hideKeyBoard();
    }

    public void createAcountBtnAction(View view) {
        userName = (EditText) findViewById(R.id.etFName);
        passwordName = (EditText) findViewById(R.id.etPassword);
        userEmail = (EditText) findViewById(R.id.etEmail);
        userLastName = (EditText) findViewById(R.id.etLName);
        cardNumber = (EditText) findViewById(R.id.etCarnNumber);

        User newUser = new User(userName.getText().toString(), userLastName.getText().toString(), userEmail.getText().toString(),
                passwordName.getText().toString(), CLIENT_ROLE, cardNumber.getText().toString());
        web.registerUser(newUser);

        editor.putString(NAME, newUser.getName() + " " + newUser.getLastName());
        editor.putString(EMAIL, newUser.getEmail());
        editor.putString(ROLE, newUser.getRole());
        editor.putString(PASSWORD, newUser.getPassword());
        editor.commit();

        hideKeyBoard();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof PostRequestWebServicesConsumer){
            if((boolean)arg){
                editor.putBoolean(ACTIVITY_EXECUTED, true);
                editor.putBoolean(IS_LOGIN, true);
                editor.commit();

                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new PrincipalPage()).commit();
            }else{
                showMessage(R.string.error_registration);
            }
        } else if (o instanceof CentralBankWebServicesConsumer){
            dolarExchangeColon = Double.parseDouble(arg.toString());
        } else if (o instanceof WebServicesConsumer){
            if(arg instanceof Boolean) {
                if (!(boolean) arg) {
                    hideKeyBoard();
                    showMessage(R.string.error_search);
                }
            } else if (arg instanceof String){
                if(arg.equals(ERROR_LOGIN_AUTENTICATION)){
                    editor.putBoolean(ACTIVITY_EXECUTED, false);
                    editor.putBoolean(IS_LOGIN, false);
                    editor.remove(NAME);
                    editor.remove(PASSWORD);
                    editor.remove(EMAIL);
                    editor.remove(ROLE);
                    editor.commit();
                    Intent intent = new Intent(this, StartPageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        } else if(o instanceof UserUpdater){
            if ((boolean) arg) {
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new PrincipalPage()).commit();
            } else {
                showMessage(R.string.error_updating);
            }
        }
    }

    private void updateAccount(){
        if (sharedPreferences.contains(EMAIL)) {
            Map<String, ?> map = sharedPreferences.getAll();
            String userEmailText = map.get(EMAIL) + "";
            String userPassword = map.get(PASSWORD) + "";
            userID = map.get(USER_ID) + "";

            User user = web.validateIfUserExists(URI_LOGIN, userEmailText + "/" + userPassword);
            if (user != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new UpdateAcountFragment()).commit();
                getSupportFragmentManager().executePendingTransactions();

                userName = (EditText) findViewById(R.id.etFName2);
                passwordName = (EditText) findViewById(R.id.etPassword2);
                userEmail = (EditText) findViewById(R.id.etEmail2);
                userLastName = (EditText) findViewById(R.id.etLName2);
                cardNumber = (EditText) findViewById(R.id.etCarnNumber2);

                userName.setText(user.getName());
                passwordName.setText(user.getPassword());
                userEmail.setText(user.getEmail());
                userLastName.setText(user.getLastName());
                cardNumber.setText(user.getCardNumber());
            }
        }
    }

    public void updateAcountBtnAction (View view){
        userName = (EditText) findViewById(R.id.etFName2);
        passwordName = (EditText) findViewById(R.id.etPassword2);
        userEmail = (EditText) findViewById(R.id.etEmail2);
        userLastName = (EditText) findViewById(R.id.etLName2);
        cardNumber = (EditText) findViewById(R.id.etCarnNumber2);

        User newUser = new User(userID, userName.getText().toString(), userLastName.getText().toString(), userEmail.getText().toString(),
                passwordName.getText().toString(), CLIENT_ROLE, cardNumber.getText().toString());
        web.updateUser(newUser);

        editor.putString(USER_ID, userID);
        editor.putString(NAME, newUser.getName() + " " + newUser.getLastName());
        editor.putString(EMAIL, newUser.getEmail());
        editor.putString(ROLE, newUser.getRole());
        editor.putString(PASSWORD, newUser.getPassword());
        editor.commit();
        hideKeyBoard();
    }

    private void hideKeyBoard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void showMessage(int message){
        mySnackbar = Snackbar.make(findViewById(R.id.contenedor), message, Snackbar.LENGTH_SHORT);
        mySnackbar.show();
    }

    public void addToShoppingCart(View view){
        shoppingCart.add(currentProduct);
        System.out.println(currentProduct.toString());
    }

    public void onClickImageProduct(Product product){
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new ProductDetailFragment()).commit();
        getSupportFragmentManager().executePendingTransactions();

        TextView productName = (TextView) findViewById(R.id.textViewName2);
        TextView productDollarPrice = (TextView) findViewById(R.id.textViewDolarPrice2);
        TextView productColonPrice = (TextView) findViewById(R.id.textViewColonPrice2);
        ImageView productImage = (ImageView) findViewById(R.id.imageViewProduct2);
        TextView productDescription = (TextView) findViewById(R.id.textViewDescription2);
        TextView productBrand = (TextView) findViewById(R.id.textViewBrand2);

        productName.setText(product.getName());
        productDollarPrice.setText(String.format( "$ %.2f",(product.getPrice() / dolarExchangeColon)));
        productColonPrice.setText(String.format( "¢ %.2f",(product.getPrice())));
        Picasso.with(this).load(product.getImageUrl()).into(productImage);
        productDescription.setText(product.getDescription());
        productBrand.setText("Marca: " + product.getBrand());
        currentProduct = product;
    }

    public double getDolarExchangeColon() {
        return dolarExchangeColon;
    }

    public void setDolarExchangeColon(double dolarExchangeColon) {
        this.dolarExchangeColon = dolarExchangeColon;
    }
}
