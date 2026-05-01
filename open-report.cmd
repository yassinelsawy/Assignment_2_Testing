@echo off
setlocal

set "ALLURE_HOME=%USERPROFILE%\allure\allure-2.25.0"
set "PATH=%ALLURE_HOME%\bin;%PATH%"

if not exist "%ALLURE_HOME%\bin\allure.bat" (
    echo Allure CLI not found. Run run-tests-with-report.cmd first to install it.
    exit /b 1
)

if not exist "target\allure-results" (
    echo No allure-results found. Run tests first.
    exit /b 1
)

echo Opening Allure report...
call allure serve target\allure-results

endlocal
