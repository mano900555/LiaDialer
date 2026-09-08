const fs = require('fs');
const path = require('path');

const baseDir = __dirname;

// إنشاء ملف تشغيل مباشر يبحث عن Gradle المحمول أو يوجه للحل البديل
const gradlewContent = `@echo off
echo [LIA Build] جارٍ فحص بيئة البناء...
where gradle >nul 2>&1
if %errorlevel% neq 0 (
    echo [تنبيه] أداة Gradle غير مسجلة في مسارات النظام.
    echo يمكنك تثبيت Gradle أو استخدام طريقة البناء السريع المباشر.
    exit /b 1
) else (
    gradle assembleDebug
)
`;

fs.writeFileSync(path.join(baseDir, 'gradlew.bat'), gradlewContent);
console.log('تم تحديث ملف gradlew.bat بنجاح!');