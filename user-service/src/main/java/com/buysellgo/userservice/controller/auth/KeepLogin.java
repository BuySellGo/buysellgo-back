package com.buysellgo.userservice.controller.auth;

public enum KeepLogin {
    ACTIVE, INACTIVE;

    public boolean isKeepLogin() {
        return this.equals(ACTIVE) || this.equals(INACTIVE);
    }
}
