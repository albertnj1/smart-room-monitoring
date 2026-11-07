@echo off
title Smart Room Monitoring System
color 0A

:: Tambahkan JDK ke PATH sementara jika belum terdaftar
where java >nul 2>&1
if %errorlevel% neq 0 (
    set "PATH=C:\Users\user\.jdks\openjdk-26.0.2.1\bin;%PATH%"
)

echo ===================================================
echo   Mengompilasi Smart Room Monitoring System...
echo ===================================================
javac *.java

if %errorlevel% neq 0 (
    echo [ERROR] Kompilasi gagal! Periksa kode Java Anda.
    pause
    exit /b %errorlevel%
)

echo Kompilasi berhasil! Memulai program...
echo.
java Main
echo.
pause

