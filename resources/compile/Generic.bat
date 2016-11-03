@echo off

ECHO ************************************
ECHO *     VERSION PRODUCION %ENTORNO%        *
ECHO ************************************
ECHO *
ECHO DEPLOY_DIR: %DEPLOY_DIR%
ECHO FILE_NAME: %FILE_NAME%
ECHO PROFILE: %PROFILE%
ECHO PROPERTIES: %PROPERTIES%
ECHO ------------------------------------
ECHO *

ECHO Pulsa una tecla para continuar con la version de: %ENTORNO%

rem FUNCIONALIDAD
pause > nul

d:
cd ../../
ECHO mvn clean package -P production -DclientDepencency=%PROFILE% %PROPERTIES%
call mvn clean package -P production -DclientDepencency=%PROFILE% %PROPERTIES%
ECHO Ha terminado el proceso de version para: %ENTORNO%
ECHO Copiado a destino: %DEPLOY_DIR%%FILE_NAME%
cd front\target
copy openppm.war %DEPLOY_DIR%%FILE_NAME%
ECHO **************************************************
ECHO Se ha creado la version en: %DEPLOY_DIR%
explorer %DEPLOY_DIR%
pause > nul