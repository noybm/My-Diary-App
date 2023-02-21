package com.app.mydiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeScreenActivity extends AppCompatActivity {


    CardView cardTodo,cardMotivation,cardDiary;
    FirebaseAuth auth;
    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        auth = FirebaseAuth.getInstance();
        cardDiary = findViewById(R.id.cardDiary);
        cardTodo = findViewById(R.id.cardTodo);
        cardMotivation = findViewById(R.id.cardMotivation);
        logout = findViewById(R.id.logout);


        if(auth.getCurrentUser()==null){

            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }

        cardMotivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DailyMotivationActivity.class));

            }
        });


        cardTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TodoActivity.class));

            }
        });

        cardDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(getApplicationContext(),DiaryActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                auth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();

            }
        });
    }
}