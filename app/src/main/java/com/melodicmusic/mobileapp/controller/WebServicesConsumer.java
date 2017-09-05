package com.melodicmusic.mobileapp.controller;

import android.app.Activity;

import com.melodicmusic.mobileapp.model.Product;
import com.melodicmusic.mobileapp.model.User;
import com.melodicmusic.mobileapp.utility.IConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;


/**
 * Created by Nelson on 2/9/2017.
 */

public class WebServicesConsumer extends Observable implements IConstants{
    private PostRequestWebServicesConsumer postSender;
    private UserUpdater userUpdater;
    private Activity mainActivity;

    public WebServicesConsumer(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private InputStream requestContent(String URIAppend, String values) {
        InputStream instream = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(URI + URIAppend + values);
            HttpResponse response;

            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                instream = entity.getContent();
            }
        } catch (ClientProtocolException | IllegalArgumentException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return instream;
    }

    public User validateIfUserExists(String URIAppend, String values){
        User userFound = null;
        InputStream instream = requestContent(URIAppend, values);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            String queryResult;

            queryResult = reader.readLine();
            JSONObject user = new JSONObject(queryResult);
            userFound = new User(user.getString("_id"), user.getString("name"), user.getString("lastName"), user.getString("email"),
                    user.getString("password"),user.getString("role"), user.getString("cardNumber"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            this.addObserver((Observer) mainActivity);
            setChanged();
            notifyObservers(ERROR_LOGIN_AUTENTICATION);
            e.printStackTrace();
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (Exception exc) {

                }
            }
        }
        return userFound;
    }

    public boolean registerUser(User user){
        JSONObject userJSONObject = new JSONObject();
        try {
            userJSONObject.put(NAME, user.getName());
            userJSONObject.put(LAST_NAME, user.getLastName());
            userJSONObject.put(EMAIL, user.getEmail());
            userJSONObject.put(PASSWORD, user.getPassword());
            userJSONObject.put(ROLE, user.getRole());
            userJSONObject.put(CARD_NUMBER, user.getCardNumber());
            postSender = new PostRequestWebServicesConsumer(userJSONObject);
            postSender.addObserver((Observer) mainActivity);
            postSender.run();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User user){
        JSONObject userJSONObject = new JSONObject();
        try {
            userJSONObject.put(USER_ID, user.getId());
            userJSONObject.put(NAME, user.getName());
            userJSONObject.put(LAST_NAME, user.getLastName());
            userJSONObject.put(EMAIL, user.getEmail());
            userJSONObject.put(PASSWORD, user.getPassword());
            userJSONObject.put(ROLE, user.getRole());
            userJSONObject.put(CARD_NUMBER, user.getCardNumber());
            userUpdater = new UserUpdater(userJSONObject, user.getId());
            userUpdater.addObserver((Observer) mainActivity);
            userUpdater.run();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> getListOfProducts(List<Product> listProducts, String queryResult) {
        System.out.println(queryResult);
        try{
            JSONArray productsArray = new JSONArray(queryResult);
            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject productJSON = productsArray.getJSONObject(i);
                Product product = new Product(productJSON.get("_id").toString(), productJSON.get("name").toString(),
                        Double.parseDouble(productJSON.get("price").toString()), productJSON.get("category").toString(),
                        productJSON.get("brand").toString(), productJSON.get("description").toString(), productJSON.get("imageUrl").toString());
                listProducts.add(product);
            }
            return listProducts;
        } catch (JSONException e){
            this.addObserver((Observer) mainActivity);
            setChanged();
            notifyObservers(false);
        }
        return listProducts;
    }

    public List<Product> getAllProducts(){
        InputStream instream = requestContent(URI_GET_ALL_PRODUCTS, "");
        List<Product> listProducts = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            String queryResult;

            queryResult = reader.readLine();
            listProducts = getListOfProducts(listProducts, queryResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (Exception exc) {

                }
            }
        }
        return listProducts;
    }

    public List<Product> getProductsByCategory(String category){
        InputStream instream = requestContent(URI_PRODUCTS_BY_CATEGORY, category);
        List<Product> listProducts = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            String queryResult;

            queryResult = reader.readLine();
            listProducts = getListOfProducts(listProducts, queryResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (Exception exc) {

                }
            }
        }
        return listProducts;
    }

    public List<Product> getProductsByPrice(String lowerPrice, String higherPrice){
        InputStream instream = requestContent(URI_PRODUCTS_BY_PRICE, lowerPrice + "/" + higherPrice);
        List<Product> listProducts = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            String queryResult;

            queryResult = reader.readLine();
            listProducts = getListOfProducts(listProducts, queryResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (Exception exc) {

                }
            }
        }
        return listProducts;
    }

    public List<Product> getProductsByName(String productName){
        InputStream instream = requestContent(URI_PRODUCTS_BY_NAME, productName);
        List<Product> listProducts = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            String queryResult;

            queryResult = reader.readLine();
            listProducts = getListOfProducts(listProducts, queryResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (Exception exc) {

                }
            }
        }
        return listProducts;
    }
}
