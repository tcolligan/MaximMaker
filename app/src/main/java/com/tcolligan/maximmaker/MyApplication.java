package com.tcolligan.maximmaker;

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
    //region Lif-cycle Methods
    @Override
    public void onCreate()
    {
        super.onCreate();

        // TODO: Load crash logger and analytics
    }
    //endregion
}
