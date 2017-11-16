package com.demoapp.keane.braintraning;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button myStartBtn;
    Button playAgainButton;
    RelativeLayout gameRelativeLayout;
    TextView sumTextView;
    TextView timerTextView;
    TextView pointsTextView;
    int locationOfCorrectAnswer = 0;
    int correctAnswer = 0;
    StringBuilder tempSb = new StringBuilder(10);
    ArrayList<Integer> answerArray = new ArrayList<Integer>();
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    int score = 0;
    int numberOfQuestions = 0;
    TextView resultTextView;
    CountDownTimer countDownTimer;


    //    View.VISIBLE--->可見
//    View.INVISIBLE--->不可見，但這個View仍然會佔用在xml檔中所分配的佈局空間，不重新layout
//    View.GONE---->不可見，但這個View在ViewGroup中不保留位置，會重新layout，不再佔用空間，那後面的view就會取代他的位置，
    public void start(View view) {
        myStartBtn.setVisibility(View.INVISIBLE);
        gameRelativeLayout.setVisibility(RelativeLayout.VISIBLE);
        playAgain(findViewById(R.id.playAgainButton));
        // 也可執行
        //        myStartBtn.setVisibility(view.INVISIBLE);
        //        gameRelativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myStartBtn = (Button) findViewById(R.id.myStrartButton);
        gameRelativeLayout = (RelativeLayout) findViewById(R.id.gameRelativeLayout);
        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        sumTextView = (TextView) findViewById(R.id.sumTextView);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        pointsTextView = (TextView) findViewById(R.id.pointsTextView);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        resultTextView = (TextView) findViewById(R.id.resultTextView);


    }

    //更新button顯示文字
    public void changeButtonText() {
        button0.setText(Integer.toString(answerArray.get(0)));
        button1.setText(Integer.toString(answerArray.get(1)));
        button2.setText(Integer.toString(answerArray.get(2)));
        button3.setText(Integer.toString(answerArray.get(3)));
    }

    //答案亂數的範圍
    public int answerRange() {
        int randomRange;
        if (correctAnswer > 99) {
            randomRange = 199;
        } else {
            randomRange = 99;
        }
        return randomRange;
    }

    //產生新的答案
    public void generateAnswer(Random random) {
        int incorrectAnswer = 0; //假的答案
        locationOfCorrectAnswer = random.nextInt(4);//隨機出1-4，並放入locationOfCorrectAnswer
        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrectAnswer) {
                answerArray.add(correctAnswer); //將正確答案放入該按鈕中locationOfCorrectAnswer
            } else {
                incorrectAnswer = random.nextInt(answerRange());
                while (incorrectAnswer == correctAnswer) {        //判斷隨機取假答案，是否與真答案相同
                    incorrectAnswer = random.nextInt(answerRange());//相同則再重取一次
                }
                answerArray.add(incorrectAnswer);
            }
        }
        changeButtonText();
        answerArray.clear(); //重置
        // 不重置Array會一直變長且因為程式寫照死。
        // 只更新button文字是array.get(0)～array.get(3)
    }

    //產生新的問題
    public void generateQuestion() {
        Random rand = new Random();
        int x = rand.nextInt(99);
        int y = rand.nextInt(99);
        if (x > y) {
            tempSb.append(x).append("+").append(y);
            correctAnswer = x + y;
        } else {
            tempSb.append(y).append("-").append(x);
            correctAnswer = y - x;
        }
        Log.i("generateQuestion()", "correctAnswer= " + correctAnswer);
        sumTextView.setText(tempSb.toString());
        tempSb.setLength(0);
        generateAnswer(rand);
    }

    //按下四個按鈕其中之一後的事件處理
    public void chooseAnswer(View view) {
        Log.i("chooseAnswer", "locationOfCorrectAnswer= " + locationOfCorrectAnswer +
                "\nview.getTag().toString()=" + view.getTag().toString());
        //用view.getTag().toString()抓使用者按下那個按鈕
        //判斷是否按下有正確答案的按鈕
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
            score++;
            resultTextView.setText("Corrent");
        } else {
            Log.i("Wrong", "wrong");
            resultTextView.setText("Wrong");
        }
        numberOfQuestions++;
        tempSb.append(score).append(" / ").append(numberOfQuestions);
        pointsTextView.setText(tempSb.toString());
        tempSb.setLength(0);
        generateQuestion();
    }

    public void buttonEnable(boolean enable) {
        button0.setEnabled(enable);
        button1.setEnabled(enable);
        button2.setEnabled(enable);
        button3.setEnabled(enable);
    }

    public void reSet(View view) {
        score = 0;
        numberOfQuestions = 0;
        timerTextView.setText("30s");
        pointsTextView.setText("0 / 0");
        resultTextView.setText("");
        playAgainButton.setVisibility(view.INVISIBLE);
        buttonEnable(true);
    }

    public void playAgain(View view) {
        reSet(view);
        generateQuestion();
        countDownTimer = new CountDownTimer(30200, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("onTick()", "mill = " + millisUntilFinished);
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                buttonEnable(false);
                playAgainButton.setVisibility(View.VISIBLE);
                timerTextView.setText("0s");
                resultTextView.setText("Your score:" + Integer.toString(score) + "/"
                        + Integer.toString(numberOfQuestions));
            }
        }.start();
    }
}
