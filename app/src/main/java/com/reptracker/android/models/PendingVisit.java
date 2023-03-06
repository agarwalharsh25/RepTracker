package com.reptracker.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PendingVisit implements Parcelable {

    private String hospitalId;
    private String hospitalName;
    private String address;
    private String contact;
    private double latitude;
    private double longitude;
    private long remainingVisits;
    private List<Visits> visits;

    public PendingVisit() {
    }

    public PendingVisit(String hospitalName, String address, String contact, double latitude, double longitude, long remainingVisits) {
        super();
        this.hospitalName = hospitalName;
        this.address = address;
        this.contact = contact;
        this.latitude = latitude;
        this.longitude = longitude;
        this.remainingVisits = remainingVisits;
//        this.visits = visits;
    }

    protected PendingVisit(Parcel in) {
        hospitalId = in.readString();
        hospitalName = in.readString();
        address = in.readString();
        contact = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        remainingVisits = in.readLong();
        visits = in.createTypedArrayList(Visits.CREATOR);
    }

    public static final Creator<PendingVisit> CREATOR = new Creator<PendingVisit>() {
        @Override
        public PendingVisit createFromParcel(Parcel in) {
            return new PendingVisit(in);
        }

        @Override
        public PendingVisit[] newArray(int size) {
            return new PendingVisit[size];
        }
    };

    public void setRemainingVisits(long remainingVisits) {
        this.remainingVisits = remainingVisits;
    }

    public List<Visits> getVisits() {
        return visits;
    }

    public void setVisits(List<Visits> visits) {
        this.visits = visits;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getRemainingVisits() {
        return remainingVisits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hospitalId);
        dest.writeString(hospitalName);
        dest.writeString(address);
        dest.writeString(contact);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(remainingVisits);
        dest.writeTypedList(visits);
    }

    static public class Visits implements Parcelable {

        private String visitId;
        private String doctorId;
        private String doctorName;
        private String doctorSpeciality;
        private String doctorContact;

        public Visits() {
        }

        public Visits(String visitId, String doctorId, String doctorName, String doctorSpeciality, String doctorContact) {
            this.visitId = visitId;
            this.doctorId = doctorId;
            this.doctorName = doctorName;
            this.doctorSpeciality = doctorSpeciality;
            this.doctorContact = doctorContact;
        }

        protected Visits(Parcel in) {
            visitId = in.readString();
            doctorId = in.readString();
            doctorName = in.readString();
            doctorSpeciality = in.readString();
            doctorContact = in.readString();
        }

        static public final Creator<Visits> CREATOR = new Creator<Visits>() {
            @Override
            public Visits createFromParcel(Parcel in) {
                return new Visits(in);
            }

            @Override
            public Visits[] newArray(int size) {
                return new Visits[size];
            }
        };

        public String getVisitId() {
            return visitId;
        }

        public void setVisitId(String visitId) {
            this.visitId = visitId;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getDoctorSpeciality() {
            return doctorSpeciality;
        }

        public void setDoctorSpeciality(String doctorSpeciality) {
            this.doctorSpeciality = doctorSpeciality;
        }

        public String getDoctorContact() {
            return doctorContact;
        }

        public void setDoctorContact(String doctorContact) {
            this.doctorContact = doctorContact;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(visitId);
            dest.writeString(doctorId);
            dest.writeString(doctorName);
            dest.writeString(doctorSpeciality);
            dest.writeString(doctorContact);
        }
    }

}