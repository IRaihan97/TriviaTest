package com.example.triviatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.example.triviatest.data.AnswerListAscyncResponse;
import com.example.triviatest.data.QuestionBank;
import com.example.triviatest.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTxt;
    private TextView questionCounter;
    private TextView highestScore;
    private TextView score;
    private Button btnTrue;
    private Button btnFalse;
    private ImageButton btnPrev;
    private ImageButton btnNext;
    private int questionIdx = 0;
    private List<Question> questionList;
    private int Score = 0;
    private int highest;
    private final String MESSAGE_ID = "message_prefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnTrue = findViewById(R.id.btnTrue);
        btnFalse = findViewById(R.id.btnFalse);
        questionTxt = findViewById(R.id.txtCard);
        questionCounter = findViewById(R.id.txtCounter);
        highestScore = findViewById(R.id.highest);
        score = findViewById(R.id.score);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnTrue.setOnClickListener(this);
        btnFalse.setOnClickListener(this);

        score.setText("Current Score: " + Score);

        SharedPreferences getData = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        highest = getData.getInt("Highest", 0);

        highestScore.setText("Highest Score: " + highest);


        questionList = new QuestionBank().getQuestions(new AnswerListAscyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTxt.setText(questionArrayList.get(questionIdx).getAnswer());
                questionCounter.setText(questionIdx + " / " + questionList.size());
                Log.d("List", "processFinished: " + questionArrayList);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPrev:
                if(questionIdx > 0){
                    questionIdx -= 1;
                    UpdateQuestion();
                }
                break;
            case R.id.btnNext:
                questionIdx = (questionIdx + 1) % questionList.size();
                UpdateQuestion();
                break;

            case R.id.btnTrue:
                checkAnswer(true);
                UpdateQuestion();
                break;

            case R.id.btnFalse:
                checkAnswer(false);
                UpdateQuestion();
                break;



        }
    }



    private void checkAnswer(boolean b) {
        boolean answerCheck = questionList.get(questionIdx).isAnswerTrue();
        int toastMsgId = 0;
        if(b == answerCheck){
            toastMsgId = R.string.correct_answer;
            fadeView();




        }
        else{
            toastMsgId = R.string.wrong_answer;
            shake();
        }

        Toast.makeText(MainActivity.this, toastMsgId, Toast.LENGTH_SHORT).show();

    }

    private void UpdateQuestion() {
        String question = questionList.get(questionIdx).getAnswer();
        questionTxt.setText(question);
        questionCounter.setText(questionIdx + " / " + questionList.size());
    }

    private void fadeView(){
        final CardView card = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        card.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                card.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                card.setCardBackgroundColor(Color.WHITE);
                questionIdx = (questionIdx + 1) % questionList.size();
                UpdateQuestion();
                Score++;
                score.setText("Current Score: " + Score);
                highestScoring();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shake(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);

        final CardView card = findViewById(R.id.cardView);
        card.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                card.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                card.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void highestScoring(){
        SharedPreferences preferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(Score > highest){
            highest = Score;
        }
        editor.putInt("Highest", highest);
        editor.apply();
    }


}
