# StoreX - E-Commerce Application 🛒

StoreX is a robust and scalable e-commerce application built using **Spring Boot**, designed to provide a seamless shopping experience for both customers and vendors. It manages core functionalities like product listings, order processing, user authentication, and cart management.

## 🔗 Live Demo

👉 [Access the Application](https://spring-boot-e-commerce-application.onrender.com/swagger-ui/index.html)\
(Note: The application may take 2–3 minutes to start, as the clicking on the link initiates its deployment process again. It is not permanently deployed.) 

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

## Prerequisites

- **1. Java 11** or later
- **2. Spring Boot 3.x**
- **3. MySQL** for database
- **4. Swagger** for API documentation

## Getting Started 🛠️



### 1. Clone the Repository

```bash
git clone https://github.com/kumarsachinnnn299/StoreX.git
cd StoreX
```
### 2. Configure MySQL
- Set up an online MySQL database or configure it locally.

- Update the application.properties file with the database credentials:

```
spring.datasource.url=jdbc:mysql://your-database-url:3306/storex
spring.datasource.username=your-username
spring.datasource.password=your-password
```

### 3. Run the Application 🚀


```
./mvnw spring-boot:run
```
The application will be accessible at :
```http://localhost:8080.```

### 4. Access Swagger API documentation:
The swagger api documentation will be accessible at:
```
http://localhost:8080/swagger-ui/index.html
```


## Project Structure
- `controller`: Handles REST API endpoints.
- `service`: Contains business logic.
- `repository`: Handles database interactions.
- `entity`: Contains entity classes for database tables.
- `config`: Configurations for security, encryption, and application settings.

## Future Enhancements

1. Implement product reviews and ratings.
2. Integrate payment gateways for handling real-time payments.
3. Add promotional codes and discount functionalities.
4. Enhance the front-end using ReactJS to build a complete user interface.

## License
This project is licensed under the MIT License.
