package com.example.e_commerce1.dto;

public class RazorpayWebhookDTO {

    private String razorpayOrderId;
    private String paymentStatus;

    public RazorpayWebhookDTO() {}

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }


    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
