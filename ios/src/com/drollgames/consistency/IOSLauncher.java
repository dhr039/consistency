package com.drollgames.consistency;

import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.drollgames.consistency.CJMain;

public class IOSLauncher extends IOSApplication.Delegate {

    private static final String TAG = "DHRlogs ";

    @Override
    protected IOSApplication createApplication() {
//        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
//        return new IOSApplication(new CJMain(null), config);

        Foundation.log(TAG + "createApplication");

        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationLandscape = false;
        config.orientationPortrait = true;
        config.useAccelerometer = false;
        config.useCompass = false;
        return new IOSApplication(new CJMain(null), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}