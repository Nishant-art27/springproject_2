# Digital Marketplace Platform

Welcome! 👋  
This is a **backend e-commerce marketplace application** built using **Spring Boot** and **MongoDB**. It supports a complete online shopping workflow including product management, shopping carts, order processing, inventory management, and Razorpay payment integration.

This project was created as a learning-focused backend system to gain hands-on experience with real-world application architecture, NoSQL databases, and third-party payment gateways.

---

## 🚀 Features

- **Product Management** – Add, view, and manage marketplace products  
- **Shopping Cart (Shopping Bag)** – Add items, update quantities, and manage carts  
- **Order Processing** – Convert carts into orders and track purchases  
- **Payment Integration** – Razorpay payment gateway support  
- **Inventory Management** – Prevents overselling by tracking stock  
- **Webhook Handling** – Automatically updates order and payment status  

---

## 🛠 Tech Stack

- **Spring Boot** 2.7.18  
- **MongoDB** (NoSQL Database)  
- **Razorpay** (Payment Gateway)  
- **Maven** (Build Tool)  
- **Java 8**  

---

## 🗄 Database Collections

- `marketplace_items` – Marketplace products  
- `customers` – Customer information  
- `shopping_bag_items` – Cart items  
- `purchase_orders` – Orders  
- `purchase_line_items` – Items within orders  
- `transactions` – Payment transactions  

All entities are logically connected to ensure data consistency.

---

## ⚙️ Getting Started

### Prerequisites

- Java 8 or higher  
- Maven  
- MongoDB (local or MongoDB Atlas)  
- Razorpay account (for payment testing)  

---

### Installation & Setup

#### 1. Clone the Repository

```bash
git clone https://github.com/netram75/springproject.git
cd springproject
spring:
  data:
    mongodb:
      uri: mongodb+srv://your-username:your-password@cluster.mongodb.net/marketplace_platform_db
razorpay:
  key:
    id: your_razorpay_key_id
    secret: your_razorpay_key_secret
  webhook:
    secret: your_webhook_secret
mvn clean install
mvn spring-boot:run
