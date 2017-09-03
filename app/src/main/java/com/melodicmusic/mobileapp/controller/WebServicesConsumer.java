package com.melodicmusic.mobileapp.controller;

import android.app.Activity;

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

public class WebServicesConsumer  implements IConstants{
    private PostRequestWebServicesConsumer postSender;
    private Activity mainActivity;

    public WebServicesConsumer(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private InputStream requestContent(String URIAppend, String values) {
        InputStream instream = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            String result = null;
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
            StringBuilder sb = new StringBuilder();
            String queryResult;

            queryResult = reader.readLine();
            JSONObject user = new JSONObject(queryResult);
            userFound = new User(user.getString("_id"), user.getString("name"), user.getString("lastName"), user.getString("email"), user.getString("role"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
            System.out.println("JSON Object creado");
            postSender = new PostRequestWebServicesConsumer(userJSONObject);
            postSender.addObserver((Observer) mainActivity);
            postSender.run();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
