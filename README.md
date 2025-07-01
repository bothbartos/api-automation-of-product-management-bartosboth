# REST API Automation Framework

This is a REST API testing framework built with Java 22, demonstrating automated testing of a complete product
management workflow (CRUD operations) using modern testing practices and tools.

## 🎯 Project Overview

This project implements automated API tests for an e-commerce product management system, validating backend
functionality through a full product lifecycle. The framework demonstrates enterprise-level testing practices including
data-driven testing, schema validation, and reusable test configurations.

## 🛠️ Technology Stack

- **Java 22** - Programming language
- **REST Assured 5.5.5** - API testing framework
- **JSON Schema Validator 5.5.5** - Schema validation
- **JUnit Jupiter 5.11.0** - Test execution and assertions
- **AssertJ 3.26.3** - Fluent assertions
- **Maven 3.9+** - Dependency management and build tool
- **Jackson 2.19.0** - JSON data processing
- **OpenCSV 5.9** - CSV data processing

## 🚀 Features

### Core Functionality

- ✅ **Complete CRUD Operations**: Create, Read, Update, Delete product workflows
- ✅ **Data-Driven Testing**: CSV-based test data management
- ✅ **Schema Validation**: JSON schema contract testing
- ✅ **Response Specifications**: Reusable validation configurations
- ✅ **Request Specifications**: Standardized API request setup

## 🧪 Test Scenarios

### 1. Product Creation (POST /products)

- Validates successful product creation with auto-generated ID
- Verifies response data matches request payload
- Tests with multiple products from CSV data

### 2. Product Retrieval (GET /products/{id})

- Validates individual product retrieval by ID
- Confirms response structure and data integrity
- Includes JSON schema validation

### 3. Product Listing (GET /products)

- Validates complete product catalog retrieval
- Verifies response is properly formatted array
- Includes product count consistency checks

### 4. Product Updates (PUT /products/{id})

- Tests product modification functionality
- Validates response reflects updated values
- Handles API-specific response formats

### 5. Product Deletion (DELETE /products/{id})

- Validates successful product deletion
- Tests proper status code responses
- Demonstrates mock API behavior understanding

## 🔧 Setup & Installation

### Prerequisites

- Java 22 or higher
- Maven 3.9+
- Internet connection (for API calls)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone git@github.com:bothbartos/api-automation-of-product-management-bartosboth.git
   cd rest-api-automation
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Run all tests**
   ```bash
   mvn test
   ```

4. **Run specific test class**
   ```bash
   mvn test -Dtest=ProductManagementWorkflowTest
   ```

## 📋 Test Data Management

### CSV Structure

```csv
title,price,category,description,image
Test Gaming Console,449.99,electronics,Gaming console for automation testing,https://via.placeholder.com/300x300
Test Headphones,129.99,electronics,Wireless headphones for testing,https://via.placeholder.com/300x300
```

## 🔍 API Under Test

**Base URL**: `https://fakestoreapi.com`

### Available Endpoints

- `GET /products` - Retrieve all products
- `GET /products/{id}` - Retrieve specific product
- `POST /products` - Create new product
- `PUT /products/{id}` - Update existing product
- `DELETE /products/{id}` - Delete product
