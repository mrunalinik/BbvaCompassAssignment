package com.assignment.bbvaassignment.apis;

import android.os.AsyncTask;

import com.assignment.bbvaassignment.listeners.OnAsyncTaskCompleteListener;
import com.assignment.bbvaassignment.utils.Utility;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class GetAsync extends AsyncTask<String,Void,String> {

    private OnAsyncTaskCompleteListener asyncComplete;
    private int connectionTimeOut;
    private int socketTimeOut;

    public GetAsync(int connectionTimeOut, int socketTimeOut, OnAsyncTaskCompleteListener asyncComplete){
        this.asyncComplete = asyncComplete;
        this.connectionTimeOut = connectionTimeOut;
        this.socketTimeOut = socketTimeOut;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection conn = null;
        try {
            URL myurl = new URL(params[0]);

            conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(connectionTimeOut);
            conn.setReadTimeout(socketTimeOut);
            conn.setRequestMethod("GET");


            InputStream inputStream = conn.getInputStream();
            String responseStr = Utility.convertStreamToString(inputStream);
            return responseStr;
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(asyncComplete != null)
            asyncComplete.onComplete(result);
    }

}
