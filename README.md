# 🗃️ Asset Management System – Backend API

A Spring Boot-powered REST API for efficient registration, categorization, depreciation, and maintenance tracking of physical assets. This system is designed for scalability and flexibility, allowing users to define their own asset categories, calculate asset depreciation using multiple methods, track maintenance history, and manage related documents.

> 🔒 Secure, extensible, and tested via Postman.

---

## 🚀 Features

### ✅ Asset Management
- CRUD operations for assets
- Fields: name, category, purchase cost/date, status, image URL, etc.

### 🧩 Dynamic Categories
- Users can create and manage custom categories
- Promotes flexible classification (e.g., Electronics, Vehicles, Buildings)

### 🔧 Maintenance Tracking
- Each asset can have multiple maintenance records
- Fields: date, provider, service type, cost, notes

### 📉 Depreciation Calculation
- Supports multiple depreciation methods:
  - Straight-Line Method
  - Declining Balance Method
- Per-asset configuration and calculated current value

### 📂 Document Management
- Upload and associate files (e.g., receipts, warranties) with assets
- File metadata stored alongside assets

### 🔐 Authentication & Authorization
- Role-based access control using Spring Security
- Basic user/admin roles

---

## 🛠️ Tech Stack

- **Backend:** Java + Spring Boot  
- **ORM:** JPA / Hibernate  
- **Security:** Spring Security  
- **Database:** MySQL / PostgreSQL  
- **Testing:** Postman  
- **Build Tool:** Maven

---

## 📌 Future Enhancements

- 🌐 Frontend (React, Angular, or Flutter)
- 🏢 Multitenancy support
- 📊 Reporting and dashboards
- ⏰ Scheduled email/alert notifications
- 📁 CSV/Excel import/export
- 🕵️ Audit logging
- 📍 Geolocation tagging
- 🔎 QR code/barcode integration

---

## 🧠 Learning Highlights

- Built RESTful APIs using layered architecture (Controller → Service → Repository)
- Implemented secure role-based access control
- Integrated file/document handling tied to assets
- Modeled flexible depreciation logic across asset types
- Gained hands-on experience designing real-world backend services

---

## 📬 API Testing

All endpoints were tested using **Postman**. Sample requests and test collections will be included soon.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

