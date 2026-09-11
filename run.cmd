@echo off
setlocal
cd /d "%~dp0"
if exist "C:\Program Files\Java\jdk-26.0.2.1\bin\java.exe" set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.2.1"
if not defined JAVA_HOME (
  echo Please set JAVA_HOME to JDK 26.
  exit /b 1
)
set "PATH=%JAVA_HOME%\bin;%PATH%"
call "%~dp0mvnw.cmd" -DskipTests package
if errorlevel 1 exit /b 1
"%JAVA_HOME%\bin\java.exe" -jar "%~dp0target\springboot1-4-1.0.war"
endlocal
