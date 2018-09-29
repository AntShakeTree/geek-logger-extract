package com.geek.logger.extract.config;

import com.geek.logger.extract.commons.MappedFile;

/**
 * @Description: geekmq
 * @Author: Captain.Ma
 * @Date: 2018-09-28 19:48
 */
public interface MappedFileEvictStrategy {
    /**
     * 退出策略
     * 过期：指的是日志文件已经全部处理完成，并且以后也不会新增数据
     *
     * @param mappedFile
     * @return
     */
    public long expired(MappedFile mappedFile);

    /**
     *  计算优先顺序
     *  一般按照具体实现的策略进行排序。
     *
     * @param thisFile
     * @param otherFile
     * @return
     */
    public int compare(MappedFile thisFile,MappedFile otherFile);


}
