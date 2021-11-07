package com.lagou.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 元敬
 * @Version 1.0
 */
public interface FileService {

    public String upload(MultipartFile file);

}
