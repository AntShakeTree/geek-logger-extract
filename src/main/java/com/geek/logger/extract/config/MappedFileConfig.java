package com.geek.logger.extract.config;

/**
 * @Description: geekmq
 * @Author: Captain.Ma
 * @Date: 2018-09-28 19:46
 */
public class MappedFileConfig {
    private MappedFileId mappedFileId;
    private boolean autoCommit;
    private MappedFileEvictStrategy mappedFileEvictStrategy;

    public MappedFileConfig(MappedFileId mappedFileId, boolean autoCommit, MappedFileEvictStrategy mappedFileEvictStrategy) {
        this.mappedFileId = mappedFileId;
        this.autoCommit = autoCommit;
        this.mappedFileEvictStrategy = mappedFileEvictStrategy;
    }

    public MappedFileId getMappedFileId() {
        return mappedFileId;
    }

    public void setMappedFileId(MappedFileId mappedFileId) {
        this.mappedFileId = mappedFileId;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public MappedFileEvictStrategy getMappedFileEvictStrategy() {
        return mappedFileEvictStrategy;
    }

    public void setMappedFileEvictStrategy(MappedFileEvictStrategy mappedFileEvictStrategy) {
        this.mappedFileEvictStrategy = mappedFileEvictStrategy;
    }
}
