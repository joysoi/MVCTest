package com.example.nikola.natgeoquiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    Button mTrueButton;
    Button mFalseButton;
    Button mNextButton;
    Button mPrevButton;
    Button mCheatButton;
    TextView mQuestionsTextView;
    TextView mNumberOfCheats;
    private int mCurrentIndex;
    private int obtainedScore;
    public static final String TAG = "MainActivity";
    public static final String KEY_INDEX = "index";
    public static final String CHEATER_INDEX = "index_cheater";
    private static final int REQUEST_CHEAT_CODE = 0;
    private boolean mIsCheater;
    int mCounterCheat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(CHEATER_INDEX, false);
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

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(this);

        mNumberOfCheats = (TextView) findViewById(R.id.txt_number_cheats);

        updateQuestion();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBoolean(CHEATER_INDEX, mIsCheater);
    }

    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTxtResId();
        mQuestionsTextView.setText(question);

    }

    private void checkIfAnswerTrue(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
        int msgResId;
        if (mIsCheater) {
            msgResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                msgResId = R.string.msg_correct;
                obtainedScore += 1;
            } else {
                msgResId = R.string.msg_incorrect;
            }
        }
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CHEAT_CODE) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @SuppressLint("DefaultLocale")
    private void showScore() {
        float percentage;
        if (mCurrentIndex == 5) {
            double totalScore = mQuestionsBank.length;
            percentage = (float) ((obtainedScore * 100) / totalScore);
            Toast.makeText(this, "You scored:" + " " + String.format("%.0f", percentage) + "%", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    private void buttonStatus(boolean status) {
        mTrueButton.setEnabled(status);
        mFalseButton.setEnabled(status);
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
                showScore();
                buttonStatus(false);
                break;
            case R.id.false_btn:
                checkIfAnswerTrue(false);
                showScore();
                buttonStatus(false);
                break;
            case R.id.question_txt_view:
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
                break;
            case R.id.next_btn:
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                mIsCheater = false;
                updateQuestion();
                buttonStatus(true);
                break;
            case R.id.prev_btn:
                if (mCurrentIndex != 0) {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionsBank.length;
                    buttonStatus(true);
                    updateQuestion();
                }
                break;
            case R.id.cheat_button:
                boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.onNewIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CHEAT_CODE);
                cheatsLeft();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void cheatsLeft() {
        mCounterCheat++;
        if (mCounterCheat == 3) {
            mCheatButton.setVisibility(View.INVISIBLE);
        }
        mNumberOfCheats.setText("You have total of 3 cheats " + " / " + mCounterCheat);
    }
}

