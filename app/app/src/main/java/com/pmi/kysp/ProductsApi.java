package com.pmi.kysp;

import android.util.JsonReader;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductsApi {
    private static Product product;
    private static int responseCode;
    public static int checkProduct(String barcode){
        responseCode = -1;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    URL url = new URL(String.format("%s/products/%s",DBconfig.URI, barcode));

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("Connection", "close");
                    responseCode = connection.getResponseCode();
                    connection.disconnect();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return responseCode;
    }

    public static Product getProduct(String barcode){
        product = null;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    URL url = new URL(String.format("%s/products/%s",DBconfig.URI, barcode));

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("Connection", "close");
                    if (connection.getResponseCode() != 200) return;

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    connection.disconnect();
                    String data = sb.toString();
                    product = new Gson().fromJson(data, Product.class);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return product;
    }
}
