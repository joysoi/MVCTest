package com.example.nikola.natgeoquiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mTrueButton;
    Button mFalseButton;
    Button mNextButton;
    Button mPrevButton;
    TextView mQuestionsTextView;
    private int mCurrentIndex = 0;
    public static final String TAG = "MainActivity";
    public static final String KEY_INDEX = "index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mTrueButton = (Button) findViewById(R.id.true_btn);
        mTrueButton.setOnClickListener(this);

        mFalseButton = (Button) findViewById(R.id.false_btn);
        mFalseButton.setOnClickListener(this);

        mQuestionsTextView = (TextView) findViewById(R.id.question_txt_view);
        mQuestionsTextView.setOnClickListener(this);

        mNextButton = (Button) findViewById(R.id.next_btn);
        mNextButton.setOnClickListener(this);

        mPrevButton = (Button) findViewById(R.id.prev_btn);
        mPrevButton.setOnClickListener(this);

        updateQuestion();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTxtResId();
        mQuestionsTextView.setText(question);
        buttonStatus(true);

    }

    private void checkIfAnswerTrue(boolean userInput) {
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
        int msgResId;
        if (userInput == answerIsTrue) {
            msgResId = R.string.msg_correct;
        } else {
            msgResId = R.string.msg_incorrect;
        }
        Toast.makeText(MainActivity.this, msgResId, Toast.LENGTH_SHORT).show();
    }

    private void showScore(boolean userInput) {

        boolean answerTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
        float percentage = 0;
        double obtainedScore;
        double totalScore = 6;

        for (int i = 0; i <= mCurrentIndex ; i++) {
            if (userInput == answerTrue) {
                obtainedScore = (answerTrue ? 1 : 0) + i;
                percentage = (float) ((obtainedScore * 100) / totalScore);
            }

            showToast(MainActivity.this, "You scored:" + " " + percentage + "%", Toast.LENGTH_SHORT);
        }

    }

    private void buttonStatus(boolean status) {
        mTrueButton.setEnabled(status);
        mFalseButton.setEnabled(status);
    }

    private void showToast(Context context, String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_america, true),
            new Question(R.string.question_asia, true),
    };

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        switch (b.getId()) {
            case R.id.true_btn:
                checkIfAnswerTrue(true);
                showScore(true);
                buttonStatus(false);
                break;
            case R.id.false_btn:
                checkIfAnswerTrue(false);
                buttonStatus(false);
                break;
            case R.id.question_txt_view:
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
                break;
            case R.id.next_btn:
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
                break;
            case R.id.prev_btn:
                if (mCurrentIndex != 0) {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionsBank.length;
                    updateQuestion();
                    buttonStatus(false);
                }
                break;
        }

    }
}
