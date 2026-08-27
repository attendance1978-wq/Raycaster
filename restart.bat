@echo off
echo ========================================
echo   Raycaster Engine - Restart
echo ========================================
echo.

echo Cleaning old files...
if exist out (
    del /q out\*.class 2>nul
    echo Deleted old .class files
)

echo.
echo Compiling...
javac -d out *.java

if %errorlevel% == 0 (
    echo.
    echo ========================================
    echo   COMPILATION SUCCESSFUL!
    echo ========================================
    echo.
    echo Running Raycaster Engine...
    echo.
    java -cp out RaycasterEngine
) else (
    echo.
    echo ========================================
    echo   COMPILATION FAILED!
    echo ========================================
    echo Please check the errors above.
    pause
)