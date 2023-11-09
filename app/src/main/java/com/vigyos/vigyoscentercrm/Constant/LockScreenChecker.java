package com.vigyos.vigyoscentercrm.Constant;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class LockScreenChecker {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isLockScreenEnabled(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        // Check if the device has a secure lock screen (PIN, pattern, or password)
        boolean isSecureLockScreen = keyguardManager.isKeyguardSecure();

        // Check if the device has a fingerprint lock
        boolean isFingerprintLockEnabled = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isFingerprintLockEnabled = keyguardManager.isDeviceSecure();
        }

        return isSecureLockScreen || isFingerprintLockEnabled;
    }
}