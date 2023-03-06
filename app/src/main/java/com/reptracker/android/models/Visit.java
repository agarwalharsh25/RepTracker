package com.reptracker.android.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Visit implements Parcelable{

    private String visitId;
    private String visitStarted;
    private String reachedHospital;
    private String presentationStarted;
    private String presentationFinished;
    private String visitFinished;

    public Visit() {
    }

    public Visit(String visitId, String visitStarted, String reachedHospital, String presentationStarted, String presentationFinished, String visitFinished) {
        this.visitId = visitId;
        this.visitStarted = visitStarted;
        this.reachedHospital = reachedHospital;
        this.presentationStarted = presentationStarted;
        this.presentationFinished = presentationFinished;
        this.visitFinished = visitFinished;
    }

    protected Visit(Parcel in) {
        visitId = in.readString();
        visitStarted = in.readString();
        reachedHospital = in.readString();
        presentationStarted = in.readString();
        presentationFinished = in.readString();
        visitFinished = in.readString();
    }

    public static final Creator<Visit> CREATOR = new Creator<Visit>() {
        @Override
        public Visit createFromParcel(Parcel in) {
            return new Visit(in);
        }

        @Override
        public Visit[] newArray(int size) {
            return new Visit[size];
        }
    };

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getVisitStarted() {
        return visitStarted;
    }

    public void setVisitStarted(String visitStarted) {
        this.visitStarted = visitStarted;
    }

    public String getReachedHospital() {
        return reachedHospital;
    }

    public void setReachedHospital(String reachedHospital) {
        this.reachedHospital = reachedHospital;
    }

    public String getPresentationStarted() {
        return presentationStarted;
    }

    public void setPresentationStarted(String presentationStarted) {
        this.presentationStarted = presentationStarted;
    }

    public String getPresentationFinished() {
        return presentationFinished;
    }

    public void setPresentationFinished(String presentationFinished) {
        this.presentationFinished = presentationFinished;
    }

    public String getVisitFinished() {
        return visitFinished;
    }

    public void setVisitFinished(String visitFinished) {
        this.visitFinished = visitFinished;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(visitId);
        dest.writeString(visitStarted);
        dest.writeString(reachedHospital);
        dest.writeString(presentationStarted);
        dest.writeString(presentationFinished);
        dest.writeString(visitFinished);
    }
}