package com.tcolligan.maximmaker.core;

import android.app.Application;

/**
 * Overridden Application Class, used for initial app loading.
 * <p/>
 * Created on 7/13/2016.
 *
 * @author Thomas Colligan
 */
public class MyApplication extends Application
{
    private static Application instance;

    //==============================================================================================
    // Life-cycle Methods
    //==============================================================================================

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static Application getApplication()
    {
        return instance;
    }

}
