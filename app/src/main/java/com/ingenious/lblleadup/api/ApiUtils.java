package com.ingenious.lblleadup.api;

public class ApiUtils
{
    //public static final String BASE_URL = "http://leadupaws-env.eba-dptn2rdg.me-south-1.elasticbeanstalk.com/index.php/UserApi/"; //Testing
    public static final String BASE_URL = "https://api2.leadup.app/index.php/UserApi/"; //AWS

    public static final String grant_type = "client_credentials";
    public static final String client_id = "leadup_china_client";
    public static final String client_secret = "12ec66a69dcbeaf939933f126b1bb07ad3c4d2cc12ec66ta69dccbeahf9399i33f1n26lba1ibbce07lnad3c4d2ecce6f4437a6971f2deabb089u2fd0p36f66c841abbff40";

    public static SOService getSOService()
    {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}