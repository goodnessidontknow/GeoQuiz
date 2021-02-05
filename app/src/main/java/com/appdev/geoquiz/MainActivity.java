package com.appdev.geoquiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String ANSWERS = "answers";
    private static final String ANSWERED = "answered";
    private static final String CHEATED = "cheated";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton, mFalseButton, mCheatButton;
    private ImageButton mNextButton, mPrevButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_one, false),
            new Question(R.string.question_two, true),
            new Question(R.string.question_three, false),
    };
    private boolean[] mAnswered = new boolean[3];
    private boolean[] mAnswers = new boolean[3];

    private boolean mAnswerShown;

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = findViewById(R.id.question_text_view);

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(v -> {if (!mAnswered[mCurrentIndex]) checkAnswer(true);});
        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(v -> {if (!mAnswered[mCurrentIndex]) checkAnswer(false);});

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(v -> {
            boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
            Intent intent = CheatActivity.newIntent(this, answerIsTrue);
            startActivityForResult(intent, REQUEST_CODE_CHEAT);
        });

        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.prev_button);

        mNextButton.setOnClickListener(v -> {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            mAnswerShown = false;
            updateQuestion();
        });
        mPrevButton.setOnClickListener(v -> {
            if (mCurrentIndex == 0) {
                mCurrentIndex = 2;
            } else {
                mCurrentIndex = Math.abs(mCurrentIndex - 1);
            }
            updateQuestion();
        });

        mQuestionTextView.setOnClickListener(v -> {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            updateQuestion();
        });

        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(ANSWERED, mAnswered);
        savedInstanceState.putBooleanArray(ANSWERS, mAnswers);
        savedInstanceState.putBoolean(CHEATED, mAnswerShown);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        if (areAllTrue(mAnswered)) {
            int right = 0;
            for(boolean b : mAnswers) if(b) right++;
            makeToast("You got " + right + "/" + mQuestionBank.length + " correct");
        }
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        if (mAnswerShown) {
            makeToast(R.string.judgment_toast);
        } else {
            int messageResID = 0;
            if (userPressedTrue == mQuestionBank[mCurrentIndex].isAnswerTrue()) {
                messageResID = R.string.correct_toast;
                mAnswers[mCurrentIndex] = true;
            } else {
                messageResID = R.string.incorrect_toast;
            }
            makeToast(messageResID);
        }
        mAnswered[mCurrentIndex] = true;
        updateQuestion();
    }

    public void makeToast(int text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    public void makeToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    public static boolean areAllTrue(boolean[] array) {
        for(boolean b : array) if(!b) return false;
        return true;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mAnswerShown = CheatActivity.wasAnswerShown(data);
        }
    }
}