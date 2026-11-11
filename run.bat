@echo off
title Enterprise Event Hub - Xavier Cedeno
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.13.9-hotspot
set Path=%JAVA_HOME%\bin;%Path%
echo ========================================================
echo Iniciando Enterprise Event Hub (Spring Boot + Java 21)
echo Dashboard web disponible en: http://localhost:8080/
echo ========================================================
.\mvnw.cmd spring-boot:run
pause

