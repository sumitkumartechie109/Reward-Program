### Customer Reward Program Assignment

1  To run the application we have to run main method under RewardProgramApplication

2.Basically I have created 2 API's one is get and another is post
where using get I will fetch the details of the customer and the totalRewardsPoints recieved and using Post I will insert the transaction records in the database.

3.Here the database I have used is h2-console database and table name is transaction.

4.Here we have used the entity as Transaction which helps us to connect to database and to use the functions like save, find for that I have created Repository Interface -> TransactionRepository

5.I have also created a service and service implementation class to maintain the business logic

6.Now the logic that has been used for generating the reward points

6.1 The logic is written in method -> calculateRewardPoints(Double transactionAmount)

6.2 First, we check if the transaction amount is over $100. If it is, we calculate the amount spent over $100 by subtracting 100 from the transaction amount.

6.3 We then multiply this amount by 2 to get the points for the amount spent over $100.

6.4 Next, we check if the transaction amount is between $50 and $100. If it is, we calculate the amount spent between $50 and $100 by subtracting 50 from the minimum of the transaction amount and 100 (to ensure we don't go over $100).

6.5 We then add this amount to the points to get the total points for the transaction. Finally, we return the total points.

6.6 After returning we are using this method in two places one is while creating the records and one is for fetching the records

6.7 In fetching the records we are finding it using customer id and iterating all the customerId and displaying the response accordingly


7.Now the logic for inserting the records

7.1 Here we are normally taking the inputs such as customerId, amount and the transactiondate, rewards point

7.2 After that we are using the calculateRewardPoints method to generate the reward points

7.3 After that we are storing it in the transactions table

7.4 the logic is implemented in controller class -> createTransaction method. 

8.For handling the response for get request we have created a model class RewardPointResponse which used to display the map of string and integer for the monthly points and the total point for that particular customer

9.The API's that are Implemented

9.1 getRewardPointsBasedOnCustomerId -> this GET API takes customerId as a input parameter and fetch the records for that particular customer

9.2 createTransaction -> this POST API will take customerId, transactionDate, Amount and RewardPoints and will return the response as records/ transaction successfully inserted.

10.TestCases
RewardProgramControllerTests -> This class is used to create tests for 

10.1.whether the transaction records are created and runs successfully

10.2 getting the rewardPoint for single customer and 

10.3 check the validations like negative field, invalid test in the url and gives the expected outcome or not.

---------------------------------------------------------------------

### Below is the Examples

#### Sample input data records

insert into transactions(id, customer_id,transaction_date,amount, reward_point) values(1,1234,'2023-12-05',300,400);

insert into transactions(id,customer_id,transaction_date,amount, reward_point) values(2,1234,'2023-10-05',200,200);

insert into transactions(id,customer_id,transaction_date,amount, reward_point) values(3,1235,'2023-12-05',300, 400);

insert into transactions(id,customer_id,transaction_date,amount, reward_point) values(4,1235,'2023-11-05',400, 600);


H2 console which I have used for database url -> http://localhost:8080/h2-console/

### Get Request

#### Get Using Single customerID

url -> http://localhost:8080/api/v1/customer/1234

response ->

{
    
    "monthlyPoints": {
        "Oct": 200,
        "Dec": 400
    },
    "totalPoints": 600
}


### Post request

Note -> for the post request I used an application like postman to enter the request input and send the details

url -> http://localhost:8080/api/v1/createTransactions

raw json request ->

{

  "customerId": 1,
  
  "amount": 100.0,
  
  "transactionDate": "2022-01-01"

}

response

Transaction created successfully