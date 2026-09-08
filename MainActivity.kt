package com.lia.dialer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0B0B0B) // خلفية AMOLED داكنة بالكامل
                ) {
                    LiaDialerScreen()
                }
            }
        }
    }
}

// دالة مساعدة لتشغيل الاهتزاز (Haptic Feedback)
fun triggerVibration(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // اهتزاز قصير وقوي نسبياً (Click effect)
        vibrator.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(35) // لنسخ الأندرويد القديمة
    }
}


@Composable
fun LiaDialerScreen() {
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // الجزء العلوي: اللوجو وشاشة عرض الرقم المدخل مع زر الحذف
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            LiaLogoHeader()
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // شاشة عرض الرقم + زر المسح (Backspace)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = phoneNumber.ifEmpty { "أدخل الرقم..." },
                    color = if (phoneNumber.isEmpty()) Color.DarkGray else Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                // زر مسح الرقم (يظهر فقط لو فيه أرقام)
                if (phoneNumber.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E1E1E))
                            .clickable {
                                if (phoneNumber.isNotEmpty()) {
                                    triggerVibration(context) // اهتزاز عند المسح
                                    phoneNumber = phoneNumber.dropLast(1)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⌫",
                            color = Color(0xFFFF5252),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // لوحة المفاتيح (Keypad) مع زر الاتصال في الأسفل
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            val buttons = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("*", "0", "#")
            )

            buttons.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { digit ->
                        DialButton(
                            digit = digit,
                            modifier = Modifier.weight(1f)
                        ) {
                            triggerVibration(context) // تفعيل الاهتزاز عند الضغط
                            phoneNumber += digit
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // زر الاتصال الفخم (Call Button) باللون الأخضر النيون
            Box(
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00E676)) // أخضر نيون ساطع
                    .clickable {
                        if (phoneNumber.isNotEmpty()) {
                            triggerVibration(context) // اهتزاز عند الضغط على الاتصال
                            val intent = Intent(Intent.ACTION_CALL).apply {
                                data = Uri.parse("tel:$phoneNumber")
                            }
                            // التحقق من الصلاحية قبل تنفيذ الاتصال
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CALL_PHONE
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                context.startActivity(intent)
                            } else {
                                // طلب الصلاحية لو لم تكن متوفرة
                                ActivityCompat.requestPermissions(
                                    context as ComponentActivity,
                                    arrayOf(Manifest.permission.CALL_PHONE),
                                    1
                                )
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📞",
                    fontSize = 32.sp
                )
            }
        }
    }
}

@Composable
fun LiaLogoHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF1E1E2C), Color(0xFF0F2027))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "L",
                color = Color(0xFF00E5FF), // أزرق نيون ساطع
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DialButton(digit: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .aspectRatio(1.2f)
            .clip(CircleShape)
            .background(Color(0xFF161616)) // لون زر داكن بريميوم
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}