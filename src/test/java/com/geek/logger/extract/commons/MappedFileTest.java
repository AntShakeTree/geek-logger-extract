package com.geek.logger.extract.commons;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description: geek-logger-extract
 * @Author: Captain.Ma
 * @Date: 2018-09-29 16:45
 */

public class MappedFileTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
//    MappedFile mappedFile = null;
    @Test
    public void readMessage() {
        MappedFile mappedFile = null;
        try {
            mappedFile = new MappedFile("1.txt");
            mappedFile.readMessage();
            mappedFile.flushed();
            Thread.sleep(10000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mappedFile.close();
        }
    }

    @Test
    public void readMessageByBlock() {
    }

    @Test
    public void contentToList() {
        MappedFile mappedFile = null;
        try {
            mappedFile = new MappedFile("1.txt");
            List<String> strings=mappedFile.contentToList(mappedFile.readMessage());
            mappedFile.flushed();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mappedFile.close();
        }

    }

    @Test
    public void readBytes() {
        MappedFile mappedFile = null;
        try {
            mappedFile = new MappedFile("1.txt");
            mappedFile.readBytes();
            mappedFile.flushed();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mappedFile.close();
        }
    }


    @Test
    public void readBytes1() {
    }
}