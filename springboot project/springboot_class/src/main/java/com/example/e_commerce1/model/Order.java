package com.example.e_commerce1.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Order entity representing a customer's purchase.
 * 
 * An order goes through several states:
 * 1. PENDING - Order created, waiting for payment
 * 2. PAYMENT_RECEIVED - Payment successful, ready for processing
 * 3. PROCESSING - Items being prepared for shipment
 * 4. SHIPPED - Order dispatched from warehouse
 * 5. OUT_FOR_DELIVERY - With delivery agent
 * 6. DELIVERED - Successfully delivered to customer
 * 7. CANCELLED - Order cancelled by customer or system
 * 8. RETURNED - Customer returned the order
 * 9. REFUNDED - Money returned to customer
 * 
 * This lifecycle mimics real e-commerce platforms like Amazon or Flipkart.
 * 
 * @author Your Name
 */
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    
    // Order reference number - visible to customers
    private String orderNumber;  // e.g., "ORD-2026-00123"

    // Customer information
    private String userId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Order items and pricing
    private List<OrderItem> items;
    private double subtotal;  // Sum of all items
    private double taxAmount;  // GST or other taxes
    private double shippingCharges;
    private double discount;  // Applied coupon or offer
    private double totalAmount;  // Final amount paid
    
    // Order status and tracking
    private String status;  // PENDING, PROCESSING, SHIPPED, DELIVERED, etc.
    private Payment payment;
    
    // Delivery information
    private User.Address shippingAddress;
    private User.Address billingAddress;
    private String trackingNumber;  // Courier tracking ID
    private String courierName;  // "BlueDart", "DTDC", etc.
    private Instant estimatedDeliveryDate;
    private Instant actualDeliveryDate;
    
    // Order lifecycle timestamps
    private Instant createdAt;
    private Instant paymentReceivedAt;
    private Instant shippedAt;
    private Instant deliveredAt;
    private Instant cancelledAt;
    
    // Additional details
    private String specialInstructions;  // "Leave at door", "Call before delivery"
    private List<String> statusHistory;  // Track status changes
    private boolean isGift;
    private String giftMessage;
    private String cancellationReason;
    private String returnReason;

    /**
     * Default constructor with sensible defaults.
     */
    public Order() {
        this.items = new ArrayList<>();
        this.statusHistory = new ArrayList<>();
        this.createdAt = Instant.now();
        this.status = "PENDING";
        this.taxAmount = 0.0;
        this.shippingCharges = 0.0;
        this.discount = 0.0;
        this.isGift = false;
    }

    /**
     * Generates a human-friendly order number.
     * Format: ORD-YYYY-XXXXX
     */
    public void generateOrderNumber() {
        int year = Instant.now().toString().substring(0, 4).equals("2026") ? 2026 : 
                   Integer.parseInt(Instant.now().toString().substring(0, 4));
        long timestamp = System.currentTimeMillis() % 100000;
        this.orderNumber = String.format("ORD-%d-%05d", year, timestamp);
    }

    /**
     * Calculates the total amount based on items, tax, shipping, and discount.
     * This is the final amount customer pays.
     */
    public void calculateTotalAmount() {
        // Calculate subtotal from items
        this.subtotal = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        
        // Total = Subtotal + Tax + Shipping - Discount
        this.totalAmount = this.subtotal + this.taxAmount + this.shippingCharges - this.discount;
        
        // Ensure total is never negative
        if (this.totalAmount < 0) {
            this.totalAmount = 0;
        }
    }

    /**
     * Updates order status and records it in history.
     * This helps track the order's journey.
     */
    public void updateStatus(String newStatus) {
        String historyEntry = String.format("%s: %s", 
                                          Instant.now().toString(), 
                                          newStatus);
        this.statusHistory.add(historyEntry);
        this.status = newStatus;
        
        // Update timestamps based on status
        switch (newStatus) {
            case "PAYMENT_RECEIVED":
                this.paymentReceivedAt = Instant.now();
                break;
            case "SHIPPED":
                this.shippedAt = Instant.now();
                break;
            case "DELIVERED":
                this.deliveredAt = Instant.now();
                this.actualDeliveryDate = Instant.now();
                break;
            case "CANCELLED":
                this.cancelledAt = Instant.now();
                break;
        }
    }

    /**
     * Checks if order can be cancelled.
     * Usually, orders can't be cancelled once shipped.
     */
    public boolean isCancellable() {
        return "PENDING".equals(status) || 
               "PAYMENT_RECEIVED".equals(status) || 
               "PROCESSING".equals(status);
    }

    /**
     * Checks if order can be returned.
     * Usually within 7-30 days of delivery.
     */
    public boolean isReturnable() {
        if (!"DELIVERED".equals(status) || deliveredAt == null) {
            return false;
        }
        
        // Check if within 30 days of delivery
        long daysSinceDelivery = java.time.Duration.between(deliveredAt, Instant.now()).toDays();
        return daysSinceDelivery <= 30;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(double shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User.Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(User.Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public User.Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(User.Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public Instant getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(Instant estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public Instant getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(Instant actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getPaymentReceivedAt() {
        return paymentReceivedAt;
    }

    public void setPaymentReceivedAt(Instant paymentReceivedAt) {
        this.paymentReceivedAt = paymentReceivedAt;
    }

    public Instant getShippedAt() {
        return shippedAt;
    }

    public void setShippedAt(Instant shippedAt) {
        this.shippedAt = shippedAt;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Instant deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public List<String> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<String> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public boolean isGift() {
        return isGift;
    }

    public void setGift(boolean gift) {
        isGift = gift;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber='" + orderNumber + '\'' +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", createdAt=" + createdAt +
                '}';
    }
}


