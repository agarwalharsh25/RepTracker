package com.reptracker.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Doctor implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("speciality")
    @Expose
    private String speciality;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("contact")
    @Expose
    private String contact;

    /**
     * No args constructor for use in serialization
     *
     */
    public Doctor() {
    }

    /**
     *
     * @param qualification
     * @param speciality
     * @param contact
     * @param name
     * @param id
     * @param department
     */
    public Doctor(String id, String name, String qualification, String speciality, String department, String contact) {
        super();
        this.id = id;
        this.name = name;
        this.qualification = qualification;
        this.speciality = speciality;
        this.department = department;
        this.contact = contact;
    }

    protected Doctor(Parcel in) {
        id = in.readString();
        name = in.readString();
        qualification = in.readString();
        speciality = in.readString();
        department = in.readString();
        contact = in.readString();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("qualification", qualification).append("speciality", speciality).append("department", department).append("contact", contact).toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(qualification);
        dest.writeString(speciality);
        dest.writeString(department);
        dest.writeString(contact);
    }
}