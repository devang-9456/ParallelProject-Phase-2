package com.cg.wallet.exception;

public class NotFoundException extends Exception {

    private String msg;

    public NotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
