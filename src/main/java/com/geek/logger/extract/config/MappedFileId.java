package com.geek.logger.extract.config;

import com.geek.logger.extract.commons.MappedFile;

/**
 * @Description: geekmq
 * @Author: Captain.Ma
 * @Date: 2018-09-27 14:55
 */
public interface MappedFileId<S> {

    /**
     *  自定义ID
     * @param
     * @return
     */
    public String generate(S source);
}
