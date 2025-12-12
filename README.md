# 🚀 PaySnap — QR-Based Payment Link Generator API

PaySnap is a secure, Stripe-powered payment system that generates checkout links, QR codes, and PDF receipts for fast and contactless transactions.  
The platform tracks payments in real time via Stripe webhooks and ensures robust security using **JWT authentication with Redis-backed token blacklisting**.

Built with **Spring Boot 3**, **PostgreSQL**, **Redis**, **ZXing**, **Stripe Java SDK**, and **OpenPDF**, PaySnap provides a modern and reliable payment infrastructure.

---

## 🧭 Overview

PaySnap enables users to:

- Create secure Stripe payment sessions on demand
- Share instant QR codes for mobile-friendly checkout
- Track payment status in real time
- Automatically generate PDF receipts after successful payments
- Download or receive receipts by email
- Maintain complete order & payment history
- Logout safely using Redis-based JWT blacklisting
- Use short and simple shareable payment URLs

---

## 🧱 Architecture Highlights

| Component        | Technology        | Purpose |
|------------------|-------------------|---------|
| Backend Framework | Spring Boot 3 | Core application framework |
| Payment Engine | Stripe Java SDK | Checkout session + payment tracking |
| QR Codes | ZXing | Dynamic QR image generation |
| Database | PostgreSQL | Persistent order & user data |
| In-Memory Store | Redis | JWT blacklist + session expiry |
| Background Tasks | Spring Scheduler | Expired session cleanup |
| PDF Generator | OpenPDF | Receipt creation |
| Email Sender | Spring Mail | Automated receipt delivery |
| Auth | JWT + Spring Security | Secure API access |
| Docs | Swagger / OpenAPI | Developer documentation |

---

## 🎯 Key Features & How They Work

### 🧾 1. Order Creation & Payment Link Generation
- Users specify amount, currency, description  
- Backend creates a **Stripe Checkout Session**  
- Returns:  
  ✔ Payment URL  
  ✔ Expiry timestamp (15 min auto-expire)  
  ✔ Shortened payment link  

### 🔲 2. QR Code Generation (PNG & PDF)
- Each payment session supports QR-based checkout  
- QR stored as PNG or rendered as PDF  
- Scanning redirects directly to Stripe payment page  

### 🔄 3. Real-Time Payment Tracking (Webhooks)
Stripe webhook listens for:
- `checkout.session.completed`
- `payment_intent.payment_failed`
- `checkout.session.expired`

Order & session status update **instantly**.

### 🧃 4. PDF Receipt Generation + Email Delivery
On successful payment:
- ✔ PDF receipt generated  
- ✔ Stored securely  
- ✔ Available for download  
- ✔ Optional email delivery  

Receipt includes: order ID, amount, currency, timestamps, customer details.

### 🔐 5. JWT Security with Redis Blacklisting
- Access & Refresh tokens generated at login  
- On logout → token hash stored in Redis blacklist  
- Every request checks Redis  

### 📜 6. Order History API
Users can view:
- Order metadata  
- Payment status  
- Creation & completion times  
- Downloadable receipt link  

### 🔁 7. Auto-Expiring Payment Links (15 mins)
- Stripe link auto-expires  
- Background scheduler marks as **EXPIRED**  

### 🔗 8. Short Payment URLs (Bonus Feature)

---

## 🛠 Additional Bonus Features Implemented

✔ Multiple currency support (USD, EUR, etc.)  
✔ QR downloadable in PNG & PDF  
✔ Email receipt sending with attachment  
✔ Webhook signature validation  
✔ Detailed event logging  

---

## 🧪 Sample API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create order |
| POST | `/api/payments/{orderId}/session` | Generate Stripe payment session |
| GET | `/api/payments/sessions/{id}/status` | Check payment status |
| GET | `/api/qr/{sessionId}/png` | Download QR (PNG) |
| GET | `/api/qr/{sessionId}/pdf` | Download QR (PDF) |
| GET | `/api/receipts/{id}/download` | Download PDF receipt |
| POST | `/api/receipts/email` | Email receipt |
| POST | `/api/auth/login` | Authenticate |
| POST | `/api/auth/logout` | Secure logout (Redis blacklist) |
| POST | `/api/webhooks/stripe` | Stripe webhook endpoint |

---

## 🔒 Security Highlights

- JWT Access & Refresh tokens  
- Redis-backed token revocation  
- Role-based access (USER / ADMIN)  
- Secure exception handling  
- Input validation  
- No exposure of sensitive data  

---

## 🔗 Swagger Documentation

http://localhost:2123/swagger-ui/index.html

---

## ✨ Created By

Made with by **Xadija Pashayeva**  

