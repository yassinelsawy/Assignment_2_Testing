# Assignment 2 - Test Automation Project

This is a Selenium WebDriver and TestNG-based test automation framework for TutorialsNinja application with Allure reporting.

## Prerequisites

- Java 17 or higher
- Maven 3.9.9 (or use the bundled mvnw.cmd)
- Chrome browser (for WebDriver)
- Optional: Allure CLI 2.25.0 (auto-installed by run-tests-with-report.cmd)

## How to Run Tests

### Option 1: Run Tests with Allure Report (Recommended)
This command automatically runs all tests and generates an HTML Allure report with detailed results.

```bash
.\run-tests-with-report.cmd
```

**What it does:**
- Cleans previous test results
- Runs all tests using Maven Surefire
- Generates Allure test report
- Automatically opens the report in your default browser
- Auto-installs Allure CLI if not already installed

### Option 2: Run Tests Only (No Report)
Run all tests without generating the detailed Allure report.

```bash
.\mvnw.cmd clean test
```

### Option 3: Run Specific Test Class
Run tests from a specific test class.

```bash
.\mvnw.cmd clean test -Dtest=TestClassName
```

### Option 4: Run Tests Using TestNG Configuration
Run tests based on testng.xml configuration file.

```bash
.\mvnw.cmd clean test -DsuiteXmlFile=testng.xml
```

### Option 5: Maven Clean Build
Clean all previously built files and dependencies.

```bash
.\mvnw.cmd clean
```

### Option 6: Skip Tests During Build
Build the project without running tests.

```bash
.\mvnw.cmd clean install -DskipTests
```

### Option 7: Verbose Test Output
Run tests with detailed console output for debugging.

```bash
.\mvnw.cmd clean test -X
```

## View Test Results

After running tests, reports are generated in:
- **Allure Report:** `target/allure-results/` (served automatically by run-tests-with-report.cmd)
- **Surefire Reports:** `target/surefire-reports/` (HTML and XML formats)

To manually open the Allure report:

```bash
.\open-report.cmd
```

## Project Structure

```
PartA/
├── src/
│   ├── main/java/          # Source code
│   ├── main/resources/     # Configuration files
│   ├── test/java/          # Test classes
│   └── test/resources/     # Test data and configurations
├── target/                 # Build output and reports
├── pom.xml                 # Maven configuration
├── testng.xml             # TestNG configuration
├── mvnw.cmd               # Maven wrapper for Windows
├── run-tests-with-report.cmd  # Script to run tests with Allure
└── Readme.md              # This file
```

## Technologies

- **Selenium WebDriver:** 4.18.1
- **TestNG:** 7.9.0
- **Allure Reports:** 2.25.0
- **WebDriverManager:** 5.7.0
- **Apache POI:** 5.2.5 (Excel data handling)
- **Java:** 17

## Troubleshooting

### Tests fail to run
- Ensure Java 17+ is installed: `java -version`
- Verify Maven is working: `.\mvnw.cmd -v`
- Check that Chrome browser is installed

### Allure report not opening
- Run script with admin privileges if permission issues occur
- Manually open report: `.\open-report.cmd`

### Maven not found
- Use `.\mvnw.cmd` instead of `mvn` - it's the bundled wrapper
- Or install Maven globally and ensure it's in PATH

## Contact & Support

For issues or questions, refer to the project documentation or contact the course instructor.