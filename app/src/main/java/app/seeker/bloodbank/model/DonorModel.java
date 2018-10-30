package app.seeker.bloodbank.model;

import java.io.Serializable;

public class DonorModel {

    String uid;
    String name;
    String email;
    String phoneNumber;
    String city;
    String country;
    String pssword;
    String bloodGroup;
   // boolean isdonor;
    String donateDate;

    public String getDonateDate() {
        return donateDate;
    }

    public void setDonateDate(String donateDate) {
        this.donateDate = donateDate;
    }

    public DonorModel() {
    }

    public DonorModel(String name, String email, String phoneNumber, String city, String country, String bloodGroup,String donateDate) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.country = country;
        this.bloodGroup = bloodGroup;
        this.donateDate =donateDate;
    }


   /* public DonorModel(String name, String email, String phoneNumber, String city, String country,String bloodGroup, boolean isdonor) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.country = country;
        this.bloodGroup = bloodGroup;
        this.isdonor = isdonor;
    }
*/
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPssword() {
        return pssword;
    }

    public void setPssword(String pssword) {
        this.pssword = pssword;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

   /* public boolean isIsdonor() {
        return isdonor;
    }*/

    /*public void setIsdonor(boolean isdonor) {
        this.isdonor = isdonor;
    }*/
}
