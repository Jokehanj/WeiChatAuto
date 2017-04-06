package com.hj.weichatauto.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.hj.weichatauto.accessibility.AutoWeiChatFriendService;

import java.util.List;

/**
 * 项目名称：WeiChatAuto
 * 类描述：
 * 创建人：HJ
 * 创建时间：2016/11/5 17:20
 */
public class Utils {

    public static String ISTRUE = "isTrue";
    public static String FILESTR = "filestr";
    public static String ADDSTR = "addstr";
    public static String PHONEPROCESS="phoneprocess";
    public static String PROCESSSTR="你已选择新的文件";

    /**
     * 判断辅助服务是否开启
     *
     * @param mContext
     * @return true开启  false 未开启
     */
    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        // AutoWeiChatFriendService为对应的服务
        final String service = mContext.getPackageName() + "/" + AutoWeiChatFriendService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取微信的版本号
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo : packageInfoList) {
            if ("com.tencent.mm".equals(packageInfo.packageName)) {
                return packageInfo.versionName;
            }
        }
        return "6.3.25";
    }

    /**
     * 保存shared数据
     *
     * @param context
     * @param key
     * @param data
     */
    public static void setBoolData(Context context, String key, boolean data) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    /**
     * 获取shared数据
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 保存shared数据
     *
     * @param context
     * @param key
     * @param data
     */
    public static void setStrData(Context context, String key, String data) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.commit();
    }

    /**
     * 获取shared数据
     *
     * @param context
     * @param key
     * @return
     */
    public static String getStrData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

}
