# Online Bookstore 
## Project Description
This is an online bookstore that provides users with a convenient online book shopping platform with various useful features. Users can easily search, order, and purchase their favorite books, while administrators can effectively manage product information, customer data, and sales revenue.<br>
Key features:<br>
* **User :** Register/login, view book information, search, add to cart/wishlist, checkout, manage personal information, apply discount codes<br>
* **Admin :** View sales/profit statistics, manage books/authors/genres/publishers, manage user accounts, manage discount codes<br>

## Technologies Used
### Front-end Technologies :
* **Thymeleaf**
* **HTML5, CSS3, JavaScript**
* **Bootstrap**
### Backend-end Technologies :
* **Spring Boot**
* **Spring JPA (Java Persistence API)**
* **Spring Security**
### Database :
* **MySQL**

## Getting Started
To run the online bookstore application locally, follow these steps:<br>
### Prerequisites
* Java 11 or higher installed 
* MySQL database server installed and running 
* Maven installed
### Setup Database
1. Create a new MySQL database for the bookstore application, e.g., <span style="color:green">**bookstore_db**.
2. Update the database connection details in the application.properties file located in the src/main/resources directory:
```
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore_db
spring.datasource.username=your-mysql-username
spring.datasource.password=your-mysql-password
```

### Build and Run the Application
**In the BookStoreApplication file, there will be a piece of code used to add data to the database**
1. For the first run, leave the code in the BookStoreApplication class as is. This will allow the application to automatically add some initial data like users, authors, categories, etc. into the database.<br>
2. For subsequent runs, open the BookStoreApplication class and comment out the code between the "COMMENT START" and "COMMENT END" markers. This will prevent the application from adding the same data over and over again during each run.
```java
@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    // COMMENT START - COMMENT FROM HERE
    @Bean
    public CommandLineRunner commandLineRunner()
    {
        return runner -> {/*....*/};
    }
    public Blob getImageBlobFromPath() {/*....*/}
    
    // COMMENT END 

}
```
3. There are 2 accounts already added:
   1. **Admin account** :
      * Username: admin 
      * Password: 1 
      * Role: ROLE_ADMIN
      
   2. **User account** :
      * Username: user
      * Password: 1
      * Role: ROLE_USER

#### Remember to comment out the entire "COMMENT START" to "COMMENT END" block in subsequent runs to prevent duplicate data from being added.














