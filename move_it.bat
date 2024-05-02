@echo off
:: Компиляция и сборка 
:: mvn package
:: Запуск Tomcat
:: sc start Tomcat10
del "C:\Apache Tomcat\webapps\EMU.war"
copy "target\EMU.war" "C:\Apache Tomcat\webapps"