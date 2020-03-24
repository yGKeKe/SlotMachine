package com.example.slotmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView ivReelOne, ivReelTwo, ivReelThree;
    private Drawable drwCherry, drwGrape, drwStrawBerry, drwPear;
    private Drawable[] drwImages;
    private TextView tvScore;
    private SeekBar seekbarDifficulty;
    private Handler hndlrReels;
    private int intInitSpeedReelOne, intInitSpeedReelTwo, intInitSpeedReelThree, intSpeedReelOne, intSpeedReelTwo, intSpeedReelThree, intScore, intIMGOne, intIMGTwo, intIMGThree;
    private boolean boolIsActive, boolReelOneActive, boolReelTwoActive, boolReelThreeActive;
    private Button btnStart;
    private LinearLayout linlayButtonGroup;
    private Random ranNum;
    private ReelOne ReelOne;
    private ReelTwo ReelTwo;
    private ReelThree ReelThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
        seekbarDifficultyListener();
        resumeInstanceState(savedInstanceState);
    }

    private void initFields(){
        ivReelOne = findViewById(R.id.ivReelOne);
        ivReelTwo = findViewById(R.id.ivReelTwo);
        ivReelThree = findViewById(R.id.ivReelThree);
        drwCherry = getDrawable(R.drawable.cherry);
        drwGrape = getDrawable(R.drawable.grape);
        drwStrawBerry = getDrawable(R.drawable.strawberry);
        drwPear = getDrawable(R.drawable.pear);
        drwImages = new Drawable[]{drwCherry, drwGrape, drwStrawBerry, drwPear};
        tvScore = findViewById(R.id.tvScorePoints);
        btnStart = findViewById(R.id.btnStart);
        seekbarDifficulty = findViewById(R.id.seekbarDifficulty);
        linlayButtonGroup = findViewById(R.id.layoutButtonGroup);
        hndlrReels = new Handler();
        boolIsActive = false;
        intScore = 0;
        ranNum = new Random();
        ReelOne = new ReelOne();
        ReelTwo = new ReelTwo();
        ReelThree = new ReelThree();
    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Score", intScore);
        savedInstanceState.putBoolean("isActive", boolIsActive);
    }

    private void resumeInstanceState(Bundle savedInstanceState){
        if( savedInstanceState != null){
            intScore = savedInstanceState.getInt("Score");
            boolIsActive = savedInstanceState.getBoolean("isActive");
            tvScore.setText("" + intScore);
        }
    }

    public void btnStart(View view){
        boolIsActive = true;
        intInitSpeedReelOne = (ranNum.nextInt(126) + 100);
        intInitSpeedReelTwo = (ranNum.nextInt(126) + 100);
        intInitSpeedReelThree = (ranNum.nextInt(126) + 100);
        intSpeedReelOne = intInitSpeedReelOne;
        intSpeedReelTwo = intInitSpeedReelTwo;
        intSpeedReelThree = intInitSpeedReelThree;
        hndlrReels.postDelayed(ReelOne, 0);
        boolReelOneActive = true;
        hndlrReels.postDelayed(ReelTwo, 0);
        boolReelTwoActive = true;
        hndlrReels.postDelayed(ReelThree, 0);
        boolReelThreeActive = true;
        btnStart.setVisibility(View.GONE);
        linlayButtonGroup.setVisibility(View.VISIBLE);
    }

    public void btnReelOne(View view){
        hndlrReels.removeCallbacks(ReelOne);
        boolReelOneActive = false;
        if(!boolReelTwoActive && !boolReelThreeActive){
            awardPoints();
        }
    }

    public void btnReelTwo(View view){
        hndlrReels.removeCallbacks(ReelTwo);
        boolReelTwoActive = false;
        if(!boolReelOneActive && !boolReelThreeActive){
            awardPoints();
        }
    }

    public void btnReelThree(View view){
        hndlrReels.removeCallbacks(ReelThree);
        boolReelThreeActive = false;
        if(!boolReelOneActive && !boolReelTwoActive){
            awardPoints();
        }
    }

    private void awardPoints(){
        boolIsActive = false;
        if(intIMGOne == intIMGTwo && intIMGOne == intIMGThree){
            intScore += 50;
        }else{
            if(intIMGOne == 1) intScore += 10;
            if(intIMGTwo == 1) intScore += 10;
            if(intIMGThree == 1) intScore += 10;
        }

        tvScore.setText("" + intScore);
        linlayButtonGroup.setVisibility(View.GONE);
        btnStart.setVisibility(View.VISIBLE);
    }

    private class ReelOne implements Runnable{
        public void run(){
            ivReelOne.setImageDrawable(drwImages[intIMGOne]);
            if(intIMGOne == 3){
                intIMGOne = 0;
            }else{
                intIMGOne++;
            }
            hndlrReels.postDelayed(ReelOne, intSpeedReelOne);
        }
    }

    private class ReelTwo implements Runnable{
        public void run(){
            ivReelTwo.setImageDrawable(drwImages[intIMGTwo]);
            if(intIMGTwo == 3){
                intIMGTwo = 0;
            }else{
                intIMGTwo++;
            }
            hndlrReels.postDelayed(ReelTwo, intSpeedReelTwo);
        }
    }

    private class ReelThree implements Runnable{
        public void run(){
            ivReelThree.setImageDrawable(drwImages[intIMGThree]);
            if(intIMGThree == 3){
                intIMGThree = 0;
            }else{
                intIMGThree++;
            }
            hndlrReels.postDelayed(ReelThree, intSpeedReelThree);
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
                switch(progress){
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
}
