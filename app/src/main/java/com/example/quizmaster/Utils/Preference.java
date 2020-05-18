package com.example.quizmaster.Utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class Preference {
    private SharedPreferences preferences;
    private int lastScore;

    public Preference(Activity activity) {
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighScore(int score){
        int currentScore = score;
         lastScore = preferences.getInt("highScore",0);

        if(currentScore>lastScore){

            preferences.edit().putInt("highScore",currentScore).apply();
        }
    }

    public int getHighScore(){
        return preferences.getInt("highScore",0);
    }

    public void setState(int quesIndex){

            preferences.edit().putInt("state",quesIndex).apply();

    }

    public int getState(){
        return preferences.getInt("state",0);
    }

}
