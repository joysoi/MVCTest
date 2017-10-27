package com.example.nikola.natgeoquiz;

public class Question {

    private int mTxtResId;
    private boolean mAnswerTrue;

    public Question(int txtResId, boolean answerTrue) {
        mTxtResId = txtResId;
        mAnswerTrue = answerTrue;
    }

    public int getTxtResId() {
        return mTxtResId;
    }

    public void setTxtResId(int txtResId) {
        mTxtResId = txtResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

}
