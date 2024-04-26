package com.wusi.reimbursement.wx.dto;


public class PhoneNumberApi extends BaseApiRes{

    private PhoneInfo phone_info;

    public static class PhoneInfo{

        private String phoneNumber;
        private String purePhoneNumber;
        private String countryCode;
        private WaterMark watermark;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getPurePhoneNumber() {
            return purePhoneNumber;
        }

        public void setPurePhoneNumber(String purePhoneNumber) {
            this.purePhoneNumber = purePhoneNumber;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public WaterMark getWatermark() {
            return watermark;
        }

        public void setWatermark(WaterMark watermark) {
            this.watermark = watermark;
        }
    }

    public static class WaterMark {
        private String timestamp;
        private String appid;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }
    }

    public PhoneInfo getPhone_info() {
        return phone_info;
    }

    public void setPhone_info(PhoneInfo phone_info) {
        this.phone_info = phone_info;
    }
}
