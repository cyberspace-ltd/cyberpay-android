package com.example.cyberpay_android.models;


import org.json.JSONObject;

import java.io.Serializable;

public class Card implements Serializable {
    private JSONObject card;

    public JSONObject getCard() {
        return card;
    }

    public void setCard(JSONObject card) {
        this.card = card;
    }


}
