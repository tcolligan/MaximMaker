package com.tcolligan.maximmaker.utils;

import android.util.Log;

import com.tcolligan.maximmaker.BuildConfig;

/**
 * A logging utility class that should be used in place of the standard
 * {@link android.util.Log} class.
 * <p/>
 * This class makes sure that only error and warning logs are printed out
 * for release builds.
 * <p/>
 * Created on 7/17/2016.
 *
 * @author Thomas Colligan
 */
@SuppressWarnings("PointlessBooleanExpression")
public class LogUtils
{
    //region Constructor
    private LogUtils()
    {
        // Private constructor to make sure that nobody can instantiate this class.
    }
    //endregion

    //region Log Methods

    /**
     * Send a {@link Log#VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg)
    {
        if (!BuildConfig.DEBUG)
        {
            return 0;
        }

        return Log.v(tag, msg);
    }

    /**
     * Send a {@link Log#VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int v(String tag, String msg, Throwable tr)
    {
        if (!BuildConfig.DEBUG)
        {
            return 0;
        }

        return Log.v(tag, msg, tr);
    }

    /**
     * Send a {@link Log#DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg)
    {
        if (!BuildConfig.DEBUG)
        {
            return 0;
        }

        return Log.d(tag, msg);
    }

    /**
     * Send a {@link Log#DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int d(String tag, String msg, Throwable tr)
    {
        if (!BuildConfig.DEBUG)
        {
            return 0;
        }

        return Log.d(tag, msg, tr);
    }

    /**
     * Send an {@link Log#INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg)
    {
        if (!BuildConfig.DEBUG)
        {
            return 0;
        }

        return Log.i(tag, msg);
    }

    /**
     * Send a {@link Log#INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int i(String tag, String msg, Throwable tr)
    {
        if (!BuildConfig.DEBUG)
        {
            return 0;
        }

        return Log.i(tag, msg, tr);
    }

    /**
     * Send a {@link Log#WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg)
    {
        return Log.w(tag, msg);
    }

    /**
     * Send a {@link Log#WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int w(String tag, String msg, Throwable tr)
    {
        return Log.w(tag, msg, tr);
    }

    /**
     * Send a {@link Log#WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param tr  An exception to log
     */
    public static int w(String tag, Throwable tr)
    {
        return Log.w(tag, tr);
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg)
    {
        return Log.e(tag, msg);
    }

    /**
     * Send a {@link Log#ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int e(String tag, String msg, Throwable tr)
    {
        return Log.e(tag, msg, tr);
    }
    //endregion
}
