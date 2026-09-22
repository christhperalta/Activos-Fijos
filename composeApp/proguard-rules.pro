# Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }
-keep,includedescriptorclasses class com.christhperalta.activosfijos.**$$serializer { *; }
-keepclassmembers class com.christhperalta.activosfijos.** { *** Companion; }
-keepclasseswithmembers class com.christhperalta.activosfijos.** { kotlinx.serialization.KSerializer serializer(...); }

# Koin
-keep class org.koin.** { *; }

# SQLDelight
-keep class app.cash.sqldelight.** { *; }
-keep class com.christhperalta.activosfijos.database.** { *; }

# QRKit
-keep class qrscanner.** { *; }
-keep class qrgenerator.** { *; }

# OkHttp (used by Ktor on Android)
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

# Compose
-dontwarn androidx.compose.**
