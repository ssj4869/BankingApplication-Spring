# BankingApplication-Spring

Banking Application using Spring, JWT, Cassandra:

Dependencies used:

1) Spring Web
2) Spring Cassandra starter
3) Lombok
4) Spring Security
5) JSON web token
6) spring boot starter test

Project Structure:

Config:
-	ApplicationConfig.java:
-	CassandraConfig
-	JwtAuthenticationFilter
-	SecurityConfig

Controller:
-	AuthenticationController: Controller to interact with /signup and /login apis
-	BankingController: Controller API to interact with banking features like deposit, withdraw, balance, statement, transfer

Service:
-	AuthenticationService: A service class used for handling AuthenticationController requests and sending responses back to the controller classes. Contains Business Logic for /signup and /register endpoints
-	
-	BankingService: A service class used for handling BankingController requests and sending responses back to the controller classes. Contains Business Logic for /transfer , /deposit, /withdraw, /balance, /statement endpoints
-	JwtService: Generating tokens for successful logins and extracting username from JWT token. Other methods include setting expiry for token, checks if the token is expired or not



DTO: Data Transfer Object. A Design Pattern used for encapsulating data and making an efficient flow of data between controller classes and service classes. Provides a structured data of what the service layer is expecting from controller classes
-	DepositRequest
-	LoginUserDTO
-	RegisterUserDTO
-	TransferRequestDTO
-	WithdrawRequest

Model classes:
-	AccountDetails – a POJO class to map “account_details” table in Cassandra
-	Transaction – a POJO class to map “transaction” table in Cassandra
-	User – a POJO class to map “User” table in Cassandra

Repository: for managing different model classes / tables and providing CRUD operations
-	AccountDetailsRepository
-	TransactionRepository
-	UserRepository


Default tables to create before running application:
-	CREATE KEYSPACE bankingapp WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

#Create Users Table

CREATE TABLE bankingapp.users (
    email text PRIMARY KEY,
    createdat timestamp,
    fullname text,
    password text,
    updatedat timestamp
)


#Create account_details table

CREATE TABLE bankingapp.account_details (
    email text PRIMARY KEY,
    balance float
)







#Create transactions table:

CREATE TABLE bankingapp.transactions (
    transaction_id uuid PRIMARY KEY,
    amount float,
    email text,
    transaction_date timestamp,
    transaction_to text,
    transaction_type text
)



Important Endpoints to interact with API and Database:

AuthenticationController.java
1)	HTTP POST “/auth/signup”
Example request in postman:
{
   		 "email": "testuser1223@gmail.com",
   		 "password": "123456",
   		 "fullName": "Test User"
}

2)	HTTP POST “/auth/login”
This will generate a token. Use it for interacting with /api/banking API
Put this token in Authorization tab. Select token type Bearer Token and paste the token
{
   		 "email": "ssj@gmail.com",
  		  "password": "123456"
}





Note: Every “api/banking” will require a bearer token to perform further actions


BankingController.java

3)	HTTP POST “api/banking/deposit”
Deposits an amount to your account.
{
   	 "amount": "1000.00"
}






4)	HTTP POST “/api/banking/transfer”
Transferring money between two user accounts.
{
   		 "destinationAccountId" : "testuser1223@gmail.com",
   		 "amount" : 1000
}

5)	HTTP GET “/api/banking/balance”
- Gets current user balance


6)	HTTP POST “api/banking/withdraw”
{
    "amount": 100.00
}


7)	HTTP GET “api/banking/statement”
Gets a list of transactions you’ve made. 




