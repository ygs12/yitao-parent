package com.gerry.yitao.yitaocartservice.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/29 23:42
 * @Description:
 */
public class ThreadUtil {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable) {
        executorService.submit(runnable);
    }
}
