package com.sparksplugin.gateauthenticator;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender  {

    String userFcmToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;
    String id;
    String send;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey ="AAAAC65lKdk:APA91bGJJS7dzzWXhMZqHtn_KqhxvN9Hw1yXuMZGGJ9XQowpcOE9e80MUEbVJ7Yi0wgS17JvrchkLoIWv14kXYDyarvUgoS88QAMBYZu1iZe8T7mPp-KAKLkgsZsWq2cbU5bbQaXVU8w";

    public FcmNotificationsSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity, String id,String send) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.id = id;
        this.send = send;


    }

    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();

        try {
            mainObj.put("to", "/topics/"+userFcmToken);
             // enter icon that exists in drawable only
            JSONObject jNot = new JSONObject();
            JSONObject jData = new JSONObject();
            jNot.put("title",title);
            jNot.put("body",body);
            jData.put("user_id", id);
            jData.put("title", title);
            jData.put("body", body);
            jData.put("send", send);
            //mainObj.put("notification",jNot);
            mainObj.put("data", jData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // code run is got response

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // code run is got error

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> header = new HashMap<>();
                    header.put("Content-Type", "application/json");
                    header.put("Authorization", "key=" + fcmServerKey);

                    /*Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);*/
                    return header;
                }
            };
            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}