package com.app.mydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyMotivationActivity extends AppCompatActivity {


    TextView item_quotes_quote,tvDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_motivation);

        item_quotes_quote = findViewById(R.id.item_quotes_quote);
        tvDate = findViewById(R.id.tvDate);

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText(date);

        String dateDay = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());

        
        int dailyQuote = Integer.parseInt(dateDay);

        final String[] proper_noun = {"All our dreams can come true, if we have the courage to pursue them.",
                "The time ypu spend chasing your goals is never wasted",
                "My mind is clearing and i am in control",
                "Your future is worthy of your hustle.",
                "It’s hard to beat a person who never gives up.",
                "I see my struggles as opportunities to grow",
                "If people are doubting how far you can go, go so far that you can’t hear them anymore.",
                "My life is a gift and i appreciate everything i have",
                "i am not perfect, but always myself.",
                "Everything you can imagine is real.",
                "Do one thing every day that scares you.",
                "Do what you feel in your heart to be right",
                "Happiness is not something ready made. It comes from your own actions.",
                "I only compare myself to myself",
                "You can either experience the pain of discipline or the pain of regret. The choice is yours.",
                "Impossible is just an opinion",
                "If something is important enough, even if the odds are stacked against you, you should still do it",
                "Hold the vision, trust the process",
                "Don’t be afraid to give up the good to go for the great",
                "I am in the right place, at the right time, doing the right thing.",
                "One day or day one. You decide.",
                "There are no obstacles i can't overcome.",
                "Keep your eyes on the stars, and your feet on the ground",
                "I’m alive, motivated and ready to slay the day ",
                "The hard days are what make you stronger.",
                "You can waste your lives drawing lines. Or you can live your life crossing them",
                "In a gentle way, you can shake the world",
                "If opportunity doesn’t knock, build a door",
                "Work hard in silence, let your success be the noise",
                "Hard work beats talent when talent doesn’t work hard.",
                "The best way to appreciate your job is to imagine yourself without one",
                "Never stop doing your best just because someone doesn’t give you credit.",
                "Never allow a person to tell you no who doesn’t have the power to say yes",
                "Change is scary. being unhappy is scarier."

        };

        String quotes = proper_noun[dailyQuote];
        Log.e("pii", "onCreate: "+proper_noun.length );

        item_quotes_quote.setText(quotes);



    }
}
