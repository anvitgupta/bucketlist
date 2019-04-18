package com.cs4279.buckit;

import android.view.View;


public abstract class CardClickListener implements View.OnClickListener {
    public static final int MARK_AS_DONE = 0;
    public static final int ADD_TO_BUCKIT = 1;
    public static final int LIKE = 2;

    protected int buttonType;
    protected String cardID;

    public void setButtonType(int buttonType) {
        this.buttonType = buttonType;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }
}
