package com.melodicmusic.mobileapp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;


/**
 * Created by Nelson on 2/9/2017.
 */

public class WebServicesConsumer  {

    private String archiveName;
    private String url;

    public WebServicesConsumer(String archiveName, String url) {
        this.archiveName = archiveName;
        this.url = "http://melodicmusicserver-env.us-west-2.elasticbeanstalk.com/api/User/LogIn/adriansb3105@gmail.com/12345";
    }

    public String requestContent() {
        HttpClient httpclient = new DefaultHttpClient();
        String result = null;
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        InputStream instream = null;

        try {
            System.out.println("Enviando");
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            System.out.println("Respuesta");
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
        return result;
    }

    public String convertStreamToString(InputStream is) {
        System.out.println("Convirtiendo");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

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
