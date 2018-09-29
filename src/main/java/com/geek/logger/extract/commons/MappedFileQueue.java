package com.geek.logger.extract.commons;


import com.geek.logger.extract.config.MappedFileConfig;
import com.geek.logger.extract.config.MappedFileEvictStrategy;
import com.geek.logger.extract.exceptions.MappedFileConfigNotFoundException;
import com.geek.logger.extract.exceptions.MappedFileNotFoundException;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Description: geekmq   消费队列
 * @Author: Captain.Ma
 * @Date: 2018-09-27 11:05
 */
public class MappedFileQueue {
    //

    private MappedFileConfig mappedFileConfig;

    private static final Map<String, MappedFile> mappedFileMap = new ConcurrentHashMap<>();
    private static final DelayQueue<MapperFileDelayedItem> delayedItemDelayQueue = new DelayQueue<>();


    public MappedFile put(String filename, boolean focus) {
        try {
            MappedFile mappedFile = new MappedFile(filename);
            if (mappedFileConfig == null) {
                throw new MappedFileConfigNotFoundException("Config is required. Please config mapper file");
            }
            mappedFileConfig.getMappedFileId().generate(mappedFile);
            mappedFile.setAutoFlush(mappedFileConfig.isAutoCommit());
            MapperFileDelayedItem<String, MappedFile> mappedFileMapperFileDelayedItem = new MapperFileDelayedItem<>(mappedFileConfig.getMappedFileEvictStrategy(), mappedFile, (p) -> evict(p));

            if (mappedFileMap.containsKey(mappedFile.getId())) {
                MappedFile mappedFile1 = mappedFileMap.get(mappedFile.getId());
                if (mappedFile1.isProcessed() && focus) {
                    mappedFileMap.put(mappedFile.getId(), mappedFile);
                    delayedItemDelayQueue.put(mappedFileMapperFileDelayedItem);
                    return mappedFile;
                } else {
                    mappedFile.close();
                    return null;
                }
            } else {
                delayedItemDelayQueue.put(mappedFileMapperFileDelayedItem);
                mappedFileMap.put(mappedFile.getId(), mappedFile);
            }
            return mappedFile;
        } catch (IOException e) {
            throw new MappedFileNotFoundException("File not found ", e);
        }

    }

    public MappedFile get(String fileId) {
        return mappedFileMap.get(fileId);
    }


    private MappedFile evict(String fileId) {
        MappedFile mappedFile=mappedFileMap.get(fileId);
        if (mappedFile!=null){
            mappedFile.close();
        }
        return mappedFileMap.remove(fileId);
    }


    private static class MapperFileDelayedItem<P, R> implements Delayed {
        private MappedFileEvictStrategy mappedFileEvictStrategy;
        private MappedFile mappedFile;
        private Callback<P, R> callback;

        public MapperFileDelayedItem(MappedFileEvictStrategy mappedFileEvictStrategy, MappedFile mappedFile, Callback<P, R> callback) {
            this.mappedFileEvictStrategy = mappedFileEvictStrategy;
            this.mappedFile = mappedFile;
            this.callback = callback;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long dis = unit.convert(mappedFileEvictStrategy.expired(mappedFile), TimeUnit.MILLISECONDS);
            return dis;
        }

        @Override
        public int compareTo(Delayed o) {
            MapperFileDelayedItem mapperFileDelayed = (MapperFileDelayedItem) o;
            return mappedFileEvictStrategy.compare(this.mappedFile, mapperFileDelayed.mappedFile);
        }


    }

    /**
     * 定时清理过期数据
     */
    private void schedulePull() {
        ExecutorsUtils.singleThreadSubmit(() -> {
            while (true) {
                MapperFileDelayedItem<String, MappedFile> booleanMapperFileDelayedItem = null;
                try {
                    booleanMapperFileDelayedItem = delayedItemDelayQueue.take();
                    booleanMapperFileDelayedItem.callback.call(booleanMapperFileDelayedItem.mappedFile.getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public MappedFileQueue(MappedFileConfig mappedFileConfig) {
        this.mappedFileConfig = mappedFileConfig;
        schedulePull();
    }
}