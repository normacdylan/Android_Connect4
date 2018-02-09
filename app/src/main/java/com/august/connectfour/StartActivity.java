package com.august.connectfour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void vsPlayer(View v) {
        Intent playerIntent = new Intent(this, GameActivity.class);
        playerIntent.putExtra("MODE", "player");
        playerIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(playerIntent);
    }

    public void IdiotBot(View v) {
        Intent idiotIntent = new Intent(this, GameActivity.class);
        idiotIntent.putExtra("MODE", "IdiotBot");
        idiotIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(idiotIntent);
    }

    public void beginnerBot(View v) {
        Intent beginnerIntent = new Intent(this, GameActivity.class);
        beginnerIntent.putExtra("MODE", "BeginnerBot");
        beginnerIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(beginnerIntent);
    }

    public void averageBot(View v) {
        Intent averageIntent = new Intent(this, GameActivity.class);
        averageIntent.putExtra("MODE", "AverageBot");
        averageIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(averageIntent);
    }
}
