package com.akbarf.generateimagewithtext;

import android.content.Context;
import android.content.SharedPreferences;

public class Constants {

    public final static String dummyImageUrl = "https://s3-alpha-sig.figma.com/img/1120/41ad/8c0b7af36a0db247d218e05b02c3f78b?Expires=1688342400&Signature=WrbK5w~AO~~6q7ZWAcN7GybkHyRUDMBumWl2Pdw6qfq~0RN1BvSpJfwCjIm9cpZZ~8P7F9oS-1sgn-EmjCxQpCx0ycJb04Z5cm1AhdGSUdrPRfg41F7GE3Idgw6FX2gr3uyp9qQZAJbT7frFmb31PY~~E8rjWuIC4F2SyolVGFRFUCZMjaNDlIVmtSLOwTs94k-y--PDP8HBpHjqCGdZV2F07ocldYG31Dx0gMQLPDVhtmFBsY~EVndfNKqC~9ffKReRy4yLjACn3Vup1BrPaBOe8VDYsgGO3~M~rSJvU14~VT8qMYnR~RmB5yybZLGZW3HWFE1Deo2YyIKLm1Xicg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4";
//    public final static String baseUrlOpenAI = "https://api.openai.com/v1/images/generations";
//    public final static String bearerOpenAI = "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";
//    public final static String organizationOpenAI = "org-KrFHZrcz8i6acfkeqoW9gmCQ";


    public final static String baseUrlPexcels = "https://api.pexels.com/v1/";
    public final static String apiKeyPexcel = "hnui4KGOtinPMZcWdkVigoGWqY26mmYVtPArxBzp4LsMHtdNn8UlEkzn";
    public final static String BASE_URL = "https://express-history-generate-images.vercel.app/";
//    public final static String BASE_URL = "http://192.168.8.131:3000/";

    public static final String PREFERENCE_FILE_KEY = "6mmYVtPArxBzp4LsMHtdNn8UlEkzn";
    private static final String KEY_LOGGED_IN = "loggedIn";
    private static final String KEY_UID = "userId";

    public static void clearShadPref(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(KEY_LOGGED_IN);
        editor.remove(KEY_UID);
        editor.clear();
        editor.apply();
    }

    public static void setIsLoggedIn(Context context, Boolean isLoggedIn) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public static Boolean isLoggedIn(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_LOGGED_IN, false);
    }


    public static void setUID(Context context, String UID) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_UID, UID);
        editor.apply();
    }

    public static String getUID(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return sp.getString(KEY_UID, null);
    }
}
