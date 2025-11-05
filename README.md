Automation Upskilling – UI & API Test Framework

A beginner-friendly, production-style test framework that exercises both the UI and the API of Automation Exercise
. It uses Cucumber BDD for readable scenarios, Selenium for browser automation, REST Assured for HTTP tests, and Allure for rich reports.
Overview

Goal: Provide clear, maintainable examples of UI and API automation with a clean code structure and stakeholder-friendly reporting.

Design patterns: Page Object (UI), Step Definitions per domain (UI/API), scenario-scoped context for data handoff, configuration via config.properties.

What you can do:

Run UI scenarios that log in, browse products, add to cart, and assert cart contents.

Run API scenarios that list products/brands and register/login with generated data.

Generate an Allure HTML report with screenshots, page source, console logs (UI) and request/response traces (API).

Tech Stack

Language/Build: Java 25, Maven

BDD: Cucumber 7 (JUnit 4 runner)

UI: Selenium 4 (Chrome)

API: REST Assured 5

Reporting: Allure 2

Assertions: AssertJ / JUnit

Data: JavaFaker

Logging: Log4j2

All versions are pinned in pom.xml.

Running Tests

All tests (UI + API):
mvn clean test

Only UI or only API (by tag):
mvn test -Dcucumber.filter.tags="@ui"
mvn test -Dcucumber.filter.tags="@api"

Headless mode (CI-friendly):
mvn test -Dheadless=true