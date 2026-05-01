@echo off
set "MVN_HOME=%USERPROFILE%\maven\apache-maven-3.9.9"
set "PATH=%MVN_HOME%\bin;%PATH%"
pushd "%~dp0"
call mvn %*
popd
