package com.example;

/**
 * Created by tom on 2016/5/10.
 */
public class AddressVO {
    private String address;
    private String personal;

    public AddressVO() {}

    public AddressVO(
            String address,
            String personal) {
        this.address = address;
        this.personal = personal;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    @Override
    public String toString() {
        return "AddressVO{" +
                "address='" + address + '\'' +
                ", personal='" + personal + '\'' +
                '}';
    }
}