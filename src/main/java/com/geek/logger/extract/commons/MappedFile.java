package com.geek.logger.extract.commons;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: geekmq   映射的File
 * @Author: Captain.Ma
 * @Date: 2018-09-27 11:06
 */

public class MappedFile {
    public static final int OS_PAGE_SIZE = 4096;
    private static String OS = System.getProperty("os.name").toLowerCase();
    private String id;
    private FileChannel fileChannel;
    private int committedPosition;
    private int flushedPosition;
    private String filename;
    private File file;
    private long startConsumeTime;
    private long createTime;
    private boolean autoFlush = false;

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    //可以是一种类型对应一个基础名称。用于区分不同日志源 可选。
    private String baseName;

    public boolean isAutoFlush() {
        return autoFlush;
    }


    public void setAutoFlush(boolean autoFlush) {
        this.autoFlush = autoFlush;
    }

    public long getStartConsumeTime() {
        return startConsumeTime;
    }

    public void setStartConsumeTime(long startConsumeTime) {
        this.startConsumeTime = startConsumeTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    private long modifyTime;
    private long lastAccessTime;
    private int fileStatus;

    private long size;

    RandomAccessFile fis;

    //
    public MappedFile() {
    }


    public MappedFile(final String fileName) throws IOException {
        init(fileName);
    }

    private void init(String filename) throws IOException {
        this.filename = filename;
        this.file = new File(filename);
        boolean ok = false;
        ensureDirOK(this.file.getParent());
        try {
            fis = new RandomAccessFile(this.file, "rw");
            this.fileChannel = fis.getChannel();
            this.size = this.fileChannel.size();
            this.fileStatus = MappedFileConstants.FILE_STATUS_PROCESSING;
            this.committedPosition = 0;
            this.flushedPosition = 0;
            this.startConsumeTime = System.currentTimeMillis();
            this.fileAttributes();
            ok = true;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (!ok && this.fileChannel != null) {
                this.fileChannel.close();
            }
            if (!ok && this.fis != null) {
                fis.close();
            }

        }
    }


    private void destroy() throws IOException {
        if (this.fileChannel != null && this.fileChannel.isOpen()) {
            this.fileChannel.close();
        }
        if (this.fis != null) {
            fis.close();
        }
    }

    public static void ensureDirOK(final String dirName) {
        if (dirName != null) {
            File f = new File(dirName);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }

    public void setFileStatus(int fileStatus) {
        this.fileStatus = fileStatus;
    }



    public String readMessage() throws IOException {
        while (this.committedPosition >= size) {
            if (!this.reset()) {
                this.fileStatus = MappedFileConstants.FILE_STATUS_PROCESSED;
                return "";
            }
        }
        this.fileChannel.position(this.committedPosition);
        this.fileStatus = MappedFileConstants.FILE_STATUS_PROCESSING;
        MappedByteBuffer byteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_ONLY, this.committedPosition, this.fileChannel.size() - this.fileChannel.position());
        this.flushedPosition += byteBuffer.limit();
        if (autoFlush) {
            this.flushed();
        }
        return Charset.forName("UTF-8").decode(byteBuffer).toString();
    }

    public String readMessageByBlock() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(OS_PAGE_SIZE);
        while (this.committedPosition >= size) {
            if (!this.reset()) {
                this.fileStatus = MappedFileConstants.FILE_STATUS_PROCESSED;
                return "";
            }
        }
        this.fileChannel.position(this.committedPosition);
        this.fileStatus = MappedFileConstants.FILE_STATUS_PROCESSING;
        this.fileChannel.read(byteBuffer);
        byteBuffer.flip();
        this.flushedPosition += byteBuffer.limit();
        if (autoFlush) {
            this.flushed();
        }
        return Charset.forName("UTF-8").decode(byteBuffer).toString();
    }


    public void flushed() {
        this.committedPosition = this.flushedPosition;
    }

    public void close() {
        this.fileStatus = MappedFileConstants.FILE_STATUS_DESTROY;
        try {
            this.destroy();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private synchronized boolean reset() throws IOException {
        this.destroy();
        fis = new RandomAccessFile(this.file, "r");
        this.fileChannel = fis.getChannel();
        this.size = this.fileChannel.size();
        if (this.size <= this.committedPosition) {
            return false;
        } else {
            this.flushed();
            return true;
        }
    }

    public List<String> contentToList(String content) {

        String[] strings = content.split("\r|\n");
        List<String> list = new ArrayList(strings.length);
        for (String s : strings) {
            if (s != null && s.length() != 0) {
                list.add(s);
            }
        }
        return list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private void fileAttributes() {
        BasicFileAttributes bAttributes = null;
        try {
            bAttributes = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);
            this.setCreateTime(bAttributes.creationTime().to(TimeUnit.MILLISECONDS));

            this.setModifyTime(file.lastModified());
            this.setLastAccessTime(file.lastModified());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public byte[] readBytes() throws IOException {
        return readBytes(OS_PAGE_SIZE);
    }

    public byte[] readBytes(int limit) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(limit);
        while (this.committedPosition >= size) {
            if (!this.reset()) {
                this.fileStatus = MappedFileConstants.FILE_STATUS_PROCESSED;
                return new byte[0];
            }
        }
        this.fileChannel.position(this.committedPosition);
        this.fileStatus = MappedFileConstants.FILE_STATUS_PROCESSING;
        this.fileChannel.read(byteBuffer);
        byteBuffer.flip();
        this.flushedPosition += byteBuffer.limit();
        if (autoFlush) {
            this.flushed();
        }
        return byteBuffer.array();
    }

    public boolean isProcessed() {
        return this.fileStatus != MappedFileConstants.FILE_STATUS_PROCESSING;
    }

    private static boolean isLinux() {
        return OS.indexOf("linux") >= 0;
    }



    public String defaultId() {
        this.setId(this.filename);
        return this.filename;
    }

}
