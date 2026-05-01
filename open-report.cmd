@echo off
setlocal

set "MVN_HOME=%USERPROFILE%\maven\apache-maven-3.9.9"
set "PATH=%MVN_HOME%\bin;%PATH%"

if not exist "target\allure-results" (
    echo No allure-results found. Run tests first with run-tests-with-report.cmd
    exit /b 1
)

call mvn allure:serve

endlocal
