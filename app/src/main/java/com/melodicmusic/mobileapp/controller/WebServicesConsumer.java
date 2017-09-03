package com.melodicmusic.mobileapp.controller;

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

    public WebServicesConsumer() {

    }

    private InputStream requestContent(String URIAppend, String values) {
        HttpClient httpclient = new DefaultHttpClient();
        String result = null;
        HttpGet httpget = new HttpGet(URI + URIAppend + values);
        HttpResponse response;
        InputStream instream = null;

        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                instream = entity.getContent();
                result = convertStreamToString(instream);
                System.out.println(result);
            }
        } catch (ClientProtocolException e) {
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
        return instream;
    }

    public User validateIfUserExists(String URIAppend, String values){
        User userFound = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(requestContent(URIAppend, values), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;

            line = reader.readLine();
            JSONArray users = new JSONArray(line);
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                System.out.println(user.toString());
                userFound = new User(user.getString("name"), user.getString("lastName"), user.getString("email"), user.getString("role"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userFound;
    }

    public String convertStreamToString(InputStream is) throws UnsupportedEncodingException {
        String line = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        try {
            line = reader.readLine();
            JSONArray users = new JSONArray(line);
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                System.out.println(user.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }
        return sb.toString();
    }
}
