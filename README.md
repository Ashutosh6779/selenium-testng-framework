# Selenium TestNG Automation Framework

A production-grade UI test automation framework built with **Selenium 4**, **TestNG**, **Page Object Model (POM)**, and **Allure Reports** — with a fully configured **GitHub Actions CI/CD pipeline**.

## Framework Architecture

```
src/test/java/com/automationframework/
├── config/
│   └── ConfigReader.java        # Centralised config loading (config.properties)
├── pages/
│   ├── BasePage.java            # POM base class — all common Selenium actions
│   ├── LoginPage.java           # Login page locators + actions
│   └── ProductsPage.java        # Products page locators + actions
├── tests/
│   ├── BaseTest.java            # TestNG @Before/@After, screenshot on failure
│   └── LoginTest.java           # Test cases with Allure annotations
└── utils/
    ├── DriverManager.java       # Thread-safe WebDriver (ThreadLocal for parallel)
    ├── WaitUtils.java           # Explicit wait helpers (no Thread.sleep)
    ├── ExcelUtils.java          # Data-driven testing via Apache POI
    └── ScreenshotUtils.java     # Auto-screenshot on failure → Allure report
```

## Key Design Decisions

| Pattern | Implementation | Why |
|---|---|---|
| Page Object Model | `BasePage` → `LoginPage`, `ProductsPage` | Single place to update locators when UI changes |
| ThreadLocal WebDriver | `DriverManager` | Each parallel thread gets its own browser instance |
| Explicit Waits | `WaitUtils` wrapping `WebDriverWait` | No `Thread.sleep()` — tests are fast and reliable |
| Config externalisation | `ConfigReader` + `config.properties` | Browser/URL set once; overrideable in CI via `-D` flags |
| Allure integration | `@Epic`, `@Feature`, `@Story`, `@Severity` | Rich HTML reports with screenshots embedded on failure |

## Tech Stack

- **Language:** Java 11
- **Build:** Maven
- **Test Runner:** TestNG 7.9 (parallel execution via `testng.xml`)
- **UI Automation:** Selenium 4.18
- **Driver Management:** WebDriverManager (auto-downloads browser drivers)
- **Reporting:** Allure Reports + ExtentReports
- **Data-Driven:** Apache POI (Excel)
- **CI/CD:** GitHub Actions

## Running the Tests

### Prerequisites
- Java 11+
- Maven 3.8+
- Chrome browser installed

### Run all tests
```bash
mvn clean test
```

### Run with a different browser
```bash
mvn clean test -Dbrowser=firefox
```

### Run in headless mode (CI)
```bash
mvn clean test -Dheadless=true
```

### Generate Allure report
```bash
mvn allure:report
# Report opens at: target/site/allure-maven-plugin/index.html
```

## CI/CD Pipeline

GitHub Actions runs the full suite on every push and pull request to `main`:
- Headless Chrome on Ubuntu
- Allure report uploaded as artifact (downloadable from Actions tab)
- Test results published inline in PR checks
- Failure screenshots captured and uploaded

See `.github/workflows/selenium-tests.yml` for the full pipeline config.

## Application Under Test

[Sauce Demo](https://www.saucedemo.com) — a publicly available e-commerce demo app designed for automation practice.

## Test Coverage

| Module | Test Scenarios |
|---|---|
| Login | Successful login, invalid password, empty fields, locked-out user |
| Products | Page load, item count, add-to-cart, cart badge update |
| Data-Driven | Multiple credential combinations via `@DataProvider` |
