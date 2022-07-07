package com.example.cooltimer;

import android.os.Parcel;
import android.os.Parcelable;

public class State implements Parcelable {

    private boolean isStarted;
    private int progress;
    private int secondsRemains;

    public State() {}

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setSecondsRemains(int secondsRemains) {
        this.secondsRemains = secondsRemains;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public int getProgress() {
        return progress;
    }

    public int getSecondsRemains() {
        return secondsRemains;
    }

    protected State(Parcel in) {
        isStarted = in.readByte() != 0;
        progress = in.readInt();
        secondsRemains = in.readInt();
    }

    public static final Creator<State> CREATOR = new Creator<State>() {
        @Override
        public State createFromParcel(Parcel in) {
            return new State(in);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isStarted ? 1 : 0));
        parcel.writeInt(progress);
        parcel.writeInt(secondsRemains);
    }
}
