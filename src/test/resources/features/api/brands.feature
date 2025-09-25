@api
Feature: Brands API

  Background:
    Given the API base URL is loaded from config

  Scenario: Get all brands request returns a non-empty list
    When I send a GET request to "/api/brandsList"
    Then the response code should be 200
    And the response should contain a list of brands

  Scenario: Unsupported HTTP method returns 405 error code
    When I send a PUT request to "/api/brandsList"
    Then the response code should be 200
    And the response body should contain responseCode 405 and message "This request method is not supported."
