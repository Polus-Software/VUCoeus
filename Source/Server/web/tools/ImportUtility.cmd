@echo off
rem Guess COEUS_HOME if not defined
set CURRENT_DIR=%cd%
if not "%COEUS_HOME%" == "" goto gotHome
set COEUS_HOME=%CURRENT_DIR%
if exist "%COEUS_HOME%\WEB-INF\classes\edu\mit\coeus\tools\CoeusKeyPairImportUtility.class" goto okHome
cd ..
set COEUS_HOME=%cd%
cd %CURRENT_DIR%
:gotHome
if exist "%COEUS_HOME%\WEB-INF\classes\edu\mit\coeus\tools\CoeusKeyPairImportUtility.class" goto okHome
echo The COEUS_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okHome
java -classpath %COEUS_HOME%/WEB-INF/classes edu.mit.coeus.tools.CoeusKeyPairImportUtility %1 %2 %3 %4 %5 %6 %7