package com.geek.logger.extract.config;

import com.geek.logger.extract.commons.MappedFile;
import com.geek.logger.extract.commons.MappedFileQueue;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

/**
 * @Description: geek-logger-extract
 * @Author: Captain.Ma
 * @Date: 2018-09-29 17:47
 */

public class DateMappedFiledTest {
    private MappedFileId mappedFileId = null;
    private DateEvictStrategy dateEvictStrategy = new DateEvictStrategy(1);
    private MappedFileConfig mappedFileConfig = null;
    private MappedFileQueue mappedFileQueue = null;

    @Before
    public void before() {
        mappedFileId=new DateMappedFiled("");
        mappedFileConfig = new MappedFileConfig(mappedFileId, true, dateEvictStrategy);
        mappedFileQueue = new MappedFileQueue(mappedFileConfig);
        mappedFileQueue.put("1.txt",false);
    }

    @Test
    public void generate() {
        String id=mappedFileId.generate(null);
        MappedFile mappedFile=mappedFileQueue.get(id);
        assertTrue(mappedFile!=null);
    }
}