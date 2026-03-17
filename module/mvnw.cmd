@REM ----------------------------------------------------------------------------
@REM Maven Wrapper for Windows
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
set WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties

if not exist "%WRAPPER_JAR%" (
  echo [mvnw] Missing "%WRAPPER_JAR%".
  echo [mvnw] Please download maven-wrapper.jar first.
  exit /b 1
)

if "%JAVA_HOME%"=="" (
  echo [mvnw] JAVA_HOME is not set. Please install JDK and set JAVA_HOME.
  exit /b 1
)

"%JAVA_HOME%\bin\java.exe" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*

