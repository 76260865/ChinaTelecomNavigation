package com.telecom.model;

public class Customer {
    private String mCustomerName;

    private String mPhoneNumber;

    private String mProdId;

    private String mApkUri;

    private int mInvalidTime;

    private String mAccountTime;

    public String getCustomerName() {
        return mCustomerName;
    }

    public void setCustomerName(String customerName) {
        mCustomerName = customerName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getProdId() {
        return mProdId;
    }

    public void setProdId(String prodId) {
        mProdId = prodId;
    }

    public String getmApkUri() {
        return mApkUri;
    }

    public void setApkUri(String apkUri) {
        mApkUri = apkUri;
    }

    public int getInvalidTime() {
        return mInvalidTime;
    }

    public void setInvalidTime(int invalidTime) {
        mInvalidTime = invalidTime;
    }

    public String getmAccountTime() {
        return mAccountTime;
    }

    public void setmAccountTime(String accountTime) {
        mAccountTime = accountTime;
    }
}
