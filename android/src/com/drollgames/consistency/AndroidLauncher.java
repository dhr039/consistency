package com.drollgames.consistency;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.drollgames.consistency.util.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {

    private static final String TAG = "AndroidLauncher";

    private CJMain cjMain;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        /* conserve battery */
        config.useAccelerometer = false;
        config.useCompass = false;
        /* keep the screen on */
        config.useWakelock = true;

        if (Constants.ENABLE_ADS) {

            cjMain = new CJMain(AndroidLauncher.this);
            initialize(cjMain, config);

            interstitialAd = new InterstitialAd(this);
            interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_interstitial_1));
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    Gdx.app.log(TAG, "ADMOB: onAdClosed");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
                    requestNewAdmobInterstitial();
//                        }
//                    });
                }
            });
            requestNewAdmobInterstitial();

        } else {
            cjMain = new CJMain(AndroidLauncher.this);
            initialize(cjMain, config);
        }
    }

    private void displayAdMobInterstitial() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    Gdx.app.error(TAG, "interstitialAd is not loaded");
                    requestNewAdmobInterstitial();
                }
            }
        });
    }

    private void requestNewAdmobInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).
                addTestDevice(getString(R.string.test_device_1)).
                addTestDevice(getString(R.string.test_device_2)).
                addTestDevice(getString(R.string.test_device_3)).
                addTestDevice(getString(R.string.test_device_4)).
                addTestDevice(getString(R.string.test_device_5)).
                addTestDevice(getString(R.string.test_device_6)).
                addTestDevice(getString(R.string.test_device_7)).
                addTestDevice(getString(R.string.test_device_8)).
                addTestDevice(getString(R.string.test_device_9)).
                addTestDevice(getString(R.string.test_device_10)).
                addTestDevice(getString(R.string.test_device_11)).
                addTestDevice(getString(R.string.test_device_12)).
                addKeyword("game").addKeyword("puzzle").addKeyword("numbers")
                .build();

        interstitialAd.loadAd(adRequest);
    }

    @Override
    public void showIntersitial() {
        Gdx.app.log(TAG, "++ showIntersitial ++");
        if (Constants.ENABLE_ADS) {
            displayAdMobInterstitial();
        } else {
            Log.v(TAG, "ads are not enabled");
        }
    }

    @Override
    public void loadIntersitial() {
        /* TODO: implement */
    }

}
