package com.example.nikola.natgeoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.nikola.natgeoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_IS_SHOWN = "com.example.nikola.natgeoquiz.answer_is_shown";
    public static final String KEY_SAVED = "index_saved";
    public static final String TAG = "CheatActivity";
    private boolean mAnswerIsTrue;
    private TextView mTextViewAnswer;
    Button showAnswerButton;
    TextView mTextViewSdkVersion;

    public static Intent onNewIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }


    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_IS_SHOWN, false);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);


        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mTextViewAnswer = (TextView) findViewById(R.id.answer_txt_view);

        showAnswerButton = (Button) findViewById(R.id.show_answer_button);
        showAnswerButton.setOnClickListener(this);

        mTextViewSdkVersion =  (TextView) findViewById(R.id.txt_version);
        mTextViewSdkVersion.setText("API Level" + " " + String.valueOf(Build.VERSION.SDK_INT));

    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        switch (b.getId()) {
            case R.id.show_answer_button:
                if (mAnswerIsTrue) {
                    mTextViewAnswer.setText(R.string.true_btn);
                } else {
                    mTextViewAnswer.setText(R.string.false_btn);
                }
                setAnswerIsShown(true);

                int cx = showAnswerButton.getWidth() / 2;
                int cy = showAnswerButton.getHeight() / 2;
                float radius = showAnswerButton.getWidth();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Animator anim = ViewAnimationUtils.createCircularReveal(showAnswerButton, cx, cy, radius, 0);

                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            showAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    showAnswerButton.setVisibility(View.INVISIBLE);
                }
        }
    }


    private void setAnswerIsShown(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_IS_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);

    }


}

