package com.app.medical.Profile;

import android.os.Parcel;
import android.os.Parcelable;

public class User_Model implements Parcelable {

    private String address;
    private int age;
    private String blood_type;
    private int emergency_contact;
    private String gender;
    private int id;
    private String name;
    private String nationality;
    private int phone;


    public User_Model() {
    }

    public User_Model( int id, String name, int age, String gender, int phone, String blood_type, String address, int emergency_contact, String nationality) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.blood_type = blood_type;
        this.address = address;
        this.emergency_contact = emergency_contact;
        this.nationality = nationality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(int emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "User_Model{" +
                "address='" + address + '\'' +
                ", age=" + age +
                ", blood_type='" + blood_type + '\'' +
                ", emergency_contact=" + emergency_contact +
                ", gender='" + gender + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", phone=" + phone +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeInt(this.age);
        parcel.writeString(this.gender);
        parcel.writeInt(this.phone);
        parcel.writeString(this.blood_type);
        parcel.writeString(this.address);
        parcel.writeInt(this.emergency_contact);
        parcel.writeString(this.nationality);
    }

    public static final Parcelable.Creator<User_Model> CREATOR = new Parcelable.Creator<User_Model>() {
        public User_Model createFromParcel(Parcel in) {
            return new User_Model(in);
        }

        public User_Model[] newArray(int size) {
            return new User_Model[size];
        }
    };

    private User_Model(Parcel in) {
        id = in.readInt();
        name = in.readString();
        age = in.readInt();
        gender = in.readString();
        phone = in.readInt();
        blood_type = in.readString();
        address = in.readString();
        emergency_contact = in.readInt();
        nationality = in.readString();
    }
}


