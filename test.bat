@echo off
title Verifikasi 10 Requirement PBO
color 0B

:: Tambahkan JDK ke PATH sementara jika belum terdaftar
where java >nul 2>&1
if %errorlevel% neq 0 (
    set "PATH=C:\Users\user\.jdks\openjdk-26.0.2.1\bin;%PATH%"
)

echo ===================================================
echo   Mengompilasi dan Menjalankan Test OOP...
echo ===================================================
javac *.java

if %errorlevel% neq 0 (
    echo [ERROR] Kompilasi gagal!
    pause
    exit /b %errorlevel%
)

echo.
java TestOOP
echo.
pause

