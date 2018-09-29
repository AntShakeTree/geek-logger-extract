package com.geek.logger.extract.config;

import com.geek.logger.extract.commons.MappedFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: geek-logger-extract   用日期与文件基础名称进行区分文件
 * @Author: Captain.Ma
 * @Date: 2018-09-29 17:37
 */
public class DateMappedFiled implements MappedFileId<MappedFile> {
    private DateFormat dateFormat = new SimpleDateFormat("YYYYMMDD");
    private String baseName;

    @Override
    public String generate(MappedFile mappedFile) {
        String path = dateFormat.format(new Date());
        if (mappedFile == null) {
            mappedFile = new MappedFile();
            mappedFile.setBaseName(mappedFile.getBaseName());
        }
        mappedFile.setId(path + baseName);
        return mappedFile.getId();
    }

    public DateMappedFiled(String baseName) {
        this.baseName = baseName;
    }
}
