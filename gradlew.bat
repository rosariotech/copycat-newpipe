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

echo.
echo ERRO: JAVA_HOME não foi setado e nenhum comando java foi encontrado.
echo.
echo Por favor sete o JAVA_HOME

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERRO: JAVA_HOME foi setado para um diretório inválido: %JAVA_HOME%
echo.
echo Por favor sete o JAVA_HOME

goto fail

:init

if not "%OS" == "Windows_NT" goto win9xME_args

:win9xME_args

