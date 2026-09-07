# Kotlin Coroutines
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Ktor Serialization & Client
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}
-keepnames @kotlinx.serialization.Serializable class *
-keepclassmembers class * {
    @serialization.SerialName <fields>;
}

# AndroidX Lifecycle & Compose
-keep class androidx.lifecycle.** { *; }
-keep class androidx.compose.** { *; }

# Serialization JSON library
-dontwarn kotlinx.serialization.**
-keep,allowshrinking class kotlinx.serialization.json.** { *; }

# OkHttp (used under the hood by Ktor)
-dontwarn okhttp3.**
-dontwarn okio.**
-keepattributes Signature,InnerClasses,EnclosingMethod
