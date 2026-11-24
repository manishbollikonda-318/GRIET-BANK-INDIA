@echo off
echo ==========================================
echo  Compiling Banking Management System...
echo ==========================================

if not exist bin mkdir bin
javac -d bin src\bank\exception\*.java src\bank\model\*.java src\bank\storage\*.java src\bank\gui\*.java src\bank\*.java src\*.java

echo ==========================================
echo  Launching Banking Management System...
echo  Default Admin Credentials: admin / admin
echo ==========================================

java -cp bin Application
pause
