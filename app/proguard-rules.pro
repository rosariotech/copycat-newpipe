-dontobfuscate
-keep class org.mozilla.javascript.** { *;}

-keep class org.mozilla.classfile.ClassFileWriter
-keep class com.google.android.exoplayer2.** {*;}

-dontwarn org.mozilla.javascript.tools.**
-dontwarn android.arch.util.paging.CountedDataSource
-dontwarn android.arch.persistence.room.paging.LimitOffsetDataSource

-dontwarn icepick.**
-keep class icepick.** {*;}
-keep class **$$Icepick {*;}
-keepclasseswithmembernames class *{
    @icepick.* <fields>;
}
-keepnames class * { @icepick.State *;}