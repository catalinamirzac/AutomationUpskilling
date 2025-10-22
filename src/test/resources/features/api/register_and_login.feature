@api
Feature: Register and Login with newly created User via API
  Scenario: Register and login with the same (saved) credentials
    Given the API base URL is loaded from config
    When I send a POST request to "createAccount" with body:
      | name          | faker |
      | email         | faker |
      | password      | faker |
      | title         | faker |
      | birth_date    | faker |
      | birth_month   | faker |
      | birth_year    | faker |
      | firstname     | faker |
      | lastname      | faker |
      | company       | faker |
      | address1      | faker |
      | address2      | faker |
      | country       | faker |
      | state         | faker |
      | city          | faker |
      | zipcode       | faker |
      | mobile_number | faker |
    Then the response code should be 200
    And the response body should contain field "responseCode"

    And I send a POST request to "login" with body:
      | email    | saved |
      | password | saved |
    Then the response code should be 200
    And the response JSON message should be "User exists!"