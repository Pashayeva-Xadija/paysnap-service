🚀 PaySnap — QR-Based Payment Link Generator API

PaySnap is a secure, Stripe-powered payment system that generates checkout links, QR codes, and PDF receipts for fast and contactless transactions.
The platform tracks payments in real time via Stripe webhooks and ensures robust security using JWT authentication with Redis-backed token blacklisting.

Built with Spring Boot 3, PostgreSQL, Redis, ZXing, Stripe Java SDK, and OpenPDF, PaySnap provides a modern and reliable payment infrastructure.

🧭 Overview

PaySnap enables users to:

Create secure Stripe payment sessions on demand

Share instant QR codes for mobile-friendly checkout

Track payment status in real time

Automatically generate PDF receipts after successful payments

Download or receive receipts by email

Maintain complete order & payment history

Logout safely using Redis-based JWT blacklisting

Use short and simple shareable payment URLs

🧱 Architecture Highlights
Component	Technology	Purpose
Backend Framework	Spring Boot 3	Core application framework
Payment Engine	Stripe Java SDK	Checkout session + payment tracking
QR Codes	ZXing	Dynamic QR image generation
Database	PostgreSQL	Persistent order & user data
In-Memory Store	Redis	JWT blacklist + session expiry
Background Tasks	Spring Scheduler	Expired session cleanup
PDF Generator	OpenPDF	Receipt creation
Email Sender	Spring Mail	Automated receipt delivery
Auth	JWT + Spring Security	Secure API access
Docs	Swagger / OpenAPI	Developer documentation
🎯 Key Features & How They Work
🧾 1. Order Creation & Payment Link Generation

Users create an order by specifying amount, currency, and description

Backend creates a Stripe Checkout Session

Returns:
✔ Payment URL
✔ Expiry timestamp (15-minute auto-expire)
✔ Shortened payment link

🔲 2. QR Code Generation (PNG & PDF)

Each payment session automatically supports QR-based checkout

QR stored as PNG or rendered as PDF

Scanning redirects directly to Stripe payment page

🔄 3. Real-Time Payment Tracking (Webhooks)

Stripe webhook endpoint listens for events:

checkout.session.completed

payment_intent.payment_failed

checkout.session.expired

Order & session status are updated instantly

🧃 4. PDF Receipt Generation + Email Delivery

On successful payment:
✔ PDF receipt generated
✔ Stored securely
✔ Available for download
✔ Optional email delivery with attachment

Receipt includes: order ID, amount, currency, timestamps, and customer details.

🔐 5. JWT Security with Redis Blacklisting

Access & Refresh tokens generated at login

On logout, token is hashed and blacklisted in Redis

Every request checks Redis to prevent reused/expired tokens

📜 6. Order History API

Users can view full payment history including:

Order metadata

Payment status

Creation & completion timestamps

Downloadable receipt link

🔁 7. Auto-Expiring Payment Links (15 mins)

Stripe link automatically expires in 15 minutes

Scheduler marks sessions as EXPIRED after TTL

Prevents unauthorized or outdated payments

🔗 8. Short Payment URLs (Bonus Feature)

Each Stripe URL gets a unique 8-character short link
Example:
https://paysnap.dev/s/Ab91ExQz

🛠 Additional Bonus Features Implemented

✔ Multiple currency support (USD, EUR, etc.)

✔ QR Code downloadable in PNG and PDF formats

✔ Email receipt sending with attachment

✔ Webhook signature validation for security

✔ Detailed logging for every payment event

🧪 Sample API Endpoints
Method	Endpoint	Description
POST	/api/orders	Create order
POST	/api/payments/{orderId}/session	Generate Stripe payment session
GET	/api/payments/sessions/{id}/status	Check payment status
GET	/api/qr/{sessionId}/png	Download QR code (PNG)
GET	/api/qr/{sessionId}/pdf	Download QR code (PDF)
GET	/api/receipts/{id}/download	Download PDF receipt
POST	/api/receipts/email	Email receipt
POST	/api/auth/login	Authenticate
POST	/api/auth/logout	Secure logout (Redis blacklist)
POST	/api/webhooks/stripe	Stripe webhook endpoint
🔒 Security Highlights

JWT Access & Refresh tokens

Redis-backed token revocation

Role-based access (USER / ADMIN)

Secure exception handling

Input validation on all endpoints

No public endpoint exposes sensitive data


🔗 Swagger Documentation

Available at:
http://localhost:2123/swagger-ui/index.html

✨ Created By

Your Name
📧 your-email@example.com

🔗 LinkedIn: https://www.linkedin.com/in/your-profile