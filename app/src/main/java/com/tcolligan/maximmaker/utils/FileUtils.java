package com.tcolligan.maximmaker.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * A utility class with static methods to help with common file operations.
 *
 * Created on 7/24/2016.
 *
 * @author Thomas Colligan
 */
public class FileUtils
{
    //==============================================================================================
    // Constructor
    //==============================================================================================

    private FileUtils()
    {
        // Private constructor to make sure that nobody can instantiate this utility class.
    }

    //==============================================================================================
    // Static Helper Methods
    //==============================================================================================

    public static String retrieveFileDataAsText(File file) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = bufferedReader.readLine()) != null)
        {
            stringBuilder.append(line);
        }

        bufferedReader.close();

        return stringBuilder.toString();
    }

    public static boolean createNewFileIfFileDoesNotExist(File file) throws IOException
    {
        if (!file.exists())
        {
            return file.createNewFile();
        }

        return true;
    }
}
