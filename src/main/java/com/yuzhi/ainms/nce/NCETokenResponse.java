package com.yuzhi.ainms.nce;

public class NCETokenResponse {

    private Data data; // 映射"data"部分
    private String errcode; // 映射"errcode"字段
    private String errmsg; // 映射"errmsg"字段

    // 构造器
    public NCETokenResponse() {
    }

    // data的getter和setter
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    // errcode的getter和setter
    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    // errmsg的getter和setter
    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    // Data内部类，用于映射"data"字段里的内容
    public static class Data {
        private String tokenId; // 映射"token_id"字段
        private String expiredDate; // 映射"expiredDate"字段

        // 构造器
        public Data() {
        }

        // tokenId的getter和setter
        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        // expiredDate的getter和setter
        public String getExpiredDate() {
            return expiredDate;
        }

        public void setExpiredDate(String expiredDate) {
            this.expiredDate = expiredDate;
        }
    }
}

