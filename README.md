# Getting Started

```

mysql > create database bankapp;
mysql > create user 'test'@'%' identified by 'test'; -- Creates the user
mysql > grant all on bankapp.* to 'test'@'%'; -- Gives all privileges to the new user on the newly created database

```

### DDL
```
create table account (id bigint not null auto_increment, currency varchar(255), account_number varchar(255), available_balance double precision, date_time_created datetime, last_update_date_time datetime, status varchar(255), total_balance double precision, user_id bigint, primary key (id))
create table transaction (id bigint not null auto_increment, currency varchar(255), amount double precision, transaction_date_time datetime, transaction_type varchar(255), account_transfer_from_id bigint, account_transfer_to_id bigint, create_by_id bigint, deposit_withdrawal_account_id bigint, primary key (id))
create table users (id bigint not null auto_increment, address varchar(255), email varchar(255) not null, full_name varchar(255), password varchar(255), phone_number varchar(255), role varchar(255) not null, salt varchar(255), primary key (id))
create table exchange_rate (id bigint not null auto_increment, from_currency varchar(255), name varchar(255), rate decimal(19,11), symbol varchar(255), to_currency varchar(255), updated_date date, primary key (id))
```

Account
---
#### Adding New Account
```
curl --location --request POST 'http://localhost:8080/api/accounts' \
--header 'Content-Type: application/json' \
--data-raw '{
    "fullName" : "fullName",
    "email": "email5@gmail",
    "address": "address",
    "phoneNumber": "phoneNumber",
    "currency": "PHP",
    "deposit": 10000
}'
```
#### Deleting Account
```
curl --location --request PUT 'http://localhost:8080/api/accounts/{account_id}/deactivate' \
--header 'Content-Type: application/json'
```

#### Getting Account Using Account Number
```
curl --location --request GET 'http://localhost:8080/api/accounts?accountNumber={account_number}' \
--header 'Content-Type: application/json'
```

#### Getting All Accounts
```
curl --location --request GET 'http://localhost:8080/api/accounts/all' \
--header 'Content-Type: application/json'
```

Transaction
---
#### Deposit 
```.env
curl --location --request POST 'http://localhost:8080/api/transactions/deposit' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountNumber" : {accountNumber},
    "amount": {amount},
    "currency": {currency}
}'
```
#### Withdraw
```.env
curl --location --request POST 'http://localhost:8080/api/transactions/withdraw' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountNumber" : {accountNumber},
    "amount": {amount},
    "currency": {currency}
}'
```

#### Transfer
```.env
curl --location --request POST 'http://localhost:8080/api/transactions/transfer' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountTransferFrom" : "6779843337",
    "accountTransferTo": "5412646103",
    "amount": "500",
    "currency": {currency}
}'
```

#### Transaction History
```.env
curl --location --request GET 'http://localhost:8080/api/transactions/history/0251149930'
```

Foreign Exchange Rate
---
#### Updating Base currency from FOREX API PROVIDER
```.env
curl --location --request GET 'http://localhost:8080/api/exchangerate/refresh/PHP'
```

#### Updating Base currency from FOREX API PROVIDER
This will query the table `exchange_rate` from the database and will return only records that match. 
```.env
curl --location --request GET 'http://localhost:8080/api/exchangerate?fromCurrency=PHP&toCurrency=USD,CAD,GBP'
```

#### Scheduler
During the start of the application, it will refreshed the forex stored in the database based on the base currency provided in the property file
You can add more base currency as needed
```.env
# Base currencies that need to be stored in DB during scheduling of exchange rate
exchangerate.default.base=PHP,USD,CAD

```
Forex update will run every 12:00 in the morning base on the cron configuration
```.env
exchangerate.scheduler.cron=0 0 * * *
```


