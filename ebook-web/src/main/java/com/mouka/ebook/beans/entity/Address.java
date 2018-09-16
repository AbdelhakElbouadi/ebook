package com.mouka.ebook.beans.entity;

/**
 *
 * @author Abdelhak
 */

public class Address {
    private String fname;
    private String lname;
    private String addrLine1;
    private String addrLine2;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String email;
    private String phone;
    private String phoneExt;

    public Address() {
    }

    public Address(String fname, String lname, String addrLine1, String addrLine2, String city, String state, String country, String email) {
        this.fname = fname;
        this.lname = lname;
        this.addrLine1 = addrLine1;
        this.addrLine2 = addrLine2;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Address(String fname, String lname, String addrLine1, String addrLine2, String city, String state, String country, String email, String phone, String phoneExt) {
        this.fname = fname;
        this.lname = lname;
        this.addrLine1 = addrLine1;
        this.addrLine2 = addrLine2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.email = email;
        this.phone = phone;
        this.phoneExt = phoneExt;
    }
    

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return the addrLine1
     */
    public String getAddrLine1() {
        return addrLine1;
    }

    /**
     * @param addrLine1 the addrLine1 to set
     */
    public void setAddrLine1(String addrLine1) {
        this.addrLine1 = addrLine1;
    }

    /**
     * @return the addrLine2
     */
    public String getAddrLine2() {
        return addrLine2;
    }

    /**
     * @param addrLine2 the addrLine2 to set
     */
    public void setAddrLine2(String addrLine2) {
        this.addrLine2 = addrLine2;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @param zipCode the zipCode to set
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the phoneExt
     */
    public String getPhoneExt() {
        return phoneExt;
    }

    /**
     * @param phoneExt the phoneExt to set
     */
    public void setPhoneExt(String phoneExt) {
        this.phoneExt = phoneExt;
    }          
            
}
