package com.example.e_commerce1.service;

import com.example.e_commerce1.dto.ProductResponseDTO;
import com.example.e_commerce1.exception.ProductNotFoundException;
import com.example.e_commerce1.model.Product;
import com.example.e_commerce1.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for product operations.
 * 
 * Handles all business logic related to products:
 * - CRUD operations
 * - Stock management
 * - Search and filtering
 * - Price calculations
 * 
 * Think of this as the "brain" of product management - it makes decisions
 * about what should happen when users interact with products.
 */
@Service
public class ProductService {
    
    // Logger helps us track what's happening in production
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    
    // Business rule: Products created within 30 days are "new arrivals"
    private static final int NEW_ARRIVAL_DAYS = 30;
    
    // Business rule: Low stock warning threshold
    private static final int LOW_STOCK_THRESHOLD = 10;
    
    // Business rule: Free shipping on orders above this price
    private static final double FREE_SHIPPING_THRESHOLD = 500.0;

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Adds a new product to the catalog.
     * 
     * In a real system, you'd also:
     * - Validate the admin user has permission
     * - Check for duplicate SKUs
     * - Generate unique SKU if not provided
     * - Trigger notification to marketing team
     */
    public Product addProduct(Product product) {
        logger.info("Adding new product: {} in category: {}", 
                    product.getName(), product.getCategory());
        
        // Business logic: Generate SKU if not provided
        if (product.getSku() == null || product.getSku().isEmpty()) {
            product.setSku(generateSku(product));
        }
        
        Product savedProduct = productRepository.save(product);
        logger.info("Successfully added product with ID: {}", savedProduct.getId());
        
        return savedProduct;
    }

    /**
     * Retrieves all active products and converts them to DTOs.
     * 
     * We return DTOs instead of raw entities to:
     * 1. Control what data is exposed to frontend
     * 2. Add calculated fields (discounts, labels)
     * 3. Format data for better UI display
     */
    public List<ProductResponseDTO> getAllProducts() {
        logger.debug("Fetching all active products");
        
        List<Product> products = productRepository.findAll();
        
        // Filter only active products - we don't show deactivated ones to customers
        return products.stream()
                .filter(Product::isActive)
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets a single product by ID.
     * Throws exception if not found - frontend will handle this gracefully.
     */
    public Product getProductById(String productId) {
        logger.debug("Fetching product with ID: {}", productId);
        
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.warn("Product not found with ID: {}", productId);
                    return new ProductNotFoundException("Product with id " + productId + " not found");
                });
    }

    /**
     * Gets a product as a DTO with all calculated fields.
     * This is what the product detail page would use.
     */
    public ProductResponseDTO getProductDetails(String productId) {
        Product product = getProductById(productId);
        return convertToResponseDTO(product);
    }

    /**
     * Searches products by name or description.
     * Case-insensitive search - more user-friendly.
     */
    public List<ProductResponseDTO> searchProducts(String searchTerm) {
        logger.info("Searching products with term: {}", searchTerm);
        
        List<Product> products = productRepository.findAll();
        
        return products.stream()
                .filter(Product::isActive)
                .filter(p -> p.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                           (p.getDescription() != null && 
                            p.getDescription().toLowerCase().contains(searchTerm.toLowerCase())))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Filters products by category.
     * Essential for navigation in e-commerce sites.
     */
    public List<ProductResponseDTO> getProductsByCategory(String category) {
        logger.debug("Fetching products in category: {}", category);
        
        List<Product> products = productRepository.findAll();
        
        return products.stream()
                .filter(Product::isActive)
                .filter(p -> category.equalsIgnoreCase(p.getCategory()))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets featured products for homepage display.
     * These are products we want to highlight - best sellers, promotions, etc.
     */
    public List<ProductResponseDTO> getFeaturedProducts() {
        logger.debug("Fetching featured products");
        
        List<Product> products = productRepository.findAll();
        
        return products.stream()
                .filter(Product::isActive)
                .filter(Product::isFeatured)
                .map(this::convertToResponseDTO)
                .limit(10)  // Show top 10 featured products
                .collect(Collectors.toList());
    }

    /**
     * Updates product information.
     * In production, you'd add partial updates to avoid overwriting everything.
     */
    public Product updateProduct(String productId, Product updatedProduct) {
        logger.info("Updating product ID: {}", productId);
        
        Product existingProduct = getProductById(productId);
        
        // Update fields that are allowed to change
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setDiscountPercentage(updatedProduct.getDiscountPercentage());
        existingProduct.setUpdatedAt(Instant.now());
        
        Product saved = productRepository.save(existingProduct);
        logger.info("Successfully updated product ID: {}", productId);
        
        return saved;
    }

    /**
     * Reduces stock when an item is purchased.
     * Returns true if successful, false if insufficient stock.
     * 
     * This is critical - we need to prevent overselling!
     */
    public boolean reduceStock(String productId, int quantity) {
        logger.debug("Attempting to reduce stock for product: {} by {} units", productId, quantity);
        
        Product product = getProductById(productId);
        
        if (product.getStock() < quantity) {
            logger.warn("Insufficient stock for product: {}. Available: {}, Requested: {}", 
                       productId, product.getStock(), quantity);
            return false;
        }
        
        product.reduceStock(quantity);
        productRepository.save(product);
        
        logger.info("Successfully reduced stock for product: {}. New stock: {}", 
                   productId, product.getStock());
        
        // In a real system, you might trigger low stock alerts here
        if (product.getStock() < LOW_STOCK_THRESHOLD) {
            logger.warn("Low stock alert! Product: {} has only {} units remaining", 
                       product.getName(), product.getStock());
        }
        
        return true;
    }

    /**
     * Soft delete - we mark as inactive instead of actually deleting.
     * This preserves order history and lets us reactivate if needed.
     */
    public void deactivateProduct(String productId) {
        logger.info("Deactivating product ID: {}", productId);
        
        Product product = getProductById(productId);
        product.setActive(false);
        product.setUpdatedAt(Instant.now());
        productRepository.save(product);
        
        logger.info("Successfully deactivated product ID: {}", productId);
    }

    /**
     * Converts a Product entity to a ProductResponseDTO.
     * 
     * This is where we add all the calculated fields and labels
     * that make the UI more user-friendly.
     */
    private ProductResponseDTO convertToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        
        // Basic fields
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        dto.setSku(product.getSku());
        
        // Pricing - both original and discounted
        dto.setOriginalPrice(product.getPrice());
        dto.setFinalPrice(product.getDiscountedPrice());
        dto.setDiscountPercentage(product.getDiscountPercentage());
        
        // Human-friendly discount label
        if (product.getDiscountPercentage() > 0) {
            dto.setDiscountLabel(String.format("%.0f%% OFF", product.getDiscountPercentage()));
            dto.setOnSale(true);
        } else {
            dto.setOnSale(false);
        }
        
        // Stock status with helpful labels
        dto.setAvailableStock(product.getStock());
        dto.setInStock(product.getStock() > 0);
        dto.setMinimumOrderQuantity(product.getMinimumOrderQuantity());
        
        if (product.getStock() == 0) {
            dto.setStockStatus("Out of Stock");
        } else if (product.getStock() < LOW_STOCK_THRESHOLD) {
            dto.setStockStatus("Low Stock - Only " + product.getStock() + " left!");
        } else {
            dto.setStockStatus("In Stock");
        }
        
        // Images
        dto.setImageUrls(product.getImageUrls());
        dto.setThumbnailUrl(product.getThumbnailUrl());
        
        // Ratings with display label
        dto.setRating(product.getAverageRating());
        dto.setReviewCount(product.getReviewCount());
        
        if (product.getReviewCount() > 0) {
            dto.setRatingLabel(String.format("%.1f ★ (%d reviews)", 
                                            product.getAverageRating(), 
                                            product.getReviewCount()));
        } else {
            dto.setRatingLabel("No reviews yet");
        }
        
        // Status badges
        dto.setFeatured(product.isFeatured());
        
        // Check if it's a new arrival (created within last 30 days)
        if (product.getCreatedAt() != null) {
            long daysSinceCreation = Duration.between(product.getCreatedAt(), Instant.now()).toDays();
            dto.setNewArrival(daysSinceCreation <= NEW_ARRIVAL_DAYS);
        }
        
        // Shipping information
        dto.setWeight(product.getWeight());
        dto.setDimensions(product.getDimensions());
        dto.setFreeShipping(product.getPrice() >= FREE_SHIPPING_THRESHOLD);
        
        // Metadata
        dto.setCreatedAt(product.getCreatedAt());
        dto.setTags(product.getTags());
        
        return dto;
    }

    /**
     * Generates a SKU (Stock Keeping Unit) for a product.
     * 
     * Real systems use more sophisticated SKU generation,
     * often based on category, brand, and a sequential number.
     */
    private String generateSku(Product product) {
        String categoryCode = product.getCategory() != null ? 
                             product.getCategory().substring(0, Math.min(3, product.getCategory().length())).toUpperCase() : 
                             "GEN";
        
        long timestamp = System.currentTimeMillis() % 100000;  // Last 5 digits
        
        return String.format("%s-%d", categoryCode, timestamp);
    }
}

