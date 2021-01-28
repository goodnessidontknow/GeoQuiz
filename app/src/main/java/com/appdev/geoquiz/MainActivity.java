package com.appdev.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String ANSWERS = "answers";
    private static final String ANSWERED = "answered";

    private Button mTrueButton, mFalseButton;
    private ImageButton mNextButton, mPrevButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_one, false),
            new Question(R.string.question_two, true),
            new Question(R.string.question_three, false),
    };
    private boolean[] mAnswered = new boolean[3];
    private boolean[] mAnswers = new boolean[3];

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

        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.prev_button);

        mNextButton.setOnClickListener(v -> {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
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
            Toast.makeText(this, "You got " + right + "/" + mQuestionBank.length + " correct", Toast.LENGTH_SHORT).show();
        }
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        int messageResID = 0;
        if (userPressedTrue == mQuestionBank[mCurrentIndex].isAnswerTrue()) {
            messageResID = R.string.correct_toast;
            mAnswers[mCurrentIndex] = true;
        }
        else {
            messageResID = R.string.incorrect_toast;
        }
        mAnswered[mCurrentIndex] = true;
        Toast.makeText(this, messageResID, Toast.LENGTH_SHORT).show();
    }

    public static boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }
}