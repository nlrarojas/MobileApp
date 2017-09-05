package com.melodicmusic.mobileapp.utility;

/**
 * Created by Nelson on 1/9/2017.
 */

public interface IConstants {
    public static String LOGIN_SAVED_PREFERENCES = "loginPreferences";

    public static String USER_ID = "user_id";
    public static String NAME = "name";
    public static String LAST_NAME = "lastName";
    public static String EMAIL = "email";
    public static String PASSWORD = "password";
    public static String ROLE = "role";
    public static String CARD_NUMBER = "cardNumber";
    public static String CLIENT_ROLE = "client";

    public static String SELECT_FORM_ENTER = "start_app";
    public static String ACTIVITY_EXECUTED = "activity_activated";
    public static String IS_LOGIN = "login";

    public static String URI = "http://melodicmusicserver-env.us-west-2.elasticbeanstalk.com/";
    public static String URI_LOGIN = "api/User/LogIn/";
    public static String URI_INSERT_USER = "api/User/";
    public static String URI_UPDATE_USER = "/api/User/";
    public static String URI_GET_ALL_PRODUCTS = "api/Product/";
    public static String URI_PRODUCTS_BY_PRICE = "api/Product/getProductsByPrice/";
    public static String URI_PRODUCTS_BY_CATEGORY = "api/Product/getProductsByCategory/";
    public static String URI_PRODUCTS_BY_NAME = "api/Product/getProductsByName/";

    public static String NAME_SPACE = "http://ws.sdde.bccr.fi.cr";
    public static String URL_CENTRAL_BANK = "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx";
    public static String METHOD_NAME = "ObtenerIndicadoresEconomicos";
    public static String SOAP_ACTION = "http://ws.sdde.bccr.fi.cr/ObtenerIndicadoresEconomicos";

    public static String TC_INDICADOR = "tcIndicador";
    public static String TC_FECHA_INICIO = "tcFechaInicio";
    public static String TC_FECHA_FIN = "tcFechaFinal";
    public static String TC_NOMBRE = "tcNombre";
    public static String TC_SUB_NIVELES = "tnSubNiveles";

    public static String INDICADOR = "317";
    public static String TC_NAME = "Nelson";
    public static String SUB_LEVELS = "N";

    public static String PROPERTY_WS_BANCK_1 = "ObtenerIndicadoresEconomicosResult";
    public static String PROPERTY_WS_BANCK_2 = "diffgram";
    public static String PROPERTY_WS_BANCK_3 = "Datos_de_INGC011_CAT_INDICADORECONOMIC";
    public static String PROPERTY_WS_BANCK_4 = "INGC011_CAT_INDICADORECONOMIC";
    public static String PROPERTY_WS_BANCK_5 = "NUM_VALOR";

    public static String WIND_CATEGORY = "Viento";
    public static String STRING_CATEGORY = "Cuerda";
    public static String PERCUSION_CATEGORY = "Percusion";
    public static String ELECTRIC_CATEGORY = "Electricos";
    public static String OTHER_CATEGORY = "Otro";

    public static String ERROR_LOGIN_AUTENTICATION = "error_login_autentication";
}
