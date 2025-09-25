@api
Feature: Products API

  Background:
    Given the API base URL is loaded from config

  Scenario: Get all products request returns a non-empty list
    When I send a GET request to "/api/productsList"
    Then the response code should be 200
    And the response body at path "products" is a non-empty list

  Scenario: Search for products by keyword
    When I send a POST request to "/api/searchProduct" with body:
      | search_product | top |
    Then the response code should be 200
    And the response body at path "products" is a non-empty list
