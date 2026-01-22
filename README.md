# 🛒 E-Commerce Application - Professional Grade

A complete, production-ready e-commerce REST API built with **Spring Boot**, **MongoDB**, and **Razorpay** payment integration. This project demonstrates real-world development practices that professional developers use daily.

## 🌟 What Makes This Project "Humanized"?

Unlike basic tutorials, this project includes the **real-world touches** that make software production-ready:

### 1. **Thoughtful Comments & Documentation**
- Every class has JavaDoc explaining its purpose
- Comments explain *why*, not just *what*
- Business logic is documented like you're talking to a colleague

### 2. **User-Friendly Error Messages**
- No cryptic stack traces for users
- Friendly messages like "Product not found. It may have been removed or the ID is incorrect."
- Validation errors explain exactly what's wrong

### 3. **Comprehensive Logging**
- Debug logs for development
- Info logs for production tracking
- Warn logs for issues that need attention
- Error logs with full context for troubleshooting

### 4. **Realistic Data Models**
- Products have ratings, reviews, discount labels
- Users have addresses, loyalty points, membership tiers
- Orders track entire lifecycle from creation to delivery

### 5. **Business Logic That Makes Sense**
- Free shipping above ₹500
- Low stock alerts when inventory < 10
- Products are "new arrivals" for 30 days
- Orders can be cancelled only before shipping
- Returns allowed within 30 days of delivery

### 6. **Professional API Design**
- Consistent response format (success/error)
- Proper HTTP status codes (200, 201, 404, 400, 500)
- Search, filter, pagination-ready structure
- RESTful endpoints that follow conventions

---

## 🏗️ Architecture Overview

```
┌─────────────────┐
│   Frontend/UI   │  (React, Angular, or Mobile App)
└────────┬────────┘
         │ HTTP/REST
┌────────▼────────────────────────────────────┐
│          CONTROLLERS                         │
│  (Receive requests, validate, return JSON)  │
└────────┬────────────────────────────────────┘
         │
┌────────▼────────────────────────────────────┐
│          SERVICES                            │
│  (Business logic, calculations, rules)      │
└────────┬────────────────────────────────────┘
         │
┌────────▼────────────────────────────────────┐
│          REPOSITORIES                        │
│  (Database operations via Spring Data)      │
└────────┬────────────────────────────────────┘
         │
┌────────▼────────────────────────────────────┐
│          MongoDB                             │
│  (Data storage: Products, Users, Orders)    │
└─────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
src/main/java/com/example/e_commerce1/
│
├── controller/          # REST API endpoints (the receptionist)
│   ├── ProductController.java    # Product CRUD + search
│   ├── CartController.java       # Shopping cart management
│   ├── OrderController.java      # Order placement & tracking
│   └── PaymentController.java    # Payment processing
│
├── service/            # Business logic (the brain)
│   ├── ProductService.java       # Product operations & stock
│   ├── CartService.java          # Cart calculations
│   ├── OrderService.java         # Order lifecycle
│   └── PaymentService.java       # Razorpay integration
│
├── repository/         # Database access (the librarian)
│   ├── ProductRepository.java
│   ├── UserRepository.java
│   ├── OrderRepository.java
│   └── CartRepository.java
│
├── model/              # Data entities (what we store)
│   ├── Product.java    # With ratings, images, discounts
│   ├── User.java       # With addresses, loyalty program
│   ├── Order.java      # With tracking, delivery status
│   ├── Payment.java    # Payment transaction details
│   └── CartItem.java   # Items in shopping cart
│
├── dto/                # Data transfer objects (API contracts)
│   ├── ApiResponse.java         # Consistent response format
│   ├── ProductRequestDTO.java   # Product creation/update
│   ├── ProductResponseDTO.java  # Rich product display
│   └── CreateOrderRequestDTO.java
│
├── exception/          # Error handling (crisis management)
│   ├── GlobalExceptionHandler.java  # Catches all errors
│   ├── ProductNotFoundException.java
│   ├── InsufficientStockException.java
│   └── EmptyCartException.java
│
└── config/             # Configuration classes
    └── RazorpayConfig.java  # Payment gateway setup
```

---

## 🚀 Features

### Product Management
- ✅ Add products with images, categories, brands
- ✅ Search products by name/description
- ✅ Filter by category
- ✅ Featured products for homepage
- ✅ Automatic SKU generation
- ✅ Stock management with alerts
- ✅ Discount calculation
- ✅ Rating system

### User Management
- ✅ User profiles with multiple addresses
- ✅ Loyalty points & membership tiers (Bronze/Silver/Gold)
- ✅ Email & phone verification flags
- ✅ Favorite categories for personalization
- ✅ Newsletter subscription

### Order Processing
- ✅ Create order from cart
- ✅ Calculate tax, shipping, discounts
- ✅ Order tracking with status updates
- ✅ Cancellation & return support
- ✅ Delivery date estimation
- ✅ Gift orders with messages
- ✅ Order history tracking

### Payment Integration
- ✅ Razorpay payment gateway
- ✅ Webhook handling for payment confirmation
- ✅ Secure payment processing
- ✅ Payment status tracking

### Developer Experience
- ✅ Comprehensive logging (SLF4J)
- ✅ Input validation with helpful messages
- ✅ Global exception handling
- ✅ Consistent API responses
- ✅ Clean code with meaningful comments

---

## 🛠️ Technology Stack

| Component | Technology | Why? |
|-----------|-----------|------|
| **Backend Framework** | Spring Boot 2.7.18 | Industry standard, rapid development |
| **Database** | MongoDB | Flexible schema for evolving e-commerce needs |
| **Payment Gateway** | Razorpay | Popular Indian payment solution |
| **Validation** | Hibernate Validator | Built-in Spring Boot validation |
| **Logging** | SLF4J + Logback | Professional logging with levels |
| **Build Tool** | Maven | Dependency management |
| **Java Version** | 8+ | Wide compatibility |

---

## ⚙️ Setup & Installation

### Prerequisites
- **Java 8** or higher
- **MongoDB** running on `localhost:27017`
- **Maven** (or use included `mvnw`)
- **Razorpay Account** (for payment testing)

### Steps

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd springboot_class
   ```

2. **Configure MongoDB**
   - Install MongoDB from https://www.mongodb.com/try/download/community
   - Start MongoDB service:
     ```bash
     mongod --dbpath /path/to/data/directory
     ```

3. **Update Configuration**
   Edit `src/main/resources/application.yaml`:
   ```yaml
   spring:
     mongodb:
       uri: mongodb://localhost:27017/ecommerce_db
   
   razorpay:
     key:
       id: YOUR_RAZORPAY_KEY_ID
       secret: YOUR_RAZORPAY_SECRET
   ```

4. **Build the project**
   ```bash
   ./mvnw clean install
   ```

5. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

6. **Verify it's running**
   Open browser: http://localhost:8080/api/products

---

## 🧪 API Endpoints

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/products` | Create a new product |
| `GET` | `/api/products` | Get all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `GET` | `/api/products/search?q=laptop` | Search products |
| `GET` | `/api/products/category/{category}` | Filter by category |
| `GET` | `/api/products/featured` | Get featured products |
| `PUT` | `/api/products/{id}` | Update product |
| `DELETE` | `/api/products/{id}` | Deactivate product |

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/orders` | Create order from cart |
| `GET` | `/api/orders/{id}` | Get order details |
| `GET` | `/api/orders` | Get all orders |

### Example Requests

**Create Product:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Wireless Headphones",
    "description": "Premium noise-cancelling headphones",
    "price": 2499.99,
    "stock": 50,
    "category": "Electronics",
    "brand": "Sony"
  }'
```

**Search Products:**
```bash
curl http://localhost:8080/api/products/search?q=headphones
```

---

## 🎯 Real-World Practices Demonstrated

### 1. **Separation of Concerns**
- Controllers handle HTTP
- Services contain business logic
- Repositories manage data
- DTOs define API contracts

### 2. **Error Handling**
```java
// Instead of this (bad):
throw new Exception("Error");

// We do this (good):
throw new ProductNotFoundException("Product with id " + id + " not found");
// Which gets caught by GlobalExceptionHandler and returned as:
{
  "success": false,
  "message": "Product not found. It may have been removed...",
  "errorCode": "PRODUCT_NOT_FOUND"
}
```

### 3. **Validation**
```java
@NotBlank(message = "Product name is required")
@Size(min = 3, max = 100, message = "Name must be 3-100 characters")
private String name;
```

### 4. **Meaningful Logging**
```java
logger.info("Creating order for user: {}. Total: ₹{}", userId, totalAmount);
logger.warn("Low stock alert! Product: {} has only {} units", name, stock);
logger.error("Payment failed for order: {}. Reason: {}", orderId, reason);
```

### 5. **Calculated Fields**
```java
// Not just storing price, but calculating discounted price:
public double getDiscountedPrice() {
    return price * (1 - discountPercentage / 100);
}
```

---

## 🔒 Security Considerations

**Current Status:** This is a development/learning project.

**For Production, Add:**
- 🔐 Spring Security for authentication
- 🔑 JWT tokens for API security
- 🛡️ HTTPS/SSL certificates
- 🔒 Password encryption (BCrypt)
- 🚫 Rate limiting to prevent abuse
- ✅ Input sanitization for XSS prevention
- 🔐 Environment variables for secrets (never commit credentials!)

---

## 📈 Future Enhancements

Ideas to make this even better:

- [ ] User authentication (JWT + Spring Security)
- [ ] Product reviews & ratings API
- [ ] Wishlist functionality
- [ ] Order cancellation & refunds
- [ ] Email notifications (order confirmation, shipping updates)
- [ ] SMS notifications for OTP
- [ ] Admin dashboard endpoints
- [ ] Product inventory alerts
- [ ] Coupon/discount code system
- [ ] Analytics & reporting
- [ ] Product recommendations (ML-based)

---

## 📚 Learning Resources

Want to understand more? Check out:

- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **MongoDB Java Driver:** https://www.mongodb.com/docs/drivers/java/
- **Razorpay API:** https://razorpay.com/docs/api/
- **REST API Design:** https://restfulapi.net/

---

## 🤝 Contributing

This project demonstrates professional practices. If you want to contribute:

1. Follow the existing code style
2. Add meaningful comments
3. Include validation & error handling
4. Test your changes
5. Update this README if needed

---

## 👨‍💻 About This Project

This isn't just code - it's a **learning resource** that shows how real developers think:

- **Comments explain WHY**, not just what
- **Error messages are user-friendly**
- **Business logic reflects real requirements**
- **Code is organized like a production app**
- **Logging helps debug issues**
- **Validation prevents bad data**

It's the difference between:
- "Product not found" ❌
- "Product not found. It may have been removed or the ID is incorrect. Please check and try again." ✅

---

## 📞 Support

Questions? Want to learn more about a specific part?

- Open an issue on GitHub
- Check the inline comments in the code
- Read the JavaDoc in each class

---

## 📝 License

This project is for educational purposes. Feel free to use, modify, and learn from it!

---

**Happy Coding! 🚀**

Remember: Professional code isn't just about making it work - it's about making it understandable, maintainable, and user-friendly. That's what makes it "humanized"!
