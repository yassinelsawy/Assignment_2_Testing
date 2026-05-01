# Assignment 2 — Software Testing

**Students ID:** 20230465 - 20230113 - 20230042

This repository contains two parts of the Software Testing assignment:

- **Part A** — Selenium WebDriver automation framework for [TutorialsNinja Demo](http://tutorialsninja.com/demo)
- **Part B** — Postman API test collection for [DummyJSON](https://dummyjson.com)

---

## Repository Structure

```
Assignment_2_Testing/
├── PartA/                          # Selenium automation framework (Java/Maven)
│   ├── pom.xml                     # Maven dependencies and build config
│   ├── testng.xml                  # TestNG suite — defines test execution order
│   ├── mvnw.cmd                    # Maven wrapper (Windows)
│   ├── run-tests-with-report.cmd   # One-click: run tests + generate Allure report
│   ├── open-report.cmd             # Open the last generated Allure report
│   ├── A2_PartA_Test Scenarios.xlsx # Manual test case documentation
│   └── src/
│       ├── main/java/com/tutorialsninja/
│       │   ├── config/ConfigReader.java      # Reads config.properties
│       │   ├── pages/                        # Page Object classes (one per page)
│       │   │   ├── HomePage.java
│       │   │   ├── LoginPage.java
│       │   │   ├── RegisterPage.java
│       │   │   ├── AccountPage.java
│       │   │   ├── CategoryPage.java
│       │   │   ├── ProductPage.java
│       │   │   ├── CartPage.java
│       │   │   ├── CheckoutPage.java
│       │   │   └── SearchPage.java
│       │   └── utils/ExcelReader.java        # Reads TestData.xlsx for data-driven tests
│       ├── main/resources/
│       │   └── config.properties             # Browser, URL, and timeout settings
│       └── test/
│           ├── java/com/tutorialsninja/
│           │   ├── base/BaseTest.java         # Setup/teardown + screenshot on failure
│           │   └── tests/                     # Test classes (one per feature)
│           │       ├── RegistrationTest.java  # TC01
│           │       ├── LoginTest.java         # TC02, TC03
│           │       ├── CurrencyTest.java      # TC04
│           │       ├── BreadcrumbTest.java    # TC05
│           │       ├── SortTest.java          # TC06
│           │       ├── SearchTest.java        # TC07, TC08
│           │       ├── SubcategorySearchTest.java # TC09
│           │       ├── CartTest.java          # TC10
│           │       └── CheckoutTest.java      # TC11
│           └── resources/
│               ├── allure.properties          # Allure results directory config
│               └── testdata/TestData.xlsx     # Test input data (one sheet per test)
│
└── PartB/                                     # Postman API tests
    ├── DummyJSON_Collection.json              # Postman collection (import this)
    ├── DummyJSON_Environment.json             # Postman environment (import this)
    └── PartB_Report.docx                      # Report template with screenshot placeholders
```

---

## Part A — Selenium Automation

### Prerequisites

| Requirement | Version |
|---|---|
| Java JDK | 17 or higher |
| Maven | 3.6+ (or use the included `mvnw.cmd`) |
| Google Chrome | Latest stable |
| Internet connection | Required (WebDriverManager downloads ChromeDriver automatically) |

> **Firefox alternative:** Change `browser=chrome` to `browser=firefox` in `config.properties`. Firefox must be installed.

### Configuration

Edit `PartA/src/main/resources/config.properties`:

```properties
browser=chrome
url=http://tutorialsninja.com/demo/index.php?route=common/home
implicit.wait=10
explicit.wait=15
testdata.path=src/test/resources/testdata/TestData.xlsx
```

### Running the Tests

**Option 1 — One-click (recommended on Windows):**

Double-click `run-tests-with-report.cmd`. It runs all tests and opens the Allure report automatically.

**Option 2 — Maven command line:**

```bash
cd PartA
mvn clean test
```

**Option 3 — Run a single test class:**

```bash
mvn clean test -Dtest=LoginTest
```

### Generating the Allure Report

After running tests, a `target/allure-results/` folder is created. To view the report:

```bash
# Using the helper script (Windows)
open-report.cmd

# Or manually with Allure CLI
allure serve target/allure-results
```

### Test Cases

| Class | TC# | Description |
|---|---|---|
| `RegistrationTest` | TC01 | Register a new user account with a unique email |
| `LoginTest` | TC02 | Login with valid credentials |
| `LoginTest` | TC03 | Login with invalid credentials (expects error) |
| `CurrencyTest` | TC04 | Switch between EUR, GBP, USD currencies and verify price changes |
| `BreadcrumbTest` | TC05 | Navigate into a category and verify the breadcrumb trail |
| `SortTest` | TC06 | Sort products A-Z and Z-A and verify order |
| `SearchTest` | TC07 | Search for a product and verify it appears in results |
| `SearchTest` | TC08 | Search for a non-existent product and verify empty results |
| `SubcategorySearchTest` | TC09 | Enable "search in subcategories" and verify it finds products hidden in sub-categories |
| `CartTest` | TC10 | Add multiple products from different categories and verify cart contents |
| `CheckoutTest` | TC11 | Complete the full checkout flow (add to cart → billing → confirm order) |

All tests are data-driven. Input data is read from `TestData.xlsx` — each sheet matches a test class (e.g., sheet `Login` feeds `LoginTest`).

### Framework Architecture

```
BaseTest  (setup / teardown / screenshot on failure)
    │
    └── All Test Classes extend BaseTest
            │
            └── Page Objects (one class per website page)
                    │
                    └── WebDriver (managed by WebDriverManager — no manual ChromeDriver download needed)
```

**Key design decisions:**
- **Page Object Model (POM):** Every page of the website has its own Java class. Tests call page methods, never manipulate the DOM directly.
- **Data-driven via Excel:** `ExcelReader.java` reads `TestData.xlsx` using Apache POI. The `@DataProvider` annotation feeds rows directly into test methods.
- **Allure reporting:** `@Description` on each test method provides human-readable labels in the report. Screenshots are automatically attached on test failure.
- **WebDriverManager:** Automatically downloads the correct ChromeDriver version — no manual setup needed.

---

## Part B — Postman API Tests

### Target API

[DummyJSON](https://dummyjson.com) — a free REST API that simulates an e-commerce backend.

### How to Import

1. Open Postman
2. Click **Import**
3. Import `DummyJSON_Environment.json` first (sets `base_url` and `auth_token`)
4. Import `DummyJSON_Collection.json`
5. Select the **DummyJSON - 20230465** environment from the top-right dropdown

### Collection Structure

```
A2-20230465
├── Auth
│   ├── Login                    POST  /auth/login       → saves access token to environment
│   └── Get Current User         GET   /auth/me           → uses saved token
│
└── Products
    ├── Get All Products          GET   /products
    ├── Get Single Product        GET   /products/1
    ├── Search Products           GET   /products/search?q=phone
    ├── Add Product               POST  /products/add
    ├── Update Product            PUT   /products/1
    ├── Delete Product            DELETE /products/1
    └── Get Invalid Product       GET   /products/99999   (negative test → expects 404)
```

### Authentication Flow

The **Login** request automatically saves the access token to the environment variable `auth_token`. **Get Current User** then uses it via `Authorization: Bearer {{auth_token}}`. Run them in order, or use the Collection Runner.

### Test Scripts

Every request has `pm.test()` assertions covering:

- Correct HTTP status code
- Required fields present in the response body
- Response time under 3000 ms

### Running the Full Suite

1. Click the collection name → **Run collection**
2. Keep the default order (Login must run before Get Current User)
3. Click **Run A2-20230465**

Results show pass/fail per assertion. Screenshots of results go in `PartB_Report.docx`.

---

## Dependencies Summary (Part A)

| Library | Version | Purpose |
|---|---|---|
| Selenium Java | 4.18.1 | Browser automation |
| TestNG | 7.9.0 | Test runner and assertions |
| WebDriverManager | 5.7.0 | Auto-downloads ChromeDriver |
| Apache POI | 5.2.5 | Read `.xlsx` test data |
| Allure TestNG | 2.25.0 | HTML test reports with screenshots |
| AspectJ Weaver | 1.9.21 | Required by Allure at runtime |
| SLF4J Simple | 2.0.12 | Logging |
