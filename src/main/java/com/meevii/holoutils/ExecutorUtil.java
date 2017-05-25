package com.meevii.holoutils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhao on 16/3/15.
 */
public class ExecutorUtil {

    private static ExecutorService mNormalExecutorService;
    private static ExecutorService mLowExecutorService;

    private static ExecutorService getNormalInstance() {
        if (mNormalExecutorService == null) {
            synchronized (ExecutorUtil.class) {
                if (mNormalExecutorService == null) {
                    mNormalExecutorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                            60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new NormalPriorityFactory());
                }
            }
        }
        return mNormalExecutorService;
    }

    private static ExecutorService getLowInstance() {
        if (mLowExecutorService == null) {
            synchronized (ExecutorUtil.class) {
                if (mLowExecutorService == null) {
                    mLowExecutorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                            60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new LowPriorityFactory());
                }
            }
        }
        return mLowExecutorService;
    }

    public static void submitNormal(Runnable runnable) {
        getNormalInstance().submit(runnable);
    }

    public static void submitLow(Runnable runnable) {
        getLowInstance().submit(runnable);
    }

    private static class NormalPriorityFactory implements ThreadFactory {

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.NORM_PRIORITY);
            thread.setUncaughtExceptionHandler((thread1, ex) -> Log.e("executil",thread1.getName() + " Error !" + ex.getMessage()));
            return thread;
        }
    }

    private static class LowPriorityFactory implements ThreadFactory {

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setUncaughtExceptionHandler((thread1, ex) -> Log.e("executil",thread1.getName() + " Error !" + ex.getMessage()));
            return thread;
        }
    }

}