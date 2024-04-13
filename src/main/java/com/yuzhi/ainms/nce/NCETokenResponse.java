package com.yuzhi.ainms.nce;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NCETokenResponse {
    private TokenData data;
    private String errcode;
    private String errmsg;

    public TokenData getData() {
        return data;
    }

    public void setData(TokenData data) {
        this.data = data;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public static class TokenData {
        @JsonProperty("token_id")
        private String tokenId;
        private String expiredDate;

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public String getExpiredDate() {
            return expiredDate;
        }

        public void setExpiredDate(String expiredDate) {
            this.expiredDate = expiredDate;
        }
    }
}
