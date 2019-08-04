package com.zymonster.domain;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhouyang
 * @date 2019/4/9
 */
public class FileUpload {

    private String name;

    private String author;

    private MultipartFile multipartFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
