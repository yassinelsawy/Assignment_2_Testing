@echo off
setlocal

pushd "%~dp0"

set "MVN_HOME=%USERPROFILE%\maven\apache-maven-3.9.9"
set "ALLURE_HOME=%USERPROFILE%\allure\allure-2.25.0"
set "PATH=%MVN_HOME%\bin;%ALLURE_HOME%\bin;%PATH%"

REM Download Allure CLI if not present
if not exist "%ALLURE_HOME%\bin\allure.bat" (
    echo Downloading Allure CLI 2.25.0...
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/allure-framework/allure2/releases/download/2.25.0/allure-2.25.0.zip' -OutFile '%USERPROFILE%\allure.zip' -UseBasicParsing"
    if errorlevel 1 (
        echo ERROR: Failed to download Allure. Check your internet connection.
        exit /b 1
    )
    echo Extracting Allure...
    powershell -Command "Expand-Archive -Path '%USERPROFILE%\allure.zip' -DestinationPath '%USERPROFILE%\allure' -Force"
    del "%USERPROFILE%\allure.zip"
    echo Allure installed to %ALLURE_HOME%
)

echo ============================================
echo  Running tests...
echo ============================================
call mvn clean test -Dsurefire.failIfNoSpecifiedTests=false

echo.
echo ============================================
echo  Generating Allure report...
echo ============================================
if exist "target\allure-results" (
    call allure serve target\allure-results
) else (
    echo No allure-results found. Tests may have failed to start.
    exit /b 1
)

popd
endlocal
