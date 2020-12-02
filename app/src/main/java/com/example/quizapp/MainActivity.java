package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Model.Question;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3, b4;
    TextView t1_question, timerTxt;
    public int total= 0;
    int wrong = 0;
    int correct = 0;
    //обьявили глобальный обьект для работы с нашей базой
    DatabaseReference reference;
    //реклама
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button)findViewById(R.id.button1);
        b2 = (Button)findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);
        b4 = (Button)findViewById(R.id.button4);

        t1_question = (TextView)findViewById(R.id.questionsTxt);
        timerTxt = (TextView)findViewById(R.id.timerTxt);
        //нам нужно обновлять вопросы
        updateQuestion();
        reverseTimer(30,timerTxt);

        //реклама
        MobileAds.initialize(this, initializationStatus -> {
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    //создали функцию обновления вопросов
    private void updateQuestion() {
        //работаем с нашим total. инкрементируем и посредством этого
        // доходим до значения > 5 в случае которого
        //мы открываем ResultActivity
        total++;
        if (total > 5)
        {
             Intent i = new Intent(MainActivity.this,ResultActivity.class);
             i.putExtra("total", String.valueOf(total - 1));
            i.putExtra("correct", String.valueOf(correct));
            i.putExtra("wrong", String.valueOf(wrong));
             startActivity(i);
        }
        else {
            //здесь мы работаем с нашей базой "Questions"
            // и пока total не > 5 мы работаем с ветками +-1, +-2 и тд в нашей фаербэйз
            reference = FirebaseDatabase.getInstance().getReference().child("Questions").child(String.valueOf(total));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Question question = snapshot.getValue(Question.class);

                    t1_question.setText(question.getQuestion());
                    b1.setText(question.getOption1());
                    b2.setText(question.getOption2());
                    b3.setText(question.getOption3());
                    b4.setText(question.getOption4());

                    //проверяем нажал ли пользователь на кнопку option 1, и верный ли был вариант
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //если вариант вопроса = ответу
                            if (b1.getText().toString().equals(question.getAnswer()))
                            {

                                //то мы отображаем зелёным цветом верный ответ
                                b1.setBackgroundColor(Color.GREEN);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //и инкрементируем верный ответ
                                    correct++;
                                    b1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        // и апдейтим на новый вопрос
                                    updateQuestion();
                                    }
                                },1500);
                            }
                            else
                            {

                                //в противном же случае инкрементируем неверные ответы
                                // и окрашиваем кнопку в красный

                                wrong++;
                                b1.setBackgroundColor(Color.RED);
                                // и поверяем следующую кнопку на правильность
                                if (b2.getText().toString().equals(question.getAnswer()))
                                {
                                    b2.setBackgroundColor(Color.GREEN);
                                }
                                else if (b3.getText().toString().equals(question.getAnswer()))
                                {

                                    b3.setBackgroundColor(Color.GREEN);
                                }
                              else if (b4.getText().toString().equals(question.getAnswer()))
                                {
                                    b4.setBackgroundColor(Color.GREEN);

                                }

                              Handler handler = new Handler();
                              handler.postDelayed(new Runnable() {
                                  @Override
                                  public void run() {
                                      //мы возвращаем кнопкам изначальный цвет
                                      //и апдейтим вопрос
                                      b1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                      b2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                      b3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                      b4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                      updateQuestion();
                                  }
                              },1500);


                            }
                        }
                    });


                    //повторяем такой код для каждой кнопки только меняя b1, b2, b3, b4
                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (b2.getText().toString().equals(question.getAnswer()))
                            {

                                b2.setBackgroundColor(Color.GREEN);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        b2.setBackgroundColor(Color.parseColor("#03A9F4"));

                                        updateQuestion();
                                    }
                                },1500);
                            }
                            else
                            {

                                wrong++;
                                b2.setBackgroundColor(Color.RED);

                                if (b1.getText().toString().equals(question.getAnswer()))
                                {
                                    b1.setBackgroundColor(Color.GREEN);
                                }
                                else if (b3.getText().toString().equals(question.getAnswer()))
                                {

                                    b3.setBackgroundColor(Color.GREEN);
                                }
                                else if (b4.getText().toString().equals(question.getAnswer()))
                                {
                                    b4.setBackgroundColor(Color.GREEN);

                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        b1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        b2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        b3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        b4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestion();
                                    }
                                },1500);


                            }
                        }


                    });


b3.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (b3.getText().toString().equals(question.getAnswer()))
        {

            b3.setBackgroundColor(Color.GREEN);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    correct++;
                    b3.setBackgroundColor(Color.parseColor("#03A9F4"));

                    updateQuestion();
                }
            },1500);
        }
        else
        {

            wrong++;
            b3.setBackgroundColor(Color.RED);

            if (b1.getText().toString().equals(question.getAnswer()))
            {
                b1.setBackgroundColor(Color.GREEN);
            }
            else if (b2.getText().toString().equals(question.getAnswer()))
            {

                b2.setBackgroundColor(Color.GREEN);
            }
            else if (b4.getText().toString().equals(question.getAnswer()))
            {
                b4.setBackgroundColor(Color.GREEN);

            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    b1.setBackgroundColor(Color.parseColor("#03A9F4"));
                    b2.setBackgroundColor(Color.parseColor("#03A9F4"));
                    b3.setBackgroundColor(Color.parseColor("#03A9F4"));
                    b4.setBackgroundColor(Color.parseColor("#03A9F4"));
                    updateQuestion();
                }
            },1500);


        }
    }

});

               b4.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (b4.getText().toString().equals(question.getAnswer()))
                       {

                           b4.setBackgroundColor(Color.GREEN);
                           Handler handler = new Handler();
                           handler.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   correct++;
                                   b4.setBackgroundColor(Color.parseColor("#03A9F4"));

                                   updateQuestion();
                               }
                           },1500);
                       }
                       else
                       {

                           wrong++;
                           b4.setBackgroundColor(Color.RED);

                           if (b1.getText().toString().equals(question.getAnswer()))
                           {
                               b1.setBackgroundColor(Color.GREEN);
                           }
                           else if (b2.getText().toString().equals(question.getAnswer()))
                           {

                               b2.setBackgroundColor(Color.GREEN);
                           }
                           else if (b3.getText().toString().equals(question.getAnswer()))
                           {
                               b3.setBackgroundColor(Color.GREEN);

                           }

                           Handler handler = new Handler();
                           handler.postDelayed(new Runnable() {
                               @Override
                               public void run() {

                                   b1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                   b2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                   b3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                   b4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                   updateQuestion();
                               }
                           },1500);


                       }
                   }

               });




                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    //мы прописываем таймер
    public void reverseTimer (int seconds,final TextView tv)
    {
        new CountDownTimer(seconds * 1000 + 1000,1000)
        {
            public void onTick (long millisUnitFinished)
            {    //тут мы конвертируем таймер в секунды и минуты
                int seconds = (int) (millisUnitFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText(String.format("%02d",minutes) + ":" + String.format("%02d", seconds));
            }

            //а когда же выйдет время мы выводим ResultActivity

            public void onFinish(){
                tv.setText("Completed");
                Intent myIntent = new Intent(MainActivity.this,ResultActivity.class);
                myIntent.putExtra("total",String.valueOf(total-1));
                myIntent.putExtra("correct",String.valueOf(correct));
                myIntent.putExtra("wrong",String.valueOf(wrong));
                startActivity(myIntent);


            }
        }.start();

    }
}