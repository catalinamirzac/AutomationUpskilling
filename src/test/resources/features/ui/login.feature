Feature: User Login

  As a registered user of Automation exercise
  I want to securely login to my account
  So that I can view my profile and use shopping features


  @ui
  Scenario Outline: Login with various credentials
    Given the user is on the Automation exercise home page
    When the user navigates to the login page
    And the user enters "<email>" and "<password>"
    And  the user clicks the login button
    Then the login should <result>

    Examples:
      | email                  | password       | result       |
      | myuser@yahoo.com       | mypassword1    | succeed      |
      | wrong_user@test.com    | secret_sauce   | fail         |
      | standard_user@test.com | wrong_pass     | fail         |
