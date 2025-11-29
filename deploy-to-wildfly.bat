@echo off
echo ========================================
echo WildFly Deployment Script
echo ========================================

REM Configuration
set WILDFLY_DEPLOYMENTS=C:\wildfly-37.0.0.Final\standalone\deployments
set WAR_FILE=target\CompC-1.0-SNAPSHOT.war
set WILDFLY_BIN=C:\wildfly-37.0.0.Final\bin

echo Step 1: Building the project...
echo.

REM Build the project
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Checking if WAR file exists...
echo.

REM Check if WAR file exists
if not exist "%WAR_FILE%" (
    echo ERROR: WAR file not found at %WAR_FILE%
    pause
    exit /b 1
)

echo WAR file found: %WAR_FILE%
echo.

echo Step 3: Deploying to WildFly...
echo.

REM Remove existing deployment and all marker files
echo Cleaning up existing deployment files...
if exist "%WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war" (
    echo Removing existing WAR file...
    del "%WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war"
)

if exist "%WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war.deployed" (
    echo Removing deployed marker...
    del "%WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war.deployed"
)

if exist "%WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war.failed" (
    echo Removing failed marker...
    del "%WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war.failed"
)

if exist "%WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war.isdeploying" (
    echo Removing isdeploying marker...
    del "%WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war.isdeploying"
)

if exist "%WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war.deploying" (
    echo Removing deploying marker...
    del "%WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war.deploying"
)

echo Copying WAR file to WildFly deployments...
copy "%WAR_FILE%" "%WILDFLY_DEPLOYMENTS%\"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Failed to copy WAR file to WildFly deployments!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Deployment completed successfully!
echo ========================================
echo.
echo The application has been deployed to:
echo %WILDFLY_DEPLOYMENTS%\CompC-1.0-SNAPSHOT.war
echo.
echo WildFly will automatically deploy the application.
echo You can check the deployment status in the WildFly console.
echo.
echo To restart WildFly manually, you can run:
echo "%WILDFLY_BIN%\standalone.bat"
echo.
pause
