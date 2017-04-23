package com.hackfresno17.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class Food extends AppCompatActivity {
    Context context;

    Intent thisIntent;
    TextView mFoodText;
    ImageView mFoodImage;

    Button mVoteUp;
    Button mVoteDown;
    Button mSkip;

    String item;
    FirebaseDatabase database;
    DatabaseReference mRootRef;
    DatabaseReference mRestarRef;
    DatabaseReference mIdRef;
    DatabaseReference mNameRef;
    DatabaseReference mRatings;
    DatabaseReference mRating;
    ValueEventListener postListener;
    View.OnClickListener upListener;
    View.OnClickListener downListener;
    View.OnClickListener skipListener;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imageRef;

    OnSuccessListener imgSuccess;
    OnFailureListener imgFail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        context = this;

        thisIntent = getIntent();
        item =  thisIntent.getExtras().getString("item");
        mFoodText = (TextView)findViewById(R.id.foodName);
        mFoodImage = (ImageView)findViewById(R.id.foodImage);

        mVoteUp = (Button)findViewById(R.id.voteup);
        mVoteDown = (Button)findViewById(R.id.votedown);
        mSkip = (Button)findViewById(R.id.skip);

        database = FirebaseDatabase.getInstance();
        mRootRef = database.getReference();
        mRestarRef = mRootRef.child("Restar");
        mIdRef = mRestarRef.child(item);
        mNameRef = mIdRef.child("name");
        mRatings = mRootRef.child("Ratings");
        mRating = mIdRef.child(item);

        //String name = mNameRef.getValue(String.class);
        //mFoodText.setText(name);

        String fileName = "images/" + item + ".jpg";
        imageRef = storageRef.child(fileName);

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mFoodText.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w( "loadPost:onCancelled", databaseError.toException());
            }
        };
        upListener = new View.OnClickListener() {
            public void onClick(View view) {
                String value = "1";
                mRating.setValue(value);
                //finish();
            }
        };
        downListener = new View.OnClickListener(){
            public void onClick(View view) {
                String value = "-1";
                mRating.setValue(value);
                finish();
            }
        };
        skipListener = new View.OnClickListener(){
            public void onClick(View view) {
                Toast.makeText(Food.this, "Skip", Toast.LENGTH_SHORT).show();
                //finish();
            }
        };

        imgSuccess = new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri.toString()).into(mFoodImage);

            }
        };

        imgFail = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception){
                //Handle errors
            }
        };
    }

    protected void onStart() {
        super.onStart();
        mNameRef.addValueEventListener(postListener);

        mVoteUp.setOnClickListener(upListener);
        mVoteDown.setOnClickListener(downListener);
        mSkip.setOnClickListener(skipListener);

        imageRef.getDownloadUrl().addOnSuccessListener(imgSuccess).addOnFailureListener(imgFail);
        //mFoodImage.setImageURI();
    }
}
