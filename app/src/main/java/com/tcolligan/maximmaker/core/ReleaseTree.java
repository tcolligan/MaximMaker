package com.tcolligan.maximmaker.core;

import timber.log.Timber;

import static android.util.Log.ERROR;
import static android.util.Log.WARN;

/**
 * A timber tree implementation to be used when the app is in release mode.
 * <p>
 * Created on 5/16/18.
 *
 * @author Thomas Colligan
 */

class ReleaseTree extends Timber.Tree
{
    @Override
    protected void log(int priority, String tag, String message, Throwable t)
    {

        if (priority == ERROR || priority == WARN)
        {
            // TODO: Log to crashalytics
        }
    }
}
