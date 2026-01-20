package com.example.e_commerce1.controller;

import com.example.e_commerce1.dto.ApiResponse;
import com.example.e_commerce1.dto.ProductRequestDTO;
import com.example.e_commerce1.dto.ProductResponseDTO;
import com.example.e_commerce1.model.Product;
import com.example.e_commerce1.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for Product operations.
 * 
 * This is the entry point for all product-related API calls.
 * Think of it as the "receptionist" - it receives requests, validates them,
 * passes them to the service layer, and returns user-friendly responses.
 * 
 * API Design Philosophy:
 * - Use standard HTTP methods (GET, POST, PUT, DELETE)
 * - Return appropriate status codes (200, 201, 404, 400, etc.)
 * - Consistent response format using ApiResponse wrapper
 * - Clear, descriptive error messages
 * 
 * @author Your Name
 * @version 1.0
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")  // In production, specify exact frontend URL
public class ProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product in the system.
     * 
     * POST /api/products
     * 
     * Example request body:
     * {
     *   "name": "Wireless Headphones",
     *   "description": "Premium noise-cancelling headphones",
     *   "price": 2499.99,
     *   "stock": 50,
     *   "category": "Electronics",
     *   "brand": "Sony"
     * }
     * 
     * @param requestDto Product details from client
     * @return Created product with 201 status
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> addProduct(@Valid @RequestBody ProductRequestDTO requestDto) {
        logger.info("Received request to add new product: {}", requestDto.getName());
        
        try {
            // Map DTO to entity - in a real app, use ModelMapper or MapStruct
            Product newProduct = new Product();
            newProduct.setName(requestDto.getName());
            newProduct.setDescription(requestDto.getDescription());
            newProduct.setPrice(requestDto.getPrice());
            newProduct.setStock(requestDto.getStock());
            newProduct.setCategory(requestDto.getCategory());
            newProduct.setBrand(requestDto.getBrand());
            
            // Set additional fields if provided
            if (requestDto.getImageUrls() != null) {
                newProduct.setImageUrls(requestDto.getImageUrls());
            }
            if (requestDto.getTags() != null) {
                newProduct.setTags(requestDto.getTags());
            }
            
            Product savedProduct = productService.addProduct(newProduct);
            
            logger.info("Successfully created product with ID: {}", savedProduct.getId());
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(savedProduct, "Product added successfully!"));
                    
        } catch (Exception e) {
            logger.error("Error adding product: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to add product. Please try again."));
        }
    }

    /**
     * Retrieves all active products.
     * 
     * GET /api/products
     * 
     * Returns product DTOs with calculated fields like discounts,
     * stock status, ratings, etc. Perfect for product listing pages.
     * 
     * @return List of all products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAllProducts() {
        logger.debug("Received request to fetch all products");
        
        try {
            List<ProductResponseDTO> products = productService.getAllProducts();
            
            logger.debug("Returning {} products", products.size());
            
            String message = products.isEmpty() ? 
                           "No products available at the moment." : 
                           "Successfully retrieved " + products.size() + " products.";
            
            return ResponseEntity.ok(ApiResponse.success(products, message));
            
        } catch (Exception e) {
            logger.error("Error fetching products: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve products. Please try again."));
        }
    }

    /**
     * Retrieves a specific product by its ID.
     * 
     * GET /api/products/{id}
     * 
     * Use this for product detail pages where you need all information
     * about a single product.
     * 
     * @param id Product ID
     * @return Product details or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductById(@PathVariable String id) {
        logger.debug("Received request to fetch product with ID: {}", id);
        
        try {
            ProductResponseDTO product = productService.getProductDetails(id);
            
            logger.debug("Successfully retrieved product: {}", product.getName());
            
            return ResponseEntity.ok(
                    ApiResponse.success(product, "Product retrieved successfully."));
                    
        } catch (Exception e) {
            logger.warn("Product not found with ID: {}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                            "Product not found. It may have been removed or the ID is incorrect.",
                            "PRODUCT_NOT_FOUND"));
        }
    }

    /**
     * Searches products by name or description.
     * 
     * GET /api/products/search?q=laptop
     * 
     * Case-insensitive search across product names and descriptions.
     * Essential for the search bar functionality.
     * 
     * @param searchTerm What the user is looking for
     * @return Matching products
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> searchProducts(
            @RequestParam(value = "q", required = true) String searchTerm) {
        
        logger.info("Searching products with term: {}", searchTerm);
        
        try {
            // Validate search term
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(ApiResponse.error("Search term cannot be empty.", "INVALID_SEARCH_TERM"));
            }
            
            List<ProductResponseDTO> products = productService.searchProducts(searchTerm.trim());
            
            String message = products.isEmpty() ? 
                           "No products found matching '" + searchTerm + "'. Try different keywords." :
                           "Found " + products.size() + " product(s) matching your search.";
            
            logger.info("Search returned {} results for term: {}", products.size(), searchTerm);
            
            return ResponseEntity.ok(ApiResponse.success(products, message));
            
        } catch (Exception e) {
            logger.error("Error during product search: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Search failed. Please try again."));
        }
    }

    /**
     * Filters products by category.
     * 
     * GET /api/products/category/Electronics
     * 
     * Use this for category navigation pages.
     * Categories could be: Electronics, Clothing, Books, Home & Garden, etc.
     * 
     * @param category Category name
     * @return Products in that category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getProductsByCategory(
            @PathVariable String category) {
        
        logger.info("Fetching products in category: {}", category);
        
        try {
            List<ProductResponseDTO> products = productService.getProductsByCategory(category);
            
            String message = products.isEmpty() ?
                           "No products found in category '" + category + "'." :
                           "Found " + products.size() + " product(s) in " + category + ".";
            
            return ResponseEntity.ok(ApiResponse.success(products, message));
            
        } catch (Exception e) {
            logger.error("Error fetching products by category: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve products."));
        }
    }

    /**
     * Gets featured products for homepage.
     * 
     * GET /api/products/featured
     * 
     * Returns products marked as featured - these are highlighted
     * on the homepage or promotional banners.
     * 
     * @return Featured products
     */
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getFeaturedProducts() {
        logger.debug("Fetching featured products");
        
        try {
            List<ProductResponseDTO> products = productService.getFeaturedProducts();
            
            return ResponseEntity.ok(
                    ApiResponse.success(products, "Featured products retrieved successfully."));
                    
        } catch (Exception e) {
            logger.error("Error fetching featured products: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve featured products."));
        }
    }

    /**
     * Updates an existing product.
     * 
     * PUT /api/products/{id}
     * 
     * Admin functionality - allows updating product details like
     * price, stock, description, etc.
     * 
     * @param id Product ID to update
     * @param requestDto Updated product data
     * @return Updated product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequestDTO requestDto) {
        
        logger.info("Received request to update product ID: {}", id);
        
        try {
            Product productToUpdate = new Product();
            productToUpdate.setName(requestDto.getName());
            productToUpdate.setDescription(requestDto.getDescription());
            productToUpdate.setPrice(requestDto.getPrice());
            productToUpdate.setStock(requestDto.getStock());
            productToUpdate.setCategory(requestDto.getCategory());
            productToUpdate.setDiscountPercentage(requestDto.getDiscountPercentage());
            
            Product updatedProduct = productService.updateProduct(id, productToUpdate);
            
            logger.info("Successfully updated product ID: {}", id);
            
            return ResponseEntity.ok(
                    ApiResponse.success(updatedProduct, "Product updated successfully!"));
                    
        } catch (Exception e) {
            logger.error("Error updating product: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update product."));
        }
    }

    /**
     * Deactivates a product (soft delete).
     * 
     * DELETE /api/products/{id}
     * 
     * We don't actually delete products to preserve order history.
     * Instead, we mark them as inactive so they don't show in the store.
     * 
     * @param id Product ID to deactivate
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateProduct(@PathVariable String id) {
        logger.info("Received request to deactivate product ID: {}", id);
        
        try {
            productService.deactivateProduct(id);
            
            logger.info("Successfully deactivated product ID: {}", id);
            
            return ResponseEntity.ok(
                    ApiResponse.success("Product has been deactivated successfully."));
                    
        } catch (Exception e) {
            logger.error("Error deactivating product: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to deactivate product."));
        }
    }
}

