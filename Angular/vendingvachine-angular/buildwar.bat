cd .\dist\vendingvachine-angular
jar cvf vendingvachine.war .
del /Q ..\vendingvachine.war
move vendingvachine.war ..
