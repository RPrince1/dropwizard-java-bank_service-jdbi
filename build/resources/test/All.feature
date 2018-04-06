Feature: All

  Background:
    Given I have an embedded Postgres DB
    And The service is running
    And I am customer with account ID "123456789"


  Scenario: Perform POST of a new customer transaction
    When I perform a POST on "/customers/transactions/@me" with data "src/test/resources/json/POST_transaction.json"
    Then The response code should be "202"
    Then 1 record should exist in the "customer_transactions" table


  Scenario: Perform GET of customer transactions
    Given A transaction exists in the database from file "src/test/resources/json/POST_transaction.json"
    When I perform a GET on "/customers/transactions/@me"
    Then The response code should be "200"
    Then I expect 1 transaction in the response


  Scenario: Perform GET of customer transactions when none exist
    When I perform a GET on "/customers/transactions/@me"
    Then The response code should be "404"