package com.example.quizmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.quizmaster.Utils.Preference;
import com.example.quizmaster.controller.AppController;
import com.example.quizmaster.data.ListAsyncResponse;
import com.example.quizmaster.data.QuestionBank;
import com.example.quizmaster.model.Question;
import com.example.quizmaster.model.Score;

import org.json.JSONArray;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTV;
    private TextView questionCount;
    private Button trueBtn;
    private Button falseBtn;
    private ImageButton nextBtn;
    private ImageButton prevBtn;
    private int questionIndex = 0;
    private List<Question> questionList;
    private CardView cardView;
    private TextView scoreTV;
    private TextView highScoreTV;
    private Score score;
    private int scoreCounter = 0;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionCount = findViewById(R.id.question_counter);
        questionTV = findViewById(R.id.question_tv);
        trueBtn = findViewById(R.id.true_btn);
        falseBtn = findViewById(R.id.false_btn);
        nextBtn = findViewById(R.id.next_btn);
        prevBtn = findViewById(R.id.prev_btn);
        cardView = findViewById(R.id.cardView);
        scoreTV = findViewById(R.id.score_tv);
        highScoreTV = findViewById(R.id.highScore_TV);
        preference = new Preference(MainActivity.this);
        score = new Score();
        trueBtn.setOnClickListener(this);
        falseBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);

        questionIndex = preference.getState();
        highScoreTV.setText(MessageFormat.format("Highest Score: {0}", String.valueOf(preference.getHighScore())));

        scoreTV.setText(MessageFormat.format("Your score: {0}", String.valueOf(score.getScore())));
        questionList = new QuestionBank().getQuestions(new ListAsyncResponse() {
            @Override
            public void dataProcessed(ArrayList<Question> questionArrayList) {
                //Log.d("main", "dataProcessed: "+questionArrayList);
                questionTV.setText(questionArrayList.get(questionIndex).toString());
                questionCount.setText(questionIndex + " / "+questionArrayList.size());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.true_btn:
                checkAnswer(true);
                break;

            case R.id.false_btn:
                checkAnswer(false);
                break;

            case R.id.next_btn:
                questionIndex = (questionIndex+1)% questionList.size();
                update();

                break;
            case R.id.prev_btn:
                if(questionIndex>0){
                    questionIndex = (questionIndex-1)%questionList.size();
                    update();


                }

                break;
        }
    }

    private void checkAnswer(boolean optChosen) {
        boolean ansVerify = questionList.get(questionIndex).getAnswerVerify();
        if(optChosen == ansVerify){

            update();
            correctAnsAnim();
            addScore();
            //Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();

        }
        else {
            update();
            wrongAnsAnim();
            minusScore();
            //cardView.setCardBackgroundColor(Color.RED);

        }     //Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();

    }

    private void wrongAnsAnim() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(2);
        alphaAnimation.setRepeatMode(Animation.RELATIVE_TO_PARENT);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                questionIndex = (questionIndex+1)% questionList.size();
                update();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void correctAnsAnim() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                questionIndex = (questionIndex+1)% questionList.size();
                update();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addScore() {
        scoreCounter+= 10;
        score.setScore(scoreCounter);
        scoreTV.setText(MessageFormat.format("Your score: {0}", String.valueOf(score.getScore())));
    }

    private void minusScore() {

        if(scoreCounter>0){
            scoreCounter-= 10;
            score.setScore(scoreCounter);
            scoreTV.setText(MessageFormat.format("Your score: {0}", String.valueOf(score.getScore())));
        }
        else{
            score.setScore(0);
            scoreTV.setText(MessageFormat.format("Your score: {0}", String.valueOf(score.getScore())));
        }


    }

    private void update() {
        cardView.setCardBackgroundColor(Color.WHITE);
        questionTV.setText(questionList.get(questionIndex).getAnswer());
        questionCount.setText(questionIndex + " / "+questionList.size());
    }

    @Override
    protected void onPause() {
        preference.saveHighScore(score.getScore());
        preference.setState(questionIndex);
        super.onPause();
    }


}

