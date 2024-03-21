# Add project specific ProGuard rules here.

    # By default, the flags in this file are appended to flags specified
    # in D:/androidSDK/adt-bundle-windows-x86_64/sdk/tools/proguard/proguard-android.txt
    # You can edit the include path and order by changing the proguardFiles
    # directive in build.gradle.
    #
    # For more details, see
    # http://developer.android.com/guide/developing/tools/proguard.html

    # Add any project specific keep options here:

    # If your project uses WebView with JS, uncomment the following
    # and specify the fully qualified class name to the JavaScript interface
    # class:
    #-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    # public *;
    #}

    -keep class * extends java.util.ListResourceBundle {
     protected Object[][] getContents();
    }

    -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
     public static final *** NULL;
    }

    -keepnames @com.google.android.gms.common.annotation.KeepName class *
    -keepclassmembernames class * {
     @com.google.android.gms.common.annotation.KeepName *;
    }

    -keepnames class * implements android.os.Parcelable {
     public static final ** CREATOR;
    }
    -dontpreverify
    -repackageclasses ''
    -allowaccessmodification
    -optimizations !code/simplification/arithmetic
    -keepattributes *Annotation*
    -keepattributes InnerClasses
    -keepparameternames

    -keep public class * extends android.content.BroadcastReceiver {public *; protected *;}
    -keep public class * extends co.acoustic.mobile.push.sdk.wi.WakefulIntentService {public *; protected *;}

    -keep public class * extends android.app.IntentService {public *; protected *;}
    -keep public class * extends android.app.Service {public *; protected *;}
    -keep public class co.acoustic.mobile.push.sdk.api.** {public *; protected *;}
    -keep public class co.acoustic.mobile.push.sdk.events.** {public *; protected *;}
    -keep public class co.acoustic.mobile.push.sdk.notification.NotificationsUtility {public *; protected *;}
    -keep public class co.acoustic.mobile.push.sdk.notification.NotificationsUtility$* {public *; protected *;}
    -keep public class co.acoustic.mobile.push.sdk.Preferences {public *; protected *;}
    -keep public class com.amazon.device.messaging.ADM {public *; protected *;}

    -keep public class co.acoustic.mobile.push.sdk.adm.AdmDeliveryChannel {public *; protected *;}
    -keep public class co.acoustic.mobile.push.sdk.gcm.GcmDeliveryChannel {public *; protected *;}
    -keep public class com.google.android.gms.common.GooglePlayServicesUtil {public *; protected *;}

    -keep public class * extends android.content.ContentProvider
    -keep public class * extends co.acoustic.mobile.push.sdk.api.notification.MceNotificationAction

    -keep public class co.acoustic.mobile.push.sdk.api.notification.MceNotificationAction

    -keep class co.acoustic.mobile.push.samples.android.fcm.CustomMessagingService {public *; protected *;}
    -keep class co.acoustic.mobile.push.sdk.plugin.carousel.CarouselNotificationType {public *; protected *;}
    -keep class co.acoustic.mobile.push.sdk.plugin.inapp.InAppMessageProcessor {public *; protected *;}
    -keep class co.acoustic.mobile.push.sdk.plugin.inbox.HtmlRichContent {public *; protected *;}
    -keep class co.acoustic.mobile.push.sdk.plugin.inbox.InboxMessageProcessor {public *; protected *;}
    -keep class co.acoustic.mobile.push.sdk.util.GooglePlayServicesTest {public *; protected *;}
    -keep class co.acoustic.mobile.push.sdk.util.db.DateTypeTemplate {public *; protected *;}

    # added nov 17
    -dontwarn rx.internal.util.unsafe.**
    -keep class co.acoustic.mobile.push.sdk.** { *; }
    -dontwarn co.acoustic.mobile.push.sdk.**
    -keep public class com.google.android.gms.** { *; }
    -dontwarn com.google.android.gms.**
    -dontwarn javax.**
    -keepnames public class * extends io.realm.RealmObject
    -keep class io.realm.** { *; }
    -dontwarn io.realm.**
    -keeppackagenames com.android.providers.contacts.**