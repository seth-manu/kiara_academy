package com.kiaraacademy.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("id")
    @Expose
    public String order_id;
    @SerializedName("entity")
    @Expose
    public String entity;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("amount_paid")
    @Expose
    public String amount_paid;
    @SerializedName("amount_due")
    @Expose
    public String amount_due;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("receipt")
    @Expose
    public String receipt;
    @SerializedName("offer_id")
    @Expose
    public String offer_id;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("attempts")
    @Expose
    public String attempts;
    @SerializedName("created_at")
    @Expose
    public String created_at;
}
