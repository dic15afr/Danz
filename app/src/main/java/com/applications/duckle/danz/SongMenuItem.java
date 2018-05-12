package com.applications.duckle.danz;

import android.widget.TextView;

public class SongMenuItem {

    private String cardName;
    private int imageResourceId;
    private int isturned;
    TextView textView;

    public int getIsturned() {
        return isturned;
    }
    public void setIsturned(int isturned) {
        this.isturned = isturned;
    }
    public String getCardName() {
        return cardName;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public int getImageResourceId() {
        return imageResourceId;
    }
    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

}
