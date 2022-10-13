package eu.ways4.newsmaniac.remote;

import eu.ways4.newsmaniac.BuildConfig;


import eu.ways4.newsmaniac.remote.api.ApiServiceInterface;
import eu.ways4.newsmaniac.remote.api.RetrofitClient;

public class ApiUtil {

    private static final String BASE_API_URL = "https://newsapi.org/";

    public static String API_KEY = BuildConfig.API_KEY;


    public static ApiServiceInterface getNewsApiServiceInterface() {
//        if(API_KEY == null || API_KEY.isEmpty()){
//            Activity currActivity = getActivity();
//            SharedPreferences sharedPref = currActivity.getPreferences(Context.MODE_PRIVATE);
//            sharedP = sharedPref;
//        }
        return RetrofitClient.getRetrofitClient(BASE_API_URL).create(ApiServiceInterface.class);
    }
}
