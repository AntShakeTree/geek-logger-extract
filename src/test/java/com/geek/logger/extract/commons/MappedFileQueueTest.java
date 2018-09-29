package com.geek.logger.extract.commons;

import com.geek.logger.extract.commons.MappedFile;
import com.geek.logger.extract.commons.MappedFileQueue;
import com.geek.logger.extract.config.DateEvictStrategy;
import com.geek.logger.extract.config.MappedFileConfig;
import com.geek.logger.extract.config.MappedFileId;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @Description: geekmq
 * @Author: Captain.Ma
 * @Date: 2018-09-29 13:35
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class MappedFileQueueTest {


    private MappedFileId<MappedFile> mappedFileId= mappedFile -> mappedFile.defaultId();
    private DateEvictStrategy dateEvictStrategy = new DateEvictStrategy(1);
    private MappedFileConfig mappedFileConfig= new MappedFileConfig(mappedFileId, true, dateEvictStrategy);
    private MappedFileQueue  mappedFileQueue = new MappedFileQueue(mappedFileConfig);

    @Test
    public void put() {
        mappedFileQueue.put("1.txt", false);
    }


//    @Benchmark
    @Test
    public void read() throws IOException, InterruptedException {
        mappedFileQueue.put("1.txt", false);
        MappedFile mappedFile1 = mappedFileQueue.get("1.txt");
//       StringBuilder re =new StringBuilder();
//
//       while (mappedFile1.isProcessed()){
//           re.append(mappedFile1.readMessage());
//       }
//        System.out.println(re.toString());
//       Thread.sleep(10000);
//        while (mappedFile1.isProcessed()){
//            re.append(mappedFile1.readMessage());
//        }
//        System.out.println("after ====>"+re.toString());
    }

    @Test
    public void readMessage() throws IOException, InterruptedException {
        mappedFileQueue.put("1.txt", false);
        MappedFile mappedFile1 = mappedFileQueue.get("1.txt");

        System.out.println(mappedFile1.readMessage());
        Thread.sleep(10000);
        System.out.println("====>"+mappedFile1.readMessage());
    }

    @Test
    public void jmh() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .mode (Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.seconds(1))
                .measurementTime(TimeValue.seconds(1))
                .threads(2)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }







}