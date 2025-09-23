## LibraryApp-API-Automation (Rest Assured · Cucumber · JUnit · Maven · Jenkins)

- This project was built as part of my portfolio to demonstrate API testing skills with Java, Rest Assured, Cucumber BDD, and CI-ready reporting.

### This repo demonstrates:
- **API testing with Rest Assured** (request specs, auth, serialization/deserialization)
- **Behavior-Driven tests with Cucumber** (feature files and step definitions)
- **Assertions with JUnit 4** (status codes, payload fields, business rules)
- **Portable reporting** (Cucumber HTML/Pretty and Surefire XML) suitable for CI
- **Clean utilities** for configuration, auth tokens, and request building

### Tech stack
- **Java 17, Maven**
- **Rest Assured 5.2.1**
- **Cucumber JVM 7.3.0**, **JUnit 4**
- **JSON** parsing with Rest Assured (JsonPath)
- **Selenium/WebDriverManager (optional)** for UI-API integration scenarios

### Repository structure (high-level)
- `src/test/resources/features/` — Gherkin feature files (business-readable specs)
- `src/test/java/com/library/steps/` — Step definitions (glue)
- `src/test/java/com/library/runner/` — Cucumber runners (`CukesRunner`, `FailedTestRunner`)
- `src/test/java/com/library/utility/` — API utilities, configuration reader, driver (optional)
- `src/test/java/com/library/pages/` — Page Object Model (only for optional UI checks)
- `src/test/resources/sql/` — SQL files for cross-checking data (optional)
- `config.properties` — environment defaults (base URL, roles, optional browser)
- `pom.xml` — dependency and build lifecycle config (Surefire includes, reporting)

### Design choices
- `LibraryAPI_Util` centralizes base URL, headers, and token helpers for librarian/student flows.
- `ConfigurationReader` manages properties and allows runtime overrides via `-D` system properties.
- `Hooks` with `io.cucumber.java.After` ensure clean teardown per Scenario (browsers closed if UI steps are used).
- `Driver` uses `InheritableThreadLocal` for safety when multiple tests run on CI; default runs are sequential.

### Key capabilities
- Robust API assertions (status, headers, payload content, lists/maps)
- Token-based auth flows and negative tests (401/403, validation errors)
- CI-ready XML outputs under `target/surefire-reports` for Jenkins JUnit plugin
- Optional UI-API/DB cross-checks through shared utilities

### Reporting

Available outputs
- **Cucumber HTML Report** → `target/cucumber-report.html`
- **Pretty Reports (plugin)** → `target/cucumber/`
- **Surefire (JUnit XML)** → `target/surefire-reports/TEST-*.xml`
- **Cucumber Report PDF** → [Cucumber_Report.pdf](test_reports/Cucumber_Report_API.pdf)

### Running locally

Run full suite with defaults (`config.properties`):
```bash
mvn clean test
```

Override environment at runtime:
```bash
mvn clean test -DbaseUrl=https://staging.libraryapp.io -Dusername=librarian -Dpassword=secret
```

If any UI-backed steps are executed, prefer headless in CI:
```bash
mvn clean test -Dbrowser=headless-chrome
```

### Running with Maven (locally or CI)
- Smoke/tagged run example: `mvn clean test -Dcucumber.filter.tags=@smoke`
- Default runner is configured via Surefire includes → `**/CukesRunner*.java`
- Build fails on test failures (`testFailureIgnore=false` in Surefire)

### CI/CD (Jenkins)
- Works on Jenkins using standard Maven steps (batch mode recommended):
```bash
mvn -B clean test -Dbrowser=headless-chrome
```
- Pass secrets as Jenkins credentials/environment variables and map to `-D` properties.
- Publish `target/surefire-reports/TEST-*.xml` as JUnit test results and attach Cucumber HTML under `target/` as build artifacts.

### Test strategy & tagging
- Business-readable scenarios live under `features/` (focused, deterministic)
- Tag policy suggestions:
  - `@smoke` — critical happy paths
  - `@wip` — work-in-progress; excluded from CI by default
  - `@allLayers` — end-to-end scenarios requiring cross-checks with UI and database data 

### Configuration and secrets
- `config.properties` contains non-sensitive defaults (e.g., base URL, optional browser)
- Real credentials are not committed; they should be injected via CI parameters/secrets
- For security, credentials in this portfolio will be sanitized or replaced with dummy values before sharing

### How to read the code
- Start from runner `com.library.runner.CukesRunner`
- Explore API helpers in `com.library.utility.LibraryAPI_Util`
- See step definitions in `com.library.steps.*`
- Check `pom.xml` for Surefire include and reporting configuration

This repo is designed to reflect real-world SDET API practices: readable specifications, maintainable structure, CI-friendly reporting, and secure configuration.


