package com.geek.logger.extract.config;

import com.geek.logger.extract.commons.MappedFile;

/**
 * @Description: geekmq
 * @Author: Captain.Ma
 * @Date: 2018-09-28 19:51
 */
public class DateEvictStrategy implements MappedFileEvictStrategy {
    private long expired;
    private static long DEFAULT_EXPIRED_TIME_MS = 24 * 60 * 60 * 1000;

    /**
     * 24小时没有新增数据则认为已经过期。
     *
     * @param mappedFile
     * @return
     */
    @Override
    public long expired(MappedFile mappedFile) {
        long distanceTime = System.currentTimeMillis() - mappedFile.getModifyTime();
        return expired-distanceTime ;
    }

    @Override
    public int compare(MappedFile thisFile, MappedFile otherFile) {
        return Long.compare(thisFile.getModifyTime(), otherFile.getModifyTime());
    }


    public DateEvictStrategy(long expired) {
        if (expired <= DEFAULT_EXPIRED_TIME_MS) {
            this.expired = DEFAULT_EXPIRED_TIME_MS;
        }
    }
}
