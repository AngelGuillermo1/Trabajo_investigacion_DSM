package com.example.gestion_estudiantes.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.Font
import com.example.gestion_estudiantes.R



private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00BCD4), // Teal 200
    onPrimary = Color.Black,
    secondary = Color(0xFFFF9800), // Amber 500
    onSecondary = Color.Black,
    tertiary = Color(0xFF4CAF50), // Green 500
    onTertiary = Color.Black,
    background = Color.Black,
    surface = Color.DarkGray,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3), // Blue 500
    onPrimary = Color.White,
    secondary = Color(0xFFFFC107), // Yellow 700
    onSecondary = Color.Black,
    tertiary = Color(0xFF8BC34A), // Light Green 600
    onTertiary = Color.Black,
    background = Color.White,
    surface = Color.LightGray,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Define tu nueva FontFamily usando la fuente "Cal Sans Regular"
val customFontFamily = FontFamily(
    Font(R.font.calsansregular) // Referencia al archivo calsansregular.ttf
)

// Define tu Typography para Material Design 3 usando la nueva FontFamily
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = customFontFamily,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
    // Puedes personalizar otros estilos aquí según sea necesario
)

// Define tu Shapes para Material Design 3
val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(0.dp)
)

@Composable
fun GestionEstudiantesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}