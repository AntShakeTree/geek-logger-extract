package com.geek.logger.extract.commons;

import java.util.concurrent.ExecutorService;

/**
 * @Description: geek-logger-extract
 * @Author: Captain.Ma
 * @Date: 2018-09-29 16:29
 */
public class ExecutorsUtils {

    private static final ExecutorService singleThreadPool=ThreadUtils.newSingleThreadExecutor("File_Evict",true);
    public static void singleThreadSubmit(Runnable runnable){
        singleThreadPool.submit(runnable);
    }
}
