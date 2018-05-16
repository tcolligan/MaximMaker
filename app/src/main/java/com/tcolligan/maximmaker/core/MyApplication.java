package com.tcolligan.maximmaker.core;

import android.annotation.SuppressLint;
import android.app.Application;

import com.tcolligan.maximmaker.BuildConfig;

import timber.log.Timber;

/**
 * Overridden Application Class, used for initial app loading.
 * <p/>
 * Created on 7/13/2016.
 *
 * @author Thomas Colligan
 */
public class MyApplication extends Application
{
    @SuppressLint("StaticFieldLeak")
    private static Application instance;

    //==============================================================================================
    // Life-cycle Methods
    //==============================================================================================

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;

        if (BuildConfig.DEBUG)
        {
            Timber.plant(new Timber.DebugTree());
        }
        else
        {
            Timber.plant(new ReleaseTree());
        }
    }

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static Application getApplication()
    {
        return instance;
    }

}
