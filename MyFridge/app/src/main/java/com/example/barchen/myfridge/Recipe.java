package com.example.barchen.myfridge;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by barchen on 11/12/16.
 */

/** Class containing all the recepies attributes */
public class Recipe {
    private String title;
    private String sourceUrl;
    private String recipeId;
    private String imageUrl;

    public Recipe(String recipeId, String title, String sourceUrl, String imageUrl){
        this.recipeId = recipeId;
        this.title = title;
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
    }

    //http://stackoverflow.com/questions/6407324/how-to-get-image-from-url-in-android
    public Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
