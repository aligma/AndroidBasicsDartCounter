package com.example.android.androidbasicsdartcounter;

/**
 * Created by aligma on 3/04/2017.
 */

public class Shot {
    public int Player; // 1st player = 1, 2nd player = 2
    public int Value; // segment, 0 for a miss or 25 for bull
    public int Multiplier; // 1, 2, 3; 1 for a miss
    public boolean Bust; // did the player bust during this turn?

    public int Points() {
        return (Bust) ? 0 : Value * Multiplier;
    }
}
