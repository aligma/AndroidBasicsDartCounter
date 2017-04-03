package com.example.android.androidbasicsdartcounter;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new Game();
        display();
    }

    /* interactions from the interface */
    public void onScoreClick(View view) {
        score(Integer.parseInt(view.getTag().toString()));
    }

    public void score(int points) {

        if (game.Victory)
            return;

        game.addShot(points);

        clearMultipliers();
        display();

        int victoryCheck = game.VictoryCheck();
        if (victoryCheck == 1)
        {
            Toast.makeText(this, "Player A Wins! Congratulations!", Toast.LENGTH_LONG).show();
        }
        else if (victoryCheck == 2)
        {
            Toast.makeText(this, "Player B Wins! Congratulations!", Toast.LENGTH_LONG).show();
        }
    }

    public void setDoubleButtonState(boolean enabled) {
        TextView doubleButton = (Button) findViewById(R.id.double_button);
        doubleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                (enabled) ? R.color.colorSpecialSelectedButton : R.color.colorSpecialButton));
    }

    public void setTripleButtonState(boolean enabled) {
        TextView tripleButton = (Button) findViewById(R.id.triple_button);
        tripleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                (enabled) ? R.color.colorSpecialSelectedButton : R.color.colorSpecialButton));
    }

    public void onDoubleButtonClick(View view) {
        if (game.CurrentMultiplier == game.DOUBLE) {
            setDoubleButtonState(false);
            game.CurrentMultiplier = game.SINGLE;
        } else {
            setDoubleButtonState(true);
            setTripleButtonState(false);
            game.CurrentMultiplier = game.DOUBLE;
        }
    }

    public void onTripleButtonClick(View view) {
        if (game.CurrentMultiplier == game.TRIPLE) {
            setTripleButtonState(false);
            game.CurrentMultiplier = game.SINGLE;
        } else {
            setTripleButtonState(true);
            setDoubleButtonState(false);
            game.CurrentMultiplier = game.TRIPLE;
        }
    }

    public void onTriple20ButtonClick(View view) {
        game.CurrentMultiplier = game.TRIPLE;
        score(game.TWENTY);
    }

    public void onBullButtonClick(View view) {
        if (game.CurrentMultiplier == game.TRIPLE) {
            Toast.makeText(this, "No such thing as a triple bullseye.", Toast.LENGTH_SHORT).show();
            return;
        }
        score(game.BULLSEYE);
    }

    private void clearMultipliers() {
        game.CurrentMultiplier = game.SINGLE;
        updateMultipliers();
    }

    private void updateMultipliers()
    {
        setDoubleButtonState(game.CurrentMultiplier == game.DOUBLE);
        setTripleButtonState(game.CurrentMultiplier == game.TRIPLE);
    }

    public void onZeroButtonClick(View view) {
        clearMultipliers();
        score(game.ZERO);
    }

    public void onUndoButtonClick(View view) {
        game.undo();
        clearMultipliers();
        display();
    }


    public void displayShot(int shotIndex, int viewId) {
        TextView textView = (TextView) findViewById(viewId);

        String s;

        if (game.Shots.size() > shotIndex) {
            Shot shot = game.Shots.get(shotIndex);
            s = shot.Bust ? "X" : String.valueOf(shot.Points());
        } else
            s = "0";

        textView.setText(s);

        // this text view represent the shot that is about to take place, so highlight it
        textView.setTextColor((ContextCompat.getColor(getApplicationContext(),
                (game.Shots.size() % game.DARTS_PER_ROUND == shotIndex % 6) ? R.color.colorNumberHighight : R.color.colorNumberNormal)));
    }


    public void displaySubtotal(int player, int view)
    {
        TextView playerASubtotalView = (TextView) findViewById(view);
        playerASubtotalView.setText(String.valueOf(game.subtotal(player)));
    }

    public void displayScore(int player, int view)
    {
        TextView scoreView = (TextView) findViewById(view);
        scoreView.setText(String.valueOf(game.getPlayerScore(player)));
    }

    public void highlightArea(int player, int view, boolean highlight)
    {
        LinearLayout area = (LinearLayout) findViewById(view);
        area.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                highlight ? R.color.currentPlayerBackground : R.color.otherPlayerBackground));
    }

    public void display() {
        int roundsCompleted = game.Shots.size() / game.DARTS_PER_ROUND;
        displayShot(roundsCompleted * game.DARTS_PER_ROUND, R.id.player_a_first);
        displayShot(roundsCompleted * game.DARTS_PER_ROUND + 1, R.id.player_a_second);
        displayShot(roundsCompleted * game.DARTS_PER_ROUND + 2, R.id.player_a_third);
        displayShot(roundsCompleted * game.DARTS_PER_ROUND + 3, R.id.player_b_first);
        displayShot(roundsCompleted * game.DARTS_PER_ROUND + 4, R.id.player_b_second);
        displayShot(roundsCompleted * game.DARTS_PER_ROUND + 5, R.id.player_b_third);

        displayScore(1, R.id.player_a_score);
        displaySubtotal(1, R.id.player_a_subtotal);
        highlightArea(1, R.id.player_a_area, game.getPlayerTurn() == 1);

        displayScore(2, R.id.player_b_score);
        displaySubtotal(2, R.id.player_b_subtotal);
        highlightArea(2, R.id.player_b_area, game.getPlayerTurn() == 2);
    }

    public void reset(View view) {
        game.Victory = false;
        game.Shots.clear();
        setTripleButtonState(false);
        setDoubleButtonState(false);
        display();
    }
}

