package com.tcolligan.maximmaker.data;

import android.app.Application;

import com.tcolligan.maximmaker.core.AppExecutors;
import com.tcolligan.maximmaker.core.MyApplication;

/**
 * A singleton to keep track of and initalize repositories.
 * <p>
 * Created on 5/14/18.
 *
 * @author Thomas Colligan
 */

public class RepositoryManager
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private static RepositoryManager INSTANCE;
    private AppExecutors appExecutors;
    private MaximRepository maximRepository;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    private RepositoryManager()
    {
        Application application = MyApplication.getApplication();
        appExecutors = new AppExecutors();
        maximRepository = new MaximRepository(appExecutors, MaximDatabase.getDatabase(application));
    }

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

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

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public MaximRepository getMaximRepository()
    {
        return maximRepository;
    }
}
