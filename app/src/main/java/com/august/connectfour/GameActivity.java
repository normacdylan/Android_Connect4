package com.august.connectfour;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.august.connectfour.Bots.AverageBot;
import com.august.connectfour.Bots.BeginnerBot;
import com.august.connectfour.Bots.Bot;
import com.august.connectfour.Bots.IdiotBot;

public class GameActivity extends AppCompatActivity {

    private GameLayout layout;
    private Bot bot;
    private boolean multiPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new GameLayout(this);
        setContentView(layout);

        String mode = getIntent().getStringExtra("MODE");
        setMode(mode);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                if (!layout.isGameOver()) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE && layout.isAboveBoard(y)) {
                        if (multiPlayer || bot.getId()!=layout.getTurn())
                            layout.updateCoinX(x);
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (multiPlayer)
                            layout.dropCoin();
                        else if (!multiPlayer && layout.getTurn()!=bot.getId()) {
                            layout.dropCoin();
                            if (layout.getCurrentColumn()>-1) {
                                layout.update();
                                if (!layout.isGameOver()) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            int move = layout.isLegalMove(bot.makeMove())? bot.makeMove():layout.getAnyFree();
                                            layout.updateCoinHole(move);
                                            layout.dropCoin();
                                        }
                                    }, 800);
                                }
                            }
                        }
                        return true;
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        layout.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout.resume();
    }

    private void setMode(String mode) {
        switch (mode) {
            case "player":
                multiPlayer = true;
                break;
            case "IdiotBot":
                bot = new IdiotBot(layout.getAnalyzer(), 2);
                multiPlayer = false;
                break;
            case "BeginnerBot":
                bot = new BeginnerBot(layout.getAnalyzer(), 2);
                multiPlayer = false;
                break;
            case "AverageBot":
                bot = new AverageBot(layout.getAnalyzer(), 2);
                multiPlayer = false;
                break;
            default:
                multiPlayer = true;
        }
    }
}
