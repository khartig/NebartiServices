@echo off

if %JRE_HOME% == "" goto setjrehome
goto setup

:setjrehome
set JRE_HOME=C:\Program Files\Java\jdk1.6.0_27\jre

:setup
set CLASSPATH=..\libs\dataIngest\*
set CLASSPATH=%CLASSPATH%;..\dataIngest.jar
      
set EXEC=com.idot.dataingest.schedulers.dbcleaner.DBCleanerScheduler
set CMD=%JRE_HOME%\bin\java -cp %CLASSPATH% %EXEC%

echo %CMD%
%CMD%