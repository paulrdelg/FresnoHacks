package com.hackfresno17.myapplication;

import android.content.Intent;
//import android.support.annotation.IdRes;
//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
//import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;

import static com.hackfresno17.myapplication.R.layout.activity_food_list;

//import java.io.FileDescriptor;
//import java.io.PrintWriter;
//import java.util.concurrent.TimeUnit;

public class Questionnaire extends AppCompatActivity {
    ToggleButton dolla1;
    ToggleButton dolla2;
    ToggleButton dolla3;
    ToggleButton dolla4;
    Button start_suggestions;
    Intent intent;
    ArrayList available;
    ArrayList used;
    int max = 8;

    View.OnClickListener handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionare);

        intent = new Intent(this, Service.class);
        startService(intent);

        dolla1 = (ToggleButton)findViewById(R.id.dolla1);
        dolla2 = (ToggleButton)findViewById(R.id.dolla2);
        dolla3 = (ToggleButton)findViewById(R.id.dolla3);
        dolla4 = (ToggleButton)findViewById(R.id.dolla4);
        start_suggestions = (Button)findViewById(R.id.start_suggestions);

        handler = new View.OnClickListener(){
            public void onClick(View view) {
                String myString = "0005";

                if(view == start_suggestions){
                    filterOptions();
                    generateSuggestions();

                    // for Loop
                    //while(used.size() != 0) {
                        //myString = (String)used.get(0);
                        //used.remove(0);
                        Intent intentSuggestions = new Intent(Questionnaire.this, Food.class);
                        intentSuggestions.putExtra("item", myString);
                        Questionnaire.this.startActivity(intentSuggestions);
                        Log.d("Content ", " Main Layout ");

                        //myString = "0001";
                        //Intent intentSuggestions2 = new Intent(Questionnaire.this, Food.class);
                        //intentSuggestions2.putExtra("item", myString);
                        //Questionnaire.this.startActivity(intentSuggestions2);
                        //Log.d("Content ", " Main Layout ");
                    //}
                    //Intent leaderboard;
                    //leaderboard = new Intent(this, foodList.this);
                    //Questionnaire.this.startActivity(leaderboard);

                }
            }
        };
        start_suggestions.setOnClickListener(handler);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if(isChecked) {
            // toggle is enabled
            Toast.makeText(this, "enabled", Toast.LENGTH_SHORT).show();
        }
        else {
            // toggle is disabled
            Toast.makeText(this, "disabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void filterOptions() {
        available = new ArrayList<String>(max);
        String stringToAdd;

        for(int i = 0; i < available.size(); i++){
            stringToAdd = "000" + Integer.toString(i);
            System.out.println(stringToAdd);
            available.add( stringToAdd );
        }
    }

    public void generateSuggestions(){
        used = new ArrayList<String>(max);
        Random rn;
        int answer;
        String myString;

        while(available.size() != 0){
            //Random
            rn = new Random();
            answer = rn.nextInt(max) + 1;
            myString = Integer.toString(answer);

            //Remove from available
            System.out.println(myString);
            available.remove(myString);

            //Add to used
            used.add(0, myString);
        }
    }
}