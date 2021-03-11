package com.cst2335.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class TriviaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] category = {"Any Category", "Science", "Technology", "Sports", "Cars"};
    URLBuilder urlBuilder = new URLBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());

        urlBuilder.setCategory(12);
        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());

        urlBuilder.setQuantity(5);
        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());

        urlBuilder.setType(2);
        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());

        urlBuilder.setDifficulty(2);
        Log.i("ACTIVITY_TRIVIA: ",  "The URL is: " + urlBuilder.getURL());

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
            any.setBackgroundColor(Color.parseColor("#cad3fe"));
            easy.setBackgroundColor(0);
            medium.setBackgroundColor(0);
            hard.setBackgroundColor(0);
        });

        //On click listener for easy button
        easy.setOnClickListener(click -> {
            urlBuilder.setDifficulty(1);
            Toast.makeText(this, "Easy", Toast.LENGTH_SHORT).show();
            easy.setBackgroundColor(Color.parseColor("#cad3fe"));
            any.setBackgroundColor(0);
            medium.setBackgroundColor(0);
            hard.setBackgroundColor(0);
        });

        //On click listener for medium button
        medium.setOnClickListener(click -> {
            urlBuilder.setDifficulty(2);
            Toast.makeText(this, "Medium", Toast.LENGTH_SHORT).show();
            medium.setBackgroundColor(Color.parseColor("#cad3fe"));
            any.setBackgroundColor(0);
            easy.setBackgroundColor(0);
            hard.setBackgroundColor(0);
        });

        //On click listener for hard button
        hard.setOnClickListener(click -> {
            urlBuilder.setDifficulty(3);
            Toast.makeText(this, "Hard", Toast.LENGTH_SHORT).show();
            hard.setBackgroundColor(Color.parseColor("#cad3fe"));
            any.setBackgroundColor(0);
            easy.setBackgroundColor(0);
            medium.setBackgroundColor(0);
        });

        // ******Creating OnclickListener that leads to TriviaPlayActivity***************
        Button startTrivia =  findViewById(R.id.start);
        Intent goTriviaPlay = new Intent(this, TriviaPlayActivity.class);
        startTrivia.setOnClickListener( click -> startActivity(goTriviaPlay));
        // ******End of OnclickListener that leads to TriviaActivity***************

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            //do nothing
        }else{
            //Show a Toast message whenever a category is selected
            Toast.makeText(this, category[position], Toast.LENGTH_SHORT).show();
            urlBuilder.setCategory(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void imageButtonClearBackground(){

    }

    private class URLBuilder {
        private final String baseAPIURL = "https://opentdb.com/api.php?";
        private int quantity;
        private int category;
        private final String[] difficulty = {"Any", "Easy", "Medium", "Hard"};
        private int difficultyChoice;
        private final String[] typeOfQuestion = {"Any", "Multiple Choice", "True/False"};
        private int typeChoice;

        public URLBuilder() {
            this.quantity = 10;
            this.category = 0;
            this.difficultyChoice = 0;
            this.typeChoice = 0;
        }


        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public void setType(int typeChoice) {
            this.typeChoice = typeChoice;
        }
        public void setDifficulty(int difficultyChoice) {
            this.difficultyChoice = difficultyChoice;
        }

        public String getURL(){
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
            if(this.typeChoice > 0 && this.typeChoice <= 2) {
                builder.append("&");
                if(this.typeChoice == 1){
                    builder.append("type=" + "multiple");
                }else {
                    builder.append("type=" + "boolean");
                }

            }
            return builder.toString();
        }

    }
}