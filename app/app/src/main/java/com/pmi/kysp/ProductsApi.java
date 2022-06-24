package com.pmi.kysp;

import android.util.JsonReader;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

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
                    URL url = new URL(String.format("%s/products/%s",BuildConfig.SERVER_URI, barcode));

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
                    URL url = new URL(String.format("%s/products/%s",BuildConfig.SERVER_URI, barcode));

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

    public static List<Product> getProducts(List<String> barcodes)
    {
        List<Product> products = new ArrayList<>();

        if (barcodes.isEmpty())
            return products;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    URL url = new URL(String.format("%s/products?list_of_barcodes=%s",BuildConfig.SERVER_URI, String.join(",", barcodes)));
                    Log.d("url", url.toString());
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
                    JSONArray productsJSON = new JSONArray(data);
                    for (int i = 0; i < productsJSON.length(); i++)
                    {
                        JSONObject productJSON = productsJSON.getJSONObject(i);
                        Product product = new Gson().fromJson(productJSON.toString(), Product.class);
                        products.add(product);
                    }

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
        return products;
    }
}
