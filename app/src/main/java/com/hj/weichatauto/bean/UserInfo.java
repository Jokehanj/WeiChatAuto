package com.hj.weichatauto.bean;

/**
 * 项目名称：WeiChatAuto
 * 类描述：表格数据
 * 创建人：HJ
 * 创建时间：2016/11/7 23:08
 */
public class UserInfo {

    private String name;
    private String phone;
    private String address;

    private String name1;
    private String phone1;
    private String address1;


    private String name2;
    private String phone2;
    private String address2;

    public UserInfo() {
    }

    public UserInfo(String name, String phone, String address, String name1, String phone1, String address1, String name2, String phone2, String address2) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.name1 = name1;
        this.phone1 = phone1;
        this.address1 = address1;
        this.name2 = name2;
        this.phone2 = phone2;
        this.address2 = address2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }
}
