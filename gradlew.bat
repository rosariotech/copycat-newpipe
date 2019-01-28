@if "%DEBUG" == "" @echo off

if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if %DIRNAME% == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

set DEFAULT_JVM_OPTS=

if defined JAVA_HOME got findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

