package com.fightitwithfitness.stemfundmanager.utils;

import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by jpriv on 9/27/2017.
 */

public class WSApi {
    public static final String VERSION = "1";

    public static final String BASE_URL = "REPLACE_WITH_APPROPRIATE_BASE_URL/" + VERSION + "/funds";
    public static final String ID = "/%d";

    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final MediaType JSON = MediaType.parse("application/json");

    public static OkHttpClient sHttpInstance;

    public static OkHttpClient getHttpClient() {
        if (sHttpInstance == null) {
            sHttpInstance = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        }
        return sHttpInstance;
    }
}
