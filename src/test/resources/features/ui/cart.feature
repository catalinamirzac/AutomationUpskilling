@ui @Run
Feature: Cart

  Scenario: Add one product to cart and verify it appears
    Given the user is on the Automation exercise home page
    When the user navigates to the products page
    And the user adds "Blue Top" to the cart
    And the user views the cart
    Then the cart should show "Blue Top" with quantity 1
