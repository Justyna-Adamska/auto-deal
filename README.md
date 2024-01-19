
<!-- TOC -->
* [I. Introduction](#i-introduction)
    * [1. Aim of the documentation](#1-aim-of-the-documentation)
    * [2. System description](#2-system-description)
* [II. System Architecture](#ii-system-architecture)
    * [Tech Stack:](#tech-stack-)
    * [Database](#database)
* [III. System Features](#iii-system-features)
  * [1. Admin Panel](#1-admin-panel)
  * [2. User Panel](#2-user-panel)
  * [3. Additional features](#3-additional-features)
* [IV. Security and Authentication](#iv-security-and-authentication)
* [V. Database](#v-database)
* [VI. User Interface](#vi-user-interface)
  * [1. UX/UI guidelines](#1-uxui-guidelines)
* [VII. Tests](#vii-tests)
  * [1. Unit tests](#1-unit-tests)
  * [2. Integration tests](#2-integration-tests)
  * [3. Tools for testing](#3-tools-for-testing)
* [_**VII. Instruction for starting app**_](#vii-instruction-for-starting-app)
<!-- TOC -->


# I. Introduction
### 1. Aim of the documentation
The aim of the following documentation is to deliver comprehensive and clear description of the application concerning online shop with admin panel, presenting key aspects of application, including main functionalities, system architecture as well as the guidelines for UX/UI.

### 2. System description
Online shop application, designed for easy maintenance for admin as well as intuitive interface for users. System allows adding new products and managing the (full CRUD cycle) through admin panel, registering new users,logging them and enabling them to place orders. 


# II. System Architecture
### Tech Stack: 
* Spring Boot  
* JPA (Hibernate)  
* Thymeleaf  
* Spring Security  
* JavaScript  
* Ajax  
* Dockerfile  
* GitHub Actions  

### Database

H2 Database for fast prototyping and easy integration

### Application division  
Application is divided into 3 main domains: order, user and product. There is also additional, auxiliary domain - cart. Each of mentioned domains is divided into packages typically used in Spring Boot applications:  
* controller  
* dto  
* mapper  
* model  
* repository  
* service  


# III. System Features
## A. Admin Panel

1. Adding product categories:  
   • Process of creating categories, including entering the name and parent identifier.  
   • Mechanisms for validating input data.
2. Category tree overview:  
   • User interface for browsing the hierarchy of categories.  
   • Search and repositioning functions for categories through dragging.  
3. Adding products:  
   • Forms for entering product data: name, description, image URL, stock status, price, product type (dropdown), category (dropdown), author (dropdown).  
   • Validation process for entered information.
4. Product list and editing:  
   • Displaying all products with editing options.  
   • Product search functions and navigating to edit a selected product.  


## B. User Panel  

1. Registration:  
   • Registration process with forms and data validation.  
2. Login:  
   • Mechanisms allowing user login and logout.  
3. Product List:  

   • Displaying products in list or grid format.  
   • Search options and adding products to the cart.  
4. Product Table with Pagination:  
   • Functionality to display products in a table with pagination options  
   • Using AJAX for sorting and searching of products  

5. Shopping Cart:
   • Adding products to the cart and order fulfillment process  
   • Displaying products in the cart with the option to place an order

Extensions and Additional Tasks:
• User account editing capability.
• Order overview for both users and administrators.
• Adding product author information in the admin panel.

Additional Requirements:
• Ensuring an aesthetically pleasing and functional way of presenting data.
• Validation of data obtained from users.




# IV. Security and Authentication  

Security Overview:  
• Utilizing Spring Security as the primary tool for ensuring application security.  
• Implementation of user authentication and authorization mechanisms.  

Authentication:  
• The process of verifying a user's identity, typically through a login (email address) and password.  
• Ability to integrate with external identity providers (e.g., OAuth, OpenID Connect).  

Authorization:  
• Controlling access to different parts of the application based on user roles and permissions.  
• Applying sophisticated access rules for various roles, such as ADMIN and USER.  

Session Management:  
• Handling user sessions to prevent session hijacking attacks.  
• Configurable mechanisms for logout and session invalidation.  

CSRF Protection:  
• Guarding against Cross-Site Request Forgery (CSRF) attacks by using CSRF tokens.  

Password Security:  
• Strong encryption of passwords (e.g., bcrypt) to protect user authentication data.  
• Implementation of password security policies (minimum length, complexity).  

SSL/TLS:  
• Encouraging the use of encrypted SSL/TLS connections to protect data transmitted between the client and server.  

Protection Against Attacks:  
• Guarding against common network attacks, such as SQL Injection, XSS (Cross-Site Scripting).  
• Regularly updating dependencies and components to patch known security vulnerabilities.  

Audit and Logging:  
• Implementing audit and logging mechanisms to monitor suspicious user activities.  
• Recording significant user actions, such as logins, attempts to access resources, and configuration changes.  


# V. Database  
Database: H2  
• The H2 database was chosen for its lightweight nature, speed, and ease of implementation, making it ideal for development and testing environments.  

• H2 allows for easy migration to larger production databases as the project scales.  


Tables

    User Table:
    Attributes:
    • user_id (INT): User identifier, primary key.
    • user_name (VARCHAR(255)): User name.
    • user_password (VARCHAR(255)): User password.
    • user_email (VARCHAR(255)): User email address.
    • user_phone (VARCHAR(255)): User phone number.

    Product Table:
    Attributes:
    • product_id (INT): Product identifier, primary key.
    • product_name (VARCHAR(255)): Product name.
    • product_price (DECIMAL): Product price.
    • product_code (VARCHAR(255)): Product code.
    • product_warranty (VARCHAR(255)): Product warranty.
    • product_prod_date (DATE): Product production date.
    • product_mileage (INT): Product mileage (if applicable).
    • product_color (VARCHAR(255)): Product color.
    • product_origin (VARCHAR(255)): Product origin.

    Admin Table:
    Attributes:
    • admin_id (INT): Administrator identifier, primary key.
    • admin_name (VARCHAR(255)): Administrator name.
    • admin_password (VARCHAR(255)): Administrator password.
    • admin_email (VARCHAR(255)): Administrator email address.
    • admin_last_log_date (TIMESTAMP): Administrator's last login date.

    Order Table:
    Attributes:
    • order_id (INT): Order identifier, primary key.
    • order_date (TIMESTAMP): Order date.
    • user_id (INT): User identifier, foreign key referencing User.

    OrderLine Table:
    Attributes:
    • orderLine_id (INT): Order line identifier, primary key.
    • order_id (INT): Order identifier, foreign key referencing Order.
    • product_id (INT): Product identifier, foreign key referencing Product.
    • orderLine_quantity (INT): Quantity of the product in the order line.
    • orderLine_totalPrice (DECIMAL): Total price of the order line.
    • orderLine_discount (DECIMAL): Discount for the order line.


Relationships  
User – Order: One to Many: One user can have multiple orders.  
Order – OrderLine: One to Many: One order can contain multiple order lines.  
Product – OrderLine: One to Many: One product can be part of multiple order lines.  



# VII. Tests
## 1. Unit tests
## 2. Integration tests
## 3. Tools for testing

# _**VII. Instruction for starting app**_

In order to start application using Docker on port 1234, just copy the following 2 lines to your Terminal with root privileges:

```
sudo docker build --tag=jjk .
sudo docker run -p1234:8080 jjk
```





