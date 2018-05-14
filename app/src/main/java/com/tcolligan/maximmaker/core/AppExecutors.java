package com.tcolligan.maximmaker.core;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global executor pools for the whole application.
 * <p>
 * Created on 5/14/18.
 *
 * @author Thomas Colligan
 */

public class AppExecutors
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private final Executor diskIo;
    private final Executor mainThread;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    private AppExecutors(Executor diskIO, Executor mainThread)
    {
        this.diskIo = diskIO;
        this.mainThread = mainThread;
    }

    public AppExecutors()
    {
        this(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public Executor diskIO()
    {
        return diskIo;
    }

    public Executor mainThread()
    {
        return mainThread;
    }

    //==============================================================================================
    // MainThreadExecutor Class
    //==============================================================================================

    private static class MainThreadExecutor implements Executor
    {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command)
        {
            mainThreadHandler.post(command);
        }
    }
}
