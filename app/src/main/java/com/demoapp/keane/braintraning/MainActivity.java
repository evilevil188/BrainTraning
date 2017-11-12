package com.demoapp.keane.braintraning;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView myTimer, myResult, myQueention , mySource;
    Button myAns1, myAns2, myAns3, myAns4;
    private Math m;
    private String tureAns = "5";
    private CountDownTimer countDownTimer;
    private StringBuilder sb = new StringBuilder(10);       //CoundDownTimer
    private StringBuilder ans = new StringBuilder(10);      //記錄答案
    private Boolean isPushButton = false;                   //是否已按按鈕了？
    private int queentionNumber = 0;                     //目前總題數
    private int rightNumber =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTimer = (TextView) findViewById(R.id.myTimer);
        myAns1 = (Button) findViewById(R.id.myAns1button);
        myAns2 = (Button) findViewById(R.id.myAns2button);
        myAns3 = (Button) findViewById(R.id.myAns3button);
        myAns4 = (Button) findViewById(R.id.myAns4button);
        myQueention = (TextView) findViewById(R.id.myQueention);
        mySource = (TextView) findViewById(R.id.mySource);
        myResult = (TextView) findViewById(R.id.myResult);
        myResult.setVisibility(View.INVISIBLE); //消失
        controlTimer();//開始倒數
    }

    public void setSource(){
        StringBuilder source = new StringBuilder(10);
        source.append(rightNumber).append("/").append(queentionNumber);
        mySource.setText(source.toString());
    }
    //計時器
    public void controlTimer() {
        countDownTimer = new CountDownTimer(30000 + 200, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sb.append(millisUntilFinished / 1000).append("s");
                myTimer.setText(sb.toString()); //setText 不可是int會報錯
                sb.setLength(0);
            }

            @Override
            public void onFinish() {
                sb.append(0);
                buttonEnble(false); //時間結束，按鈕不給按
                showResult();
                myTimer.setText(sb.toString());
            }
        }.start();
    }

    //顯示答題數、答對的題數
    public void showResult(){
        //利用Toast的靜態函式makeText來建立Toast物件
        Toast toast = Toast.makeText(MainActivity.this,
                "total = " + queentionNumber +" \nRight = " + rightNumber, Toast.LENGTH_LONG);
        //顯示Toast
        toast.show();
    }

    //處理按鈕事件
    public void processOnClick(View v, Button btn) {
        if (!isPushButton) {
            isPushButton = true;
            ans.setLength(0);
            ans.append(btn.getText());
            Log.i("wen " , "ans = " + ans.toString());
            buttonEnble(false);
            ans(v);
        }
    }

    //在xml裡的onClick()裡面若有設置a方法，就需要有a(View v) 方法實作
    public void sendAns1(View v) {
        processOnClick(v, myAns1);
    }

    public void sendAns2(View v) {
        processOnClick(v, myAns2);
    }

    public void sendAns3(View v) {
        processOnClick(v, myAns3);
    }

    public void sendAns4(View v) {
        processOnClick(v, myAns4);
    }

    //比對答案
    public void ans(View v) {
        Log.i("wen " , "tureAns = " + tureAns+" ,  ans = " + ans.toString());
        if (tureAns.equals(ans.toString())) {
            myResult.setText("Right");
            rightNumber++;
        } else {
            myResult.setText("Wrong");
        }
        myResult.setVisibility(v.VISIBLE);//顯示
        newQueention();
        newChooseAns();
        queentionNumber++;
        setSource();
        isPushButton = false;
        buttonEnble(true);
    }

    //當其中有一按鈕被按下，其它按鈕就不能按了
    public void buttonEnble(Boolean enable) {
        myAns1.setEnabled(enable);
        myAns2.setEnabled(enable);
        myAns3.setEnabled(enable);
        myAns4.setEnabled(enable);
    }

    //亂數產生新題目 queention
    public void newQueention() {
        StringBuilder sbb = new StringBuilder(10);
        int x = (int) (m.random() * 98 + 1);
        int y = (int) (m.random() * 98 + 1);
        if (x > y) {
            sbb.append(x).append("-").append(y);
            tureAns = Integer.toString(x - y);

        } else {
            sbb.append(x).append("+").append(y);
            tureAns = Integer.toString(x + y);
        }
        myQueention.setText(sbb.toString());
    }

    //顯示正確可選答案 ans 
    public void newChooseAns() {
        int x = (int) (m.random() * 4 + 1); //1 - 4變數
        while (x == 0) {
            x = (int) (m.random() * 4 + 1);
        }
        switch (x) {
            case 1:
                myAns1.setText(tureAns);
                setOtherButtonText(myAns2,myAns3,myAns4 );
                break;
            case 2:
                myAns2.setText(tureAns);
                setOtherButtonText(myAns1,myAns3,myAns4 );
                break;
            case 3:
                myAns3.setText(tureAns);
                setOtherButtonText(myAns2,myAns1,myAns4 );
                break;
            case 4:
                myAns4.setText(tureAns);
                setOtherButtonText(myAns2,myAns3,myAns1 );
                break;
            default:
                break;
        }
    }

    //顯示除正確答案外，其它新的可選答案
    public void setOtherButtonText(Button btnA, Button btnB, Button btnC) {
        int xx = 0; //(int) (m.random() * 99 + 1);
        int r;  //產生亂數的範圍
        if(tureAns.length() == 2){
            r = 99;
        }else{
            r= 199;
        }
        for (int i = 0; i < 3; i++) {
            xx = (int) (m.random() * r + 1);
            while (tureAns.equals(Integer.toString(xx)) ) {
                xx = (int) (m.random() *r + 1);
            }
            switch (i){
                case 0:
                    btnA.setText(Integer.toString(xx));
                    break;
                case 1:
                    btnB.setText(Integer.toString(xx));
                    break;
                case 2:
                    btnC.setText(Integer.toString(xx));
                    break;
                default:
                    break;
            }
        }
    }


}
