@api
Feature: Create Account via API

  Scenario: Register a new user with required details
    Given the API base URL is loaded from config
    When I send a POST request to "/api/createAccount" with body:
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
