package com.example.android.androidbasicsdartcounter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aligma on 3/04/2017.
 */

public class Game {

    private final int STARTING_SCORE = 501;
    private final int PLAYERS = 2;
    private final int DARTS_PER_PLAYER = 3;
    final int DARTS_PER_ROUND = PLAYERS * DARTS_PER_PLAYER;

    final int SINGLE = 1;
    final int DOUBLE = 2;
    final int TRIPLE = 3;
    final int ZERO = 0;
    final int TWENTY = 20;
    final int BULLSEYE = 25;

    public List<Shot> Shots;
    public int CurrentMultiplier = SINGLE;
    public boolean Victory = false;


    public Game()
    {
        Shots = new ArrayList<>();
    }


    public int getPlayerScore(int playerNumber)
    {
        int score = STARTING_SCORE;
        for (int i = 0; i < this.Shots.size(); i++) {
            Shot shot = this.Shots.get(i);
            if (shot.Player == playerNumber) {
                score -= shot.Points();
            }
        }
        return score;
    }

    public int getPlayerTurn()
    {
        int shotCount = this.Shots.size();
        return ((shotCount % DARTS_PER_ROUND) < DARTS_PER_PLAYER) ? 1 : 2;
    }

    // bust the rest of the player's turn
    public void bust(int player) {
        Shot bustShot = new Shot();
        bustShot.Player = player;
        bustShot.Value = 0;
        bustShot.Multiplier = SINGLE;
        bustShot.Bust = true;

        while (this.Shots.size() % DARTS_PER_PLAYER != 0) {
            this.Shots.add(bustShot);
        }
    }

    public int subtotal(int player)
    {
        int subtotal = 0;
        int roundsCompleted = this.Shots.size() / DARTS_PER_ROUND;
        int startAt = roundsCompleted * DARTS_PER_ROUND + (DARTS_PER_PLAYER * (player - 1));

        for (int i = startAt; i < startAt + 3; i++)
        {
            if (Shots.size() > i)
                subtotal += Shots.get(i).Points();
        }

        return subtotal;
    }

    public Shot addShot(int points)
    {
        Shot shot = new Shot();
        shot.Player = getPlayerTurn();
        shot.Value = points;
        shot.Multiplier = CurrentMultiplier;
        shot.Bust = (shot.Player == 1 && (getPlayerScore(1) < points * CurrentMultiplier)) ||
                (shot.Player == 2 && (getPlayerScore(2) < points * CurrentMultiplier));

        Shots.add(shot);

        if (shot.Bust)
            bust(shot.Player);

        return shot;
    }

    public int VictoryCheck()
    {
        if (Shots.size() == 0)
            return 0;

        int lastShotNumber = (Shots.size() - 1) % DARTS_PER_ROUND;
        int player = lastShotNumber < DARTS_PER_PLAYER ? 1 : 2;

        if (getPlayerScore(player) == 0)
        {
            Victory = true;
            return player;
        }

        return 0;
    }

    public void undo()
    {
        Victory = false;

        if (Shots.size() == 0)
            return;

        // if the last shot is a bust, keep removing until it's not a bust anymore.
        // otherwise just remove one.

        if (Shots.get(Shots.size() - 1).Bust && Shots.get(Shots.size() - 1).Value == 0) {
            while (Shots.get(Shots.size() - 1).Bust && Shots.get(Shots.size() - 1).Value == 0) {
                Shots.remove(Shots.size() - 1);
            }
        }
        Shots.remove(Shots.size() - 1);

    }
}
