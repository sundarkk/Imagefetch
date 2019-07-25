package com.vikas.imagesendingfetching;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Parse {

    public static String keyid="imageurl";
    public static String Arrayname="result";
    public static String [] imageurl;

    public Parse(String response){

        try {
            JSONObject jo=new JSONObject(response);
            JSONArray ja=jo.getJSONArray(Arrayname);
            imageurl=new String[ja.length()];
            for (int i=0;i<ja.length();i++){

                JSONObject job=ja.getJSONObject(i);
                imageurl[i]=job.getString(keyid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
