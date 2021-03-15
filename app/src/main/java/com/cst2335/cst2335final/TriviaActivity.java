package com.cst2335.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class TriviaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String category[] = {"Any Category", "Sports", "Cars", "Movies"};
//    String category[];
//    HashMap<String, String> categoryList;
//    private HashMap<String, String> category = CategoryList;
    URLBuilder urlBuilder = new URLBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

//        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());
//
//        urlBuilder.setCategory(12);
//        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());
//
//        urlBuilder.setQuantity(5);
//        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());
//
//        urlBuilder.setType(2);
//        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());
//
//        urlBuilder.setDifficulty(2);
//        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());



        Spinner spinner = findViewById(R.id.categorySpinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, this.category);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        ImageButton any = findViewById(R.id.any);
        ImageButton easy = findViewById(R.id.easy);
        ImageButton medium = findViewById(R.id.medium);
        ImageButton hard = findViewById(R.id.hard);

        //On click listener for any button
        any.setOnClickListener(click -> {
            urlBuilder.setDifficulty(0);
            Toast.makeText(this, "Any", Toast.LENGTH_SHORT).show();
            Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());
            any.setBackgroundColor(Color.parseColor("#cad3fe"));
            easy.setBackgroundColor(0);
            medium.setBackgroundColor(0);
            hard.setBackgroundColor(0);
        });

        //On click listener for easy button
        easy.setOnClickListener(click -> {
            urlBuilder.setDifficulty(1);
            Toast.makeText(this, "Easy", Toast.LENGTH_SHORT).show();
            Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());
            easy.setBackgroundColor(Color.parseColor("#cad3fe"));
            any.setBackgroundColor(0);
            medium.setBackgroundColor(0);
            hard.setBackgroundColor(0);
        });

        //On click listener for medium button
        medium.setOnClickListener(click -> {
            urlBuilder.setDifficulty(2);
            Toast.makeText(this, "Medium", Toast.LENGTH_SHORT).show();
            Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());
            medium.setBackgroundColor(Color.parseColor("#cad3fe"));
            any.setBackgroundColor(0);
            easy.setBackgroundColor(0);
            hard.setBackgroundColor(0);
        });

        //On click listener for hard button
        hard.setOnClickListener(click -> {
            urlBuilder.setDifficulty(3);
            Toast.makeText(this, "Hard", Toast.LENGTH_SHORT).show();
            Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());
            hard.setBackgroundColor(Color.parseColor("#cad3fe"));
            any.setBackgroundColor(0);
            easy.setBackgroundColor(0);
            medium.setBackgroundColor(0);
        });


        // ******Creating OnclickListener that leads to TriviaPlayActivity***************
        Button startTrivia =  findViewById(R.id.start);
        Intent goTriviaPlay = new Intent(this, TriviaPlayActivity.class);
        startTrivia.setOnClickListener( click -> {
            EditText quantity = findViewById(R.id.enterQuantity);
            RadioGroup typeGroupChoice = findViewById(R.id.radioChoiceGroup);
            int typeID = typeGroupChoice.getCheckedRadioButtonId();
            if(quantity.getText() == null || quantity.getText().toString().isEmpty()){
                quantity.setText("10");
                urlBuilder.setQuantity(Integer.parseInt(String.valueOf(quantity.getText())));
                urlBuilder.setType(typeID);
                CategoryList categoryList = new CategoryList();
                categoryList.execute("https://opentdb.com/api_category.php");
                Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL() + " and type is: " + typeID);
                startActivity(goTriviaPlay);
            }else if(Integer.parseInt(quantity.getText().toString()) <= 0 || Integer.parseInt(quantity.getText().toString()) >= 100){
                Toast.makeText(this, "Please, enter a valid number of questions from 0 to 99", Toast.LENGTH_LONG).show();
            }
            else{
                urlBuilder.setQuantity(Integer.parseInt(String.valueOf(quantity.getText())));
                urlBuilder.setType(typeID);
//                CategoryList categoryList = new CategoryList();
//                categoryList.execute(urlBuilder.getURL());
                Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL() + " and type is: " + typeID);
                startActivity(goTriviaPlay);
            }
        });
        // ******End of OnclickListener that leads to TriviaPlayActivity***************
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position == 0){
            urlBuilder.setCategory(0);
        }else{
            //Show a Toast message whenever a category is selected
            Toast.makeText(this, category[position], Toast.LENGTH_SHORT).show();
            urlBuilder.setCategory(position);
        }
        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * This class is used to build the custom API url based on the user choice like quantity of questions,
     * difficulty, questions category and type. After build the URL, the app will used it to load the questions.
     */
    private class URLBuilder {
        private final String baseAPIURL = "https://opentdb.com/api.php?";
        private int quantity;
        private int category;
        private final String[] difficulty = {"Any", "Easy", "Medium", "Hard"};
        private int difficultyChoice;
        private final String[] typeOfQuestion = {"Any", "Multiple Choice", "True/False"};
        private int typeChoice;

        /**
         * Default constructor to initialize the default variable values if the user do not choose all or some of them
         */
        public URLBuilder() {
            this.quantity = 10;
            this.category = 0;
            this.difficultyChoice = 0;
            this.typeChoice = 0;
        }

        /** This method sets the quantity variable by passing the @param quantity value to the instance variable */
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        /** This method sets the category variable by passing the @param category value to the instance variable */
        public void setCategory(int category) {
            this.category = category;
        }

        /** This method sets the type of questions variable by passing the @param typeChoice value to the instance variable */
        public void setType(int typeChoice) {
            //Compares if typeChoice value matches with the view id of multiple choice chosen by the user in the UI
            if(typeChoice == 2131230971){
                //Multiple choice
                //Sets the typeChoice to 1
                this.typeChoice = 1;
            }
            //Compares if typeChoice value matches with the view id of True/False choice chosen by the user in the UI
            else if(typeChoice == 2131230811){
                //True or False (boolean)
                //Sets the typeChoice to 1
                this.typeChoice = 2;
            }
            //If the user didn't choose the type of question or if selected any type, the variable typeChoice will receive the default type
            else{
                //Any type of questions
                this.typeChoice = 0;
            }

        }

        /**
         * This method sets difficulty variable by passing @param difficultyChoice value to the instance variable */
        public void setDifficulty(int difficultyChoice) {
            this.difficultyChoice = difficultyChoice;
        }

        /** This method builds and @return the complete URL as a String to be used to load the questions based on the user's choice */
        public String getURL(){
            //Creating and initializing a string builder object with the base URL from the API that can be changed depending on the user's choice
            StringBuilder builder = new StringBuilder(this.baseAPIURL);

            builder.append("amount=" + this.quantity);
            if(this.category > 0){
                builder.append("&");
                builder.append("category=" + this.category);
            }
            if(this.difficultyChoice > 0 && this.difficultyChoice <= 3) {
                builder.append("&");
                builder.append("difficulty=" + this.difficulty[difficultyChoice].toLowerCase());
            }
            if(this.typeChoice >= 1 && this.typeChoice <= 2) {
                builder.append("&");
                if(this.typeChoice == 1){
                    builder.append("type=" + "multiple");
                }else if(this.typeChoice == 2) {
                    builder.append("type=" + "boolean");
                }

            }
            return builder.toString();
        }//End of getURL
    }//End of URLBuilder

    private class CategoryList extends AsyncTask<String, Integer, String> {

        private int categoryID;
        private String categoryName;
        HashMap<String, String> categoryList = new HashMap<String, String>();

        @Override
        protected String doInBackground(String... args) {
            try {
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string
                Log.i("JSON", result);//Debug###########

                // convert string to JSON
                JSONObject categories = new JSONObject(result);
                JSONArray jsonArray = categories.getJSONArray("trivia_categories");
                Log.i("JSON", categories.toString());
                for(int i=0; i < jsonArray.length(); i++) {

                    String id = "";
                    String name = "";
                    categories = jsonArray.getJSONObject(i);

                    for(int j=0; j < 1; j++){
                        id = String.valueOf(categories.getInt("id"));
                        name = categories.getString("name");
                    }
                    categoryList.put(id, name);
                }
            }
            catch (Exception e) {
            }

            return "Done";
        }

        public void onProgressUpdate(Integer ... args) {
//            ProgressBar progressBar = findViewById(R.id.lab6_progressBar);
//            progressBar.setVisibility(1);

            Log.i("HTTP", "In onProgressUpdate"); //added this line here just to debug
        }
        //Type3 from doInBackground is the return from doInBackground
        public void onPostExecute(String fromDoInBackground){ //The return value "Done"

            Log.i("In onProgressUpdate", "The key is: " + categoryList);
//            for (String key : categoryList.keySet()) {
//                Log.i("In onProgressUpdate", "The key is: " + key);
//            }

            //all messages have arrived on main processor, doInBackground is finished
            //Set weather image
//            ImageView weatherImage = findViewById(R.id.lab6_currentWeather);
//            weatherImage.setImageBitmap(this.currentWeather);
//            //Set temperature value
//            TextView lab6_temperature = findViewById(R.id.lab6_temperature);
//            lab6_temperature.setText("Temperature: " + Math.round(Float.parseFloat(this.currentTemperature)) + "\u2103");
//            TextView lab6_minTemperature = findViewById(R.id.lab6_minTemperature);
//            lab6_minTemperature.setText("Min temperature: " + Math.round(Float.parseFloat(this.min)) + "\u2103");
//            TextView lab6_maxTemperature = findViewById(R.id.lab6_maxTemperature);
//            lab6_maxTemperature.setText("Max temperature: " + Math.round(Float.parseFloat(this.max)) + "\u2103");
//            TextView lab6_uvRating = findViewById(R.id.lab6_uvRating);
//            lab6_uvRating.setText("UV Rating: " + this.uv);
//
//
//            ProgressBar progressBar = findViewById(R.id.lab6_progressBar);
//            progressBar.setVisibility(View.INVISIBLE);
        }


    }
}