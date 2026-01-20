# E-Commerce Backend System

A minimal e-commerce backend system built with Spring Boot and MongoDB.

## Features

✅ Product Management (Create, List products)  
✅ Shopping Cart (Add items, View cart, Clear cart)  
✅ Order Management (Create orders from cart)  
✅ Payment Integration (Razorpay)  
✅ Webhook Support (Payment status updates)  
✅ Automatic Stock Management  

## Tech Stack

- **Backend**: Spring Boot 2.7.18
- **Database**: MongoDB
- **Payment Gateway**: Razorpay
- **Build Tool**: Maven
- **Java Version**: 8 (JDK 1.8)

## Database Schema

### Collections

1. **users** - User information
2. **products** - Product catalog
3. **cart_items** - Shopping cart items
4. **orders** - Order records
5. **order_items** - Individual items in orders
6. **payments** - Payment transactions

### Entity Relationships

```
USER (1) -----> (N) CART_ITEM
USER (1) -----> (N) ORDER
PRODUCT (1) --> (N) CART_ITEM
PRODUCT (1) --> (N) ORDER_ITEM
ORDER (1) ----> (N) ORDER_ITEM
ORDER (1) ----> (1) PAYMENT
```

## Prerequisites

- Java 8 (JDK 1.8) or higher
- Maven 3.6+
- MongoDB running on localhost:27017 (or MongoDB Atlas cloud database)
- Razorpay account (for payment integration)

## Setup Instructions

### 1. Clone the repository
```bash
git clone <repository-url>
cd EcommerceSpring-master
```

### 2. Configure MongoDB

**Option A: Local MongoDB**
- Install MongoDB Community Edition from [mongodb.com](https://www.mongodb.com/try/download/community)
- Start MongoDB service: `mongod` or start as Windows service
- MongoDB will run on default port 27017

**Option B: MongoDB Atlas (Cloud)**
- Create free cluster at [mongodb.com/cloud/atlas](https://www.mongodb.com/cloud/atlas)
- Get connection string and update `application.yaml`:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://<username>:<password>@cluster.mongodb.net/ecommerce_db
```

**Compile and Build:**
```bash
mvn clean install -DskipTests
```

**Run Application:**
```bash
mvn spring-boot:run
```

**Or run the JAR directly:**
```bash
java -jar target/e_commerce1-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

**Verify it's running:**
- Open browser: `http://localhost:8080`
- You should see the welcome page
- API endpoints available at `http://localhost:8080/api/*
```yaml
razorpay:
  key:
    id: your_razorpay_key_id
    secret: your_razorpay_key_secret
  webhook:
    secret: your_webhook_secret
```

### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Product APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/products` | Create a new product |
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |

### Cart APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/cart/add` | Add item to cart |
| GET | `/api/cart/{userId}` | Get user's cart |
| DELETE | `/api/cart/{userId}/clear` | Clear user's cart |

### Order APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create order from cart |
| GET | `/api/orders/{orderId}` | Get order details |
| GET | `/api/orders` | Get all orders |

### Payment APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/payments/razorpay/{orderId}` | Create Razorpay payment |

### Webhook

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/webhook/razorpay` | Razorpay payment webhook |

## Sample Requests

### 1. Create Product
```json
POST /api/products
{
  "name": "Laptop",
  "description": "Gaming Laptop",
  "price": 50000.0,
  "stock": 10
}
```

### 2. Add to Cart
```json
POST /api/cart/add
{
  "userId": "user123",
  "productId": "prod123",
  "quantity": 2
}
```

### 3. Create Order
```json
POST /api/orders
{
  "userId": "user123"
}
```

### 4. Create Payment
```json
POST /api/payments/razorpay/order123
```

## Business Logic

### Order Creation Flow

1. User adds products to cart
2. User creates order
3. System validates stock availability
4. Order is created with status "CREATED"
5. Stock is reduced
6. Cart is cleared

### Payment Flow

1. Payment is initiated via Razorpay
2. Payment record created with status "PENDING"
### Using Postman or any API client:

**Step 1: Create Products**
```bash
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "price": 599.0,
  "stock": 50
}
```

**Step 2: Add to Cart**
```bash
POST http://localhost:8080/api/cart/add
Content-Type: application/json

{
  "userId": "user001",
  "Troubleshooting

### MongoDB Connection Issues
**Error:** `MongoSocketOpenException: Exception opening socket`
- **Solution:** Make sure MongoDB is running on port 27017
- Check with: `mongod --version` (if installed locally)
- Or verify MongoDB Atlas connection string

### Port Already in Use
**Error:** `Port 8080 was already in use`
- **Solution:** Change port in `application.yaml`:
```yaml
server:
  port: 8081
```

### Build Failures
- Run: `mvn clean install -DskipTests` to skip tests during build
- Make sure Java 8 is installed: `java -version`
- Clear Maven cache if needed: Delete `.m2/repository` folder

### IDE Shows Errors But Build Works
- IDE cache issue - not actual errors
- Solution: Invalidate IDE cache and restart, or use Maven commands directly

## Important Notes

- All amounts are in INR (Indian Rupees)
- Razorpay amounts are handled in paise (1 INR = 100 paise)
- Stock is automatically validated and reduced when orders are created
- Insufficient stock throws `InsufficientStockException`
- Duplicate cart items are automatically merged with updated quantity
- Order timestamps recorded using `Instant.now()`
- Empty cart validation prevents creating orders without items

## Features Implemented

✅ **User Management** - User entities with role support  
✅ **Product Catalog** - CRUD operations with stock tracking  
✅ **Shopping Cart** - Add, view, clear with duplicate handling  
✅ **Order Processing** - Create orders with stock validation  
✅ **Payment Integration** - Razorpay payment gateway  
✅ **Webhook Handling** - Automatic payment status updates  
✅ **Stock Management** - Real-time inventory tracking  
✅ **Custom Exceptions** - Proper error handling  
✅ **Entity Relationships** - Normalized database design  

## License

This is a student project for educational purposes.

---

**Developed with Spring Boot and MongoDB**
```bash
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "userId": "user001"
}
```

**Step 5: Create Payment**
```bash
POST http://localhost:8080/api/payments/razorpay/{{orderId}}
```

**Testing Notes:**
- Use consistent `userId` across cart and order operations
- Save `productId` from create product response
- Save `orderId` from create order response
- Cart is automatically cleared after successful order creation
The application uses custom exceptions for better error handling:

- `ProductNotFoundException` - When product is not found
- `OrderNotFoundException` - When order is not found
- `EmptyCartException` - When trying to create order with empty cart
- `InsufficientStockException` - When product stock is insufficient

## Testing

Use Postman to test all the APIs. Make sure to:

1. Create products first
2. Add items to cart
3. Create order
4. Initiate payment
5. Verify order status updates after payment

## Project Structure

```
src/main/java/com/example/e_commerce1/
├── controller/          # REST controllers
├── service/             # Business logic
├── repository/          # Database repositories
├── model/              # Entity models
├── dto/                # Data Transfer Objects
├── exception/          # Custom exceptions
├── config/             # Configuration classes
└── webhook/            # Webhook handlers
```

## Notes

- All amounts are in INR (Indian Rupees)
- Razorpay amounts are handled in paise (1 INR = 100 paise)
- Stock is automatically managed when orders are created
- Duplicate cart items are automatically merged with quantity updated
- Order timestamps are recorded using `Instant`

## Author

Student Project for E-Commerce Backend System
