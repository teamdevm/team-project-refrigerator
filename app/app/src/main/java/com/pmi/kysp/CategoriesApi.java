package com.pmi.kysp;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoriesApi {
    public static List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(0, "Все", true));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    URL url = new URL(String.format("%s/categories",BuildConfig.SERVER_URI));

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(1000);
                    connection.setReadTimeout(1000);
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
                    JSONArray jsonCategories = new JSONArray(data);
                    for (int i = 0; i < jsonCategories.length(); i++) {
                        JSONObject jsonCategory = jsonCategories.getJSONObject(i);
                        categories.add(new Gson().fromJson(jsonCategory.toString(), Category.class));
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
        return categories;
    }
}
