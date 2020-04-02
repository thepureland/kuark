@echo start H2 server with web console ...
@echo %cd% | findstr "kuark-data" >nul && set prefix=%cd% || set prefix="%prefix%/kuark-data/kuark-data-jdbc/h2"
@java -cp "%prefix%/h2-1.4.200.jar;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Console -tcpPort 9092 -webPort 8082 %*
@if errorlevel 1 pause