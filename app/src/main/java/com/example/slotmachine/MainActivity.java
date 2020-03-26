package com.example.slotmachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView ivReelOne, ivReelTwo, ivReelThree;
    private Drawable[] drwImages;
    private TextView tvScore;
    private SeekBar seekbarDifficulty;
    private Handler hndlrReels;
    private int intInitSpeedReelOne, intInitSpeedReelTwo, intInitSpeedReelThree, intSpeedReelOne,
            intSpeedReelTwo, intSpeedReelThree, intScore, intIMGOne, intIMGTwo, intIMGThree;
    private boolean boolIsActive;
    private Button btnStart;
    private Random ranNum;
    private ReelOne ReelOne;
    private ReelTwo ReelTwo;
    private ReelThree ReelThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
        resumeInstanceState(savedInstanceState);
        seekbarDifficultyListener();
    }

    private void initFields(){
        drwImages = new Drawable[]{getDrawable(R.drawable.cherry), getDrawable(R.drawable.grape), getDrawable(R.drawable.strawberry), getDrawable(R.drawable.pear)};
        ivReelOne = findViewById(R.id.ivReelOne);
        ivReelTwo = findViewById(R.id.ivReelTwo);
        ivReelThree = findViewById(R.id.ivReelThree);
        tvScore = findViewById(R.id.tvScorePoints);
        btnStart = findViewById(R.id.btnStart);
        seekbarDifficulty = findViewById(R.id.seekbarDifficulty);
        hndlrReels = new Handler();
        boolIsActive = false;
        intScore = 0;
        ranNum = new Random();
        ReelOne = new ReelOne();
        ReelTwo = new ReelTwo();
        ReelThree = new ReelThree();
        intIMGOne = 0;
        intIMGTwo = 0;
        intIMGThree = 0;
        ivReelOne.setImageDrawable(drwImages[intIMGOne]);
        ivReelTwo.setImageDrawable(drwImages[intIMGTwo]);
        ivReelThree.setImageDrawable(drwImages[intIMGThree]);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Score", intScore);
        savedInstanceState.putBoolean("isActive", boolIsActive);
        savedInstanceState.putInt("SpeedReelOne", intSpeedReelOne);
        savedInstanceState.putInt("SpeedReelTwo", intSpeedReelTwo);
        savedInstanceState.putInt("SpeedReelThree", intSpeedReelThree);
        savedInstanceState.putInt("imgOne", intIMGOne);
        savedInstanceState.putInt("imgTwo", intIMGTwo);
        savedInstanceState.putInt("imgThree", intIMGThree);
        savedInstanceState.putInt("initSpeedOne", intInitSpeedReelOne);
        savedInstanceState.putInt("initSpeedTwo", intInitSpeedReelTwo);
        savedInstanceState.putInt("initSpeedThree", intInitSpeedReelThree);
    }

    private void resumeInstanceState(Bundle savedInstanceState){
        if( savedInstanceState != null){
            intScore = savedInstanceState.getInt("Score");
            boolIsActive = savedInstanceState.getBoolean("isActive");
            tvScore.setText(getString(R.string.tvScore, intScore));
            intInitSpeedReelOne = savedInstanceState.getInt("initSpeedOne");
            intInitSpeedReelTwo = savedInstanceState.getInt("initSpeedTwo");
            intInitSpeedReelThree = savedInstanceState.getInt("initSpeedThree");
            intSpeedReelOne = savedInstanceState.getInt("SpeedReelOne");
            intSpeedReelTwo = savedInstanceState.getInt("SpeedReelTwo");
            intSpeedReelThree = savedInstanceState.getInt("SpeedReelThree");
            intIMGOne = savedInstanceState.getInt("imgOne");
            intIMGTwo = savedInstanceState.getInt("imgTwo");
            intIMGThree = savedInstanceState.getInt("imgThree");

            if(boolIsActive){
                ivReelOne.setImageDrawable(drwImages[intIMGOne]);
                ivReelTwo.setImageDrawable(drwImages[intIMGTwo]);
                ivReelThree.setImageDrawable(drwImages[intIMGThree]);
                btnStart.setText(R.string.btnStop);
            }
        }
    }

    public void onPause(){
        super.onPause();
        hndlrReels.removeCallbacks(ReelOne);
        hndlrReels.removeCallbacks(ReelTwo);
        hndlrReels.removeCallbacks(ReelThree);
    }

    public void onResume(){
        super.onResume();
        if(boolIsActive){
            hndlrReels.postDelayed(ReelOne, 0);
            hndlrReels.postDelayed(ReelTwo, 0);
            hndlrReels.postDelayed(ReelThree, 0);
        }
    }

    public void btnStart(View view){
        if(!boolIsActive) {
            boolIsActive = true;
            intInitSpeedReelOne = (ranNum.nextInt(101) + 100);
            intInitSpeedReelTwo = (ranNum.nextInt(101) + 100);
            intInitSpeedReelThree =  (ranNum.nextInt(101) + 100);
            initSeekBarProgress(seekbarDifficulty.getProgress());
            hndlrReels.postDelayed(ReelOne, 0);
            hndlrReels.postDelayed(ReelTwo, 0);
            hndlrReels.postDelayed(ReelThree, 0);
            btnStart.setText(R.string.btnStop);
        }else{
            boolIsActive = false;
            stopReels();
            btnStart.setText(R.string.btnStart);
        }
    }

    private void stopReels(){
        hndlrReels.removeCallbacks(ReelOne);
        hndlrReels.removeCallbacks(ReelTwo);
        hndlrReels.removeCallbacks(ReelThree);
        awardPoints();
    }

    private void awardPoints(){
        boolIsActive = false;
        if(intIMGOne == intIMGTwo && intIMGOne == intIMGThree){
            intScore += 50;
        }else{
            if(intIMGOne == 0) intScore += 10;
            if(intIMGTwo == 0) intScore += 10;
            if(intIMGThree == 0) intScore += 10;
        }

        tvScore.setText(getString(R.string.tvScore, intScore));
    }

    private class ReelOne implements Runnable{
        public void run(){
            intIMGOne = (intIMGOne == 3) ? 0 : intIMGOne + 1;
            ivReelOne.setImageDrawable(drwImages[intIMGOne]);
            hndlrReels.postDelayed(ReelOne, intSpeedReelOne);
        }
    }

    private class ReelTwo implements Runnable{
        public void run(){
            intIMGTwo = (intIMGTwo == 3) ? 0 : intIMGTwo + 1;
            ivReelTwo.setImageDrawable(drwImages[intIMGTwo]);
            hndlrReels.postDelayed(ReelTwo, intSpeedReelTwo);
        }
    }

    private class ReelThree implements Runnable{
        public void run(){
            intIMGThree = (intIMGThree == 3) ? 0 : intIMGThree + 1;
            ivReelThree.setImageDrawable(drwImages[intIMGThree]);
            hndlrReels.postDelayed(ReelThree, intSpeedReelThree);
        }
    }

    public void initSeekBarProgress(int sbProgress) {
        switch (sbProgress) {
            case 0:
                seekbarDifficultySpeedMultiplier(5.0);
                break;
            case 1:
                seekbarDifficultySpeedMultiplier(4.8);
                break;
            case 2:
                seekbarDifficultySpeedMultiplier(4.6);
                break;
            case 3:
                seekbarDifficultySpeedMultiplier(4.4);
                break;
            case 4:
                seekbarDifficultySpeedMultiplier(4.2);
                break;
            case 5:
                seekbarDifficultySpeedMultiplier(4.0);
                break;
            case 6:
                seekbarDifficultySpeedMultiplier(3.8);
                break;
            case 7:
                seekbarDifficultySpeedMultiplier(3.6);
                break;
            case 8:
                seekbarDifficultySpeedMultiplier(3.4);
                break;
            case 9:
                seekbarDifficultySpeedMultiplier(3.2);
                break;
            case 10:
                seekbarDifficultySpeedMultiplier(3.0);
                break;
            case 11:
                seekbarDifficultySpeedMultiplier(2.8);
                break;
            case 12:
                seekbarDifficultySpeedMultiplier(2.6);
                break;
            case 13:
                seekbarDifficultySpeedMultiplier(2.4);
                break;
            case 14:
                seekbarDifficultySpeedMultiplier(2.2);
                break;
            case 15:
                seekbarDifficultySpeedMultiplier(2.0);
                break;
            case 16:
                seekbarDifficultySpeedMultiplier(1.8);
                break;
            case 17:
                seekbarDifficultySpeedMultiplier(1.6);
                break;
            case 18:
                seekbarDifficultySpeedMultiplier(1.4);
                break;
            case 19:
                seekbarDifficultySpeedMultiplier(1.2);
                break;
            case 20:
                seekbarDifficultySpeedMultiplier(1.0);
                break;
        }
    }

    private void seekbarDifficultySpeedMultiplier(double dblSpeedMultiplier){
        intSpeedReelOne = (int) dblSpeedMultiplier * intInitSpeedReelOne;
        intSpeedReelTwo = (int) dblSpeedMultiplier * intInitSpeedReelTwo;
        intSpeedReelThree = (int) dblSpeedMultiplier * intInitSpeedReelThree;
    }

    public void seekbarDifficultyListener(){
        seekbarDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initSeekBarProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void btnHelp(View view){
        Intent HelpScreen = new Intent(this, HelpScreen.class);
        startActivity(HelpScreen);
    }
}
