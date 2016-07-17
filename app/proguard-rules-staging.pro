# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Tom\AppData\Local\Android\sdk1/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Do not obfuscate the code for staging type releases.
# We still want to be able to debug staging release, obfuscation makes that difficult.
-dontobfuscate

# Proguard was eating this class since it was only referenced via XML.
# But we actually need it for the search in this app.
-keep class android.support.v7.widget.SearchView { *; }