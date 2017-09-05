package com.melodicmusic.mobileapp.controller;

import com.melodicmusic.mobileapp.utility.IConstants;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

/**
 * Created by Nelson on 3/9/2017.
 */

public class PostRequestWebServicesConsumer extends Observable implements Runnable, IConstants{
    private JSONObject newUser;

    public PostRequestWebServicesConsumer(JSONObject newUser) {
        this.newUser = newUser;
    }

    @Override
    public void run() {
        String sb = "";
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(URI + URI_INSERT_USER); //Enter URL here
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(newUser.toString());
            wr.flush();
            wr.close();

            int httpResult = httpURLConnection.getResponseCode();
            System.out.println("Respuesta entero: " +  httpResult + " Mensaje: " + httpURLConnection.getResponseMessage());
            if(httpResult == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb += line + "\n";
                }
                br.close();

                setChanged();
                notifyObservers(true);
            }else{
                setChanged();
                notifyObservers(false);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
