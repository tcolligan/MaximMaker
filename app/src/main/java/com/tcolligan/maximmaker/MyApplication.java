package com.tcolligan.maximmaker;

import android.app.Application;

/**
 * Overridden Application Class, used for initial app loading.
 *
 * Created on 7/13/2016.
 *
 * @author Thomas Colligan
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // TODO: Load crash logger and analytics
    }
}
