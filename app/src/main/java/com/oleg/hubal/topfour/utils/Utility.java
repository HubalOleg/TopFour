package com.oleg.hubal.topfour.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by User on 14.12.2016.
 */

public class Utility {

    public static JSONObject getJSONObjectFromResponse(Response<ResponseBody> response) throws IOException, JSONException {
        return new JSONObject(response.body().string()).getJSONObject("response");
    }
}
