@api

Feature: Products API

  Scenario: Get all products request returns a non-empty list
    When I send a GET request to "/api/productsList"
    Then the response code should be 200
    And the response should contain a list of products
