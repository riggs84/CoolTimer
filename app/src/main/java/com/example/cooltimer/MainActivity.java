package com.example.cooltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView timer;
    private Button button;
    private boolean isStarted;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        timer = findViewById(R.id.timer);
        button = findViewById(R.id.start_stop);

        seekBar.setMax(900);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                timer.setText(String.valueOf(i));
                start(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button.setOnClickListener(view -> {
            isStarted = !isStarted;
            if (isStarted) {
                countDownTimer.start();
                seekBar.setEnabled(false);
                button.setText("Stop");
            } else {
                countDownTimer.cancel();
                seekBar.setEnabled(true);
                button.setText("Start");
            }
        });
    }

    public void start(SeekBar view) {
        countDownTimer = new CountDownTimer(view.getProgress() * 1000L, 1000) {
            @Override
            public void onTick(long l) {
                int secondsRemains =  (int) l/1000;
                timer.setText(String.valueOf(secondsRemains));
            }

            @Override
            public void onFinish() {
                MediaPlayer.create(getApplicationContext(), R.raw.bell_sound).start();
            }
        };
    }
}