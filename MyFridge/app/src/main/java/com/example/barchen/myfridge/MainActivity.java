package com.example.barchen.myfridge;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView responseView;
    EditText ingredientsText;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        responseView = (TextView) findViewById(R.id.responseView);
        ingredientsText = (EditText) findViewById(R.id.ingredientsText);
        imageView = (ImageView) findViewById(R.id.image);

        final Button button = (Button) findViewById(R.id.queryButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new RetrieveRecipe().execute();
            }
        });
    }


    /** this class handles the API calls in a separate thread */
    private class RetrieveRecipe extends AsyncTask<Void, Void, String> {
        private final String API_KEY = "7468a23ef586c3ee0846e3ff5a0048f2";
        private final String API_SEARCH_URL = "http://food2fork.com/api/search";
        private final String API_RECIPE_REQUEST_URL = "http://food2fork.com/api/get";

        private Exception exception;


        protected void onPreExecute() {
            //TODO: implement if needed

            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        private String getIngredients(){
            return ingredientsText.getText().toString();
        }

        //a method to generate the query that need to be passed to the api
        private String buildQuery(String[] ingredients){
            String query = "";
            if (ingredients.length == 0){
                return query;
            }
            else if (ingredients.length == 1) {
                return ingredients[0];
            }
            else {
                for (int i = 0; i < ingredients.length - 1; i++){
                    query += ingredients[i];
                    query += "%2c";
                }
                query += ingredients[ingredients.length - 1];
                return query;
            }
        }

        protected String doInBackground(Void... urls) {
            String input = getIngredients();
            String[] ingredients = input.split(" ");
            String query = buildQuery(ingredients);


            // Do some validation here

            try {
                URL url = new URL(API_SEARCH_URL + "?key=" + API_KEY + "&q=" + query);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);


            //TODO: transform the string to a recipe object

            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                int count = object.getInt("count");

                JSONArray recipes = object.getJSONArray("recipes");

                //this loop translate the JSON objects into usable Recipe objects
                ArrayList<Recipe> recipeObjects = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    JSONObject jsonRecipe = recipes.getJSONObject(i);
                    String title = jsonRecipe.getString("title");
                    String sourceUrl = jsonRecipe.getString("source_url");
                    String recipeId = jsonRecipe.getString("recipe_id");
                    String imageUrl = jsonRecipe.getString("image_url");
                    Recipe recipe = new Recipe(recipeId, title, sourceUrl, imageUrl);
                    recipeObjects.add(recipe);
                }

                if (recipeObjects.size() > 0){
                    Recipe recipeToShow = recipeObjects.get(0);
                    responseView.setText(recipeToShow.getTitle());
                    imageView.setImageDrawable(recipeToShow.LoadImageFromWebOperations(recipeToShow.getImageUrl()));
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    responseView.setText("No recipes found =(");
                }



            } catch (JSONException e) {
                // Appropriate error handling code
            }
        }
    }
}
