package com.example.e_commerce1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Product entity representing items available for purchase in the e-commerce system.
 * 
 * This class models real-world products with attributes that customers care about:
 * pricing, availability, ratings, and visual representation.
 * 
 * @author Your Name
 * @since 1.0
 */
@Document(collection = "products")
public class Product {

    @Id
    private String id;
    
    // Basic product information
    private String name;
    private String description;
    private String category;  // e.g., "Electronics", "Clothing", "Books"
    private String brand;
    
    // Pricing and inventory
    private double price;
    private double discountPercentage;  // For sales and promotions
    private int stock;
    private int minimumOrderQuantity;  // Wholesale or bulk requirements
    
    // Product images - multiple images for different angles
    private List<String> imageUrls;
    private String thumbnailUrl;
    
    // Customer feedback
    private double averageRating;  // 0.0 to 5.0
    private int reviewCount;
    
    // Product availability and status
    private boolean isActive;  // Can be displayed in store
    private boolean isFeatured;  // Highlighted on homepage
    private boolean isAvailable;  // In stock and ready to ship
    
    // Metadata - tracking product lifecycle
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;  // Admin who added the product
    
    // Additional details for customer decision-making
    private String sku;  // Stock Keeping Unit
    private double weight;  // In kg, for shipping calculations
    private String dimensions;  // e.g., "10x20x5 cm"
    private List<String> tags;  // For search and filtering, e.g., "summer", "sale", "new"

    /**
     * Default constructor initializing collections and timestamps.
     * Called when creating a new product - sets sensible defaults.
     */
    public Product() {
        this.imageUrls = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.isActive = true;
        this.isAvailable = true;
        this.isFeatured = false;
        this.averageRating = 0.0;
        this.reviewCount = 0;
        this.discountPercentage = 0.0;
        this.minimumOrderQuantity = 1;
    }

    // Getters and Setters with meaningful comments for key business logic

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();  // Track when product details change
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        this.updatedAt = Instant.now();
    }

    /**
     * Calculates the final price after discount.
     * This is what the customer actually pays.
     * 
     * @return discounted price
     */
    public double getDiscountedPrice() {
        if (discountPercentage > 0) {
            return price * (1 - discountPercentage / 100);
        }
        return price;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
        this.updatedAt = Instant.now();
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
        // Automatically mark as unavailable if out of stock
        this.isAvailable = stock > 0;
        this.updatedAt = Instant.now();
    }

    /**
     * Reduces stock when an order is placed.
     * Returns true if successful, false if insufficient stock.
     */
    public boolean reduceStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            this.isAvailable = stock > 0;
            this.updatedAt = Instant.now();
            return true;
        }
        return false;
    }

    /**
     * Increases stock when inventory is restocked.
     */
    public void increaseStock(int quantity) {
        stock += quantity;
        this.isAvailable = stock > 0;
        this.updatedAt = Instant.now();
    }

    public int getMinimumOrderQuantity() {
        return minimumOrderQuantity;
    }

    public void setMinimumOrderQuantity(int minimumOrderQuantity) {
        this.minimumOrderQuantity = minimumOrderQuantity;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * Updates the product rating when a new review is added.
     * Uses a weighted average approach for accuracy.
     */
    public void addRating(double newRating) {
        double totalRating = this.averageRating * this.reviewCount;
        this.reviewCount++;
        this.averageRating = (totalRating + newRating) / this.reviewCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", discountedPrice=" + getDiscountedPrice() +
                ", stock=" + stock +
                ", averageRating=" + averageRating +
                ", isAvailable=" + isAvailable +
                '}';
    }
}

