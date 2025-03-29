# StoreX - E-Commerce Application 🛒

StoreX is a robust and scalable e-commerce application built using **Spring Boot**, designed to provide a seamless shopping experience for both customers and vendors. It manages core functionalities like product listings, order processing, user authentication, and cart management.

## Features ✨

- **User Authentication & Authorization**: Secure authentication using **Spring Security** with **JWT tokens** and **cookie-based authentication**.
- **Product Management**: Manage product listings, including CRUD operations with pagination and sorting.
- **Cart Management**: Add products to the cart, update quantities, and remove products from the cart.
- **Order Processing**: Users can place orders, track their status, and view order details.
- **Address Management**: Users can add, update, and manage their shipping addresses.
- **API Integration**: Multiple REST APIs for product, cart, order, address, and authentication management.
- **Custom Error Handling**: Standardized error responses with custom exception handling.

## Tech Stack 💻

- **Backend**: Spring Boot
- **Security**: Spring Security, JWT (JSON Web Token)
- **Database**: MySQL
- **Containerization**: Docker
- **Java**: Java 11
- **Persistence**: Spring Data JPA
- **Validation**: Bean Validation API
- **API Documentation**: Swagger (optional to add)

## Project Setup 🛠️

### Prerequisites

- **Java 11** or later
- **MySQL** (for local development, you can configure an online DB in `application.properties`)
- **Docker** (for containerization)

### Clone the Repository

```bash
git clone https://github.com/kumarsachinnnn299/StoreX.git
cd StoreX
