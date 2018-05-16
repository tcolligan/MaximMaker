package com.tcolligan.maximmaker.data;

import android.app.Application;

import com.tcolligan.maximmaker.core.AppExecutors;
import com.tcolligan.maximmaker.core.MyApplication;

/**
 * A singleton to keep track of repositories.
 * <p>
 * Created on 5/14/18.
 *
 * @author Thomas Colligan
 */

public class RepositoryManager
{
    private static RepositoryManager INSTANCE;
    private AppExecutors appExecutors;
    private MaximRepository maximRepository;

    private RepositoryManager()
    {
        Application application = MyApplication.getApplication();
        appExecutors = new AppExecutors();
        maximRepository = new MaximRepository(appExecutors, MaximDatabase.getDatabase(application));
    }

    public static RepositoryManager getInstance()
    {
        if (INSTANCE == null)
        {
            synchronized (RepositoryManager.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new RepositoryManager();
                }
            }
        }

        return INSTANCE;
    }

    public MaximRepository getMaximRepository()
    {
        return maximRepository;
    }
}
