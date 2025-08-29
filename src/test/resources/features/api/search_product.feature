@api
Feature: Search Product via API

  Scenario: Search for products by keyword
    Given the API base URL is loaded from config
    When I send a POST request to "/api/searchProduct" with body:
      | search_product | top |
    Then the response code should be 200
    And the response body should contain field "products"