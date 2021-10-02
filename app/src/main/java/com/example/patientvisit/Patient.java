package com.example.patientvisit;

import android.os.Parcel;
import android.os.Parcelable;


public class Patient implements Parcelable {
    private String id;
    private String address,age,cost,gender,medical_description,name,phone_no,starting_date;

    protected Patient(Parcel in) {
        id = in.readString();
        address = in.readString();
        age = in.readString();
        cost = in.readString();
        gender = in.readString();
        medical_description = in.readString();
        name = in.readString();
        phone_no = in.readString();
        starting_date = in.readString();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public Patient() {
    }

    public Patient(String id,String address, String age, String cost, String gender, String medical_description, String name, String phone_no, String starting_date) {
        this.id = id;
        this.address = address;
        this.age = age;
        this.cost = cost;
        this.gender = gender;
        this.medical_description = medical_description;
        this.name = name;
        this.phone_no = phone_no;
        this.starting_date = starting_date;
    }


    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getCost() {
        return cost;
    }

    public String getGender() {
        return gender;
    }

    public String getMedical_description() {
        return medical_description;
    }

    public String getName() {
        return name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getStarting_date() {
        return starting_date;
    }




    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMedical_description(String medical_description) {
        this.medical_description = medical_description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setStarting_date(String starting_date) {
        this.starting_date = starting_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(address);
        parcel.writeString(age);
        parcel.writeString(cost);
        parcel.writeString(gender);
        parcel.writeString(medical_description);
        parcel.writeString(name);
        parcel.writeString(phone_no);
        parcel.writeString(starting_date);
    }
}