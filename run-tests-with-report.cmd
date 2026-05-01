@echo off
setlocal

set "MVN_HOME=%USERPROFILE%\maven\apache-maven-3.9.9"
set "PATH=%MVN_HOME%\bin;%PATH%"

echo ============================================
echo  Running tests...
echo ============================================
call mvn clean test -Dsurefire.failIfNoSpecifiedTests=false

echo.
echo ============================================
echo  Generating and serving Allure report...
echo ============================================
call mvn allure:serve

endlocal
