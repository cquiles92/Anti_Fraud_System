# Anti_Fraud_System

This project demonstrates (in a simplified form) the principles of anti-fraud systems in the financial sector using a REST API. 

Description
------------
https://hyperskill.org/projects/232

A project from the JetBrains Hyperskill Academy.

This project is managed by roles (Administrator, Support, Merchant) to process transactions based off heuristic rules for Transaction valudation based off Account limitations, Geo Restrictions and IP validation.
Each endpoint is secured using Spring Security for Authorization and requests are validated.

Instructions
-------------
Requirements:
Java 11
Spring Framework
Gradle
H2 Database (Can be modified to use Repository of choice)

In order to begin using this system, users must be registered and unlocked and assigned appropriate roles for the required services.

Registering Users
-------------
***"/api/auth/user/"***
POST

JSON RegisteredUser

String Name, String Username, String Password
(1st account is always assigned Admin)
(Every account after admin is a "Merchant")

This is an open endpoint assigned for the creation of new User accounts. By default the first account registered will be assigned Administrator and unlocked. Every account afterwards will be assigned Merchant and Locked.

Roles
-------------
Administrator
This role is assigned to the first account made in the system and cannot be disabled or deleted. This role is responsible for the following: Locking/Unlocking accounts, Assigning roles, and Deleting users.

Support
This role is responsible for reporting suspicious (banned) IP addresses, suspicious card numbers, altering and modifying transactions, and accessing Transaction history.

Merchant
This role is responsible for uploading and sending Transactions to the Anti Fraud System for approval or denial.


Administrator Endpoints
-------------
***"/api/auth/user/{username}"***
DELETE

This Delete endpoint allows Administrators to delete user accounts that are not with the role Administrator in the system using the correct username as part of the path.

***"/api/auth/list"***
GET

This endpoint returns a list of registered users in the system. This endpoint is enabled for Support as well as Administrator roles. 

***"/api/auth/role"***
PUT

JSON RoleModificationRequest

String Username, String Role (Merchant, Support)

This endpoint allows users with the role Administrator to reassign roles of a user account. Roles supported to be modified are Merchant and Support roles. This request requires a RoleModificationRequest JSON.

***"/api/auth/access"***
PUT

JSON UserLockRequest

String Username, String Operation (LOCK, UNLOCK)

This endpoint allows users with the role Administrator to lock or unlock a user account. Roles supported to be modified are Merchant and Support roles. This request requires a UserLockRequest JSON.

Support Endpoints
-------------
***"/api/auth/list"***
GET

This endpoint returns a list of registered users in the system. This endpoint is enabled for Support as well as Administrator roles. 

***"/api/antifraud/transaction"***
PUT

JSON TransactionFeedback

Long transactionId, String feedback

This endpoint allows Support to modify and ovveride heuristic rules set by the validation rules. It is necessary for transactions that require "Manual Processing". After feedback account limits are adjusted. This request requires a TransactionFeedback JSON.

***"/api/antifraud/stolencard"***
POST

JSON StolenCard

String number

This endpoint allows Support to report and save a credit card number that should be blacklisted. This request requires a StolenCard JSON with a valid credit card as the body.

***"/api/antifraud/stolencard/{number}"***
DELETE

Path Variable

String number

This endpoint allows Support to delete a blacklisted card. This request requires a valid credit card number as the path variable.

***"/api/antifraud/stolencard"***
GET

This endpoint allows a user with the role Support to get a list of all reported blacklisted cards in the system.

***"/api/antifraud/suspicious-ip"***
POST

JSON IPAddress

String ip

This endpoint allows Support to report and save an ip address that should be blacklisted. This request requires a IPAddress JSON with a valid ip address as the body.

***"/api/antifraud/suspicious-ip/{ip}"***
DELETE

Path Variable

String ip

This endpoint allows Support to delete a blacklisted ip address. This request requires a valid ip address as the path variable.

***"/api/antifraud/suspicious-ip"***
GET

This endpoint allows a user with the role Support to get a list of all reported blacklisted ip addresses in the system.

***"/api/antifraud/history"***
GET

This endpoint allows a user with the role Support to get a list of all reported transactions in the system.

***"/api/antifraud/history/{number}"***
GET

Path variable

String number

This endpoint allows a user with the role Support to get a list of all reported transactions of a card number in the system. This request requires a valid credit card number as the path variable.

Merchant Endpoints
-------------
***"/api/antifraud/transaction"***
POST

TransactionRequest JSON

Long amount, String ip, String number, Code region (EAP, ECA, HIC, LAC, MENA, SA, SSA), LocalDateTime date

This POST endpoint allows Merchants to send requests to this service to process a given Transaction which may be approved or denied after verification and support. Requires a TransactionRequest JSON.

Transaction Rules
-------------
Cards that have not been initialized and saved in the system have a default value of . Cards that exceed this value use the secondary limit for Manual Processing. This limit is . If a limit exceeds this it is prohibitted. If the amount is below $1, the transaction throws an exception. The values for transactions use the chart below and the following formulas to adjust the Allowed values and Manual Processing values.

The formula for increasing the limit:

new_limit = 0.8 * current_limit + 0.2 * value_from_transaction

The formula for decreasing the limit:

new_limit = 0.8 * current_limit - 0.2 * value_from_transaction

| Feedback →        |               |                   |               |
| Validity ↓        | ALLOWED       | MANUAL_PROCESSING | PROHIBITED    |
| ----------------- |:-------------:|:-----------------:|:-------------:|
| ALLOWED           |   Exception   |   ↓ max ALLOWED   | ↓ max ALLOWED |
|                   |               |                   | ↓ max MANUAL  |
| MANUAL_PROCESSING | ↑ max ALLOWED |     Exception     | ↓ max MANUAL  |
|                   |               |                   |               |
| PROHIBITED        | ↑ max ALLOWED |   ↑ max MANUAL    |   Exception   |
|                   | ↑ max MANUAL  |                   |               |

Transactions are validated in the following order:
Amounts are processed (default allowed value <= 200, default manual value <= 1500) 

A transaction containing a card number is PROHIBITED if:

There are transactions from more than 2 regions of the world other than the region of the transaction that is being verified in the last hour in the transaction history;
There are transactions from more than 2 unique IP addresses other than the IP of the transaction that is being verified in the last hour in the transaction history.
There are transactions with amounts requesting more than the manual limit (default = 1500).
The card used for the transaction is blacklisted in the system.
The ip address used for the transaction is blacklisted in the system.

A transaction containing a card number is sent for MANUAL_PROCESSING if:

There are transactions from 2 regions of the world other than the region of the transaction that is being verified in the last hour in the transaction history;
There are transactions from 2 unique IP addresses other than the IP of the transaction that is being verified in the last hour in the transaction history.
There are transactions with amounts requesting more than the allowed limit (default = 200) and below manual limit (default = 1500).

List of JSON RequestBody
-------------
RoleModificationRequest
String User, String Role (Merchant, Support)

TranactionFeedback
Long transactionId, String feedback

TransactionRequest
Long amount, String ip, String number, Code region (EAP, ECA, HIC, LAC, MENA, SA, SSA), LocalDateTime date

UserLockRequest
String Username, String Operation (LOCK, UNLOCK)

Technologies
------------
Built using:
- Language: Java
- Frameworks: 
  - Spring Boot, 
  - Spring MVC, 
  - Spring JPA, 
  - Spring Security
- ORM: Hibernate
- Database: H2
- Build Tool: Gradle

Roadmap
----------
Possible create a front end to make the engine and API usable through a web browser with forms and a dynamic page. More features can be added to include updating ENUMs to include more Credit Cards and more Regions as well as including an IP Blacklist.
