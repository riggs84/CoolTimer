package com.example.cooltimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.SeekBar;

import com.example.cooltimer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final static String STATE_KEY = "State";

    private ActivityMainBinding binding;
    private CountDownTimer countDownTimer;
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            this.state = savedInstanceState.getParcelable(STATE_KEY);
        } else {
            this.state = new State();
        }

        binding.seekBar.setMax(900);
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                state.setProgress(i);
                binding.timer.setText(String.valueOf(state.getProgress()));
                // TODO we create new thread every time we call onCreate() Activity LC. Expensive
                start(state.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.startStop.setOnClickListener(view -> {
            state.setStarted(!state.isStarted());
            binding.seekBar.setEnabled(!state.isStarted());
            binding.startStop.setText(state.isStarted()? "Stop" : "Start");
            if (state.isStarted()) {
                countDownTimer.start();
            } else {
                countDownTimer.cancel();
            }
        });
    }

    public void start(int progress) {
        countDownTimer = new CountDownTimer(progress * 1000L, 1000) {
            @Override
            public void onTick(long l) {
                state.setSecondsRemains((int) l/1000);
                binding.timer.setText(String.valueOf(state.getSecondsRemains()));
            }

            @Override
            public void onFinish() {
                MediaPlayer.create(getApplicationContext(), R.raw.bell_sound).start();
            }
        };
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_KEY, this.state);
    }
}