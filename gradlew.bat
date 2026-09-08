@echo off
echo [LIA Build] جارٍ فحص بيئة البناء...
where gradle >nul 2>&1
if %errorlevel% neq 0 (
    echo [تنبيه] أداة Gradle غير مسجلة في مسارات النظام.
    echo يمكنك تثبيت Gradle أو استخدام طريقة البناء السريع المباشر.
    exit /b 1
) else (
    gradle assembleDebug
)
