package com.melodicmusic.mobileapp.controller;

import com.melodicmusic.mobileapp.utility.IConstants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by Nelson on 3/9/2017.
 */

public class CentralBankWebServicesConsumer extends Observable implements IConstants, Runnable{
    private String startDate;
    private String endDate;

    public CentralBankWebServicesConsumer(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void run() {
        String result;

        SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
        request.addProperty(TC_INDICADOR, INDICADOR);
        request.addProperty(TC_FECHA_INICIO, startDate);
        request.addProperty(TC_FECHA_FIN, endDate);
        request.addProperty(TC_NOMBRE, TC_NAME);
        request.addProperty(TC_SUB_NIVELES, SUB_LEVELS);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        try{
            HttpTransportSE transportSE = new HttpTransportSE(URL_CENTRAL_BANK);
            transportSE.call(SOAP_ACTION, envelope);

            SoapObject result_XML = (SoapObject) envelope.bodyIn;
            SoapObject outterBody = (SoapObject) result_XML.getProperty(PROPERTY_WS_BANCK_1);
            SoapObject diffGram = (SoapObject) outterBody.getProperty(PROPERTY_WS_BANCK_2);
            SoapObject innerDifferGram = (SoapObject) diffGram.getProperty(PROPERTY_WS_BANCK_3);
            SoapObject indicators = (SoapObject) innerDifferGram.getProperty(PROPERTY_WS_BANCK_4);
            SoapPrimitive exchangeValue = (SoapPrimitive) indicators.getProperty(PROPERTY_WS_BANCK_5);

            setChanged();
            notifyObservers(exchangeValue.getValue());
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
