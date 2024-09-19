//package com.techlabs.app.service;
//
//import java.io.IOException;
//
//import org.apache.tomcat.util.http.fileupload.FileItem;
//import org.springframework.web.multipart.MultipartFile;
//
//public interface FileService {
//
//    FileItem saveFileAndReturnItem(MultipartFile file) throws IOException;
//
//    FileItem getFileByUUIDName(String name);
//}

package com.techlabs.app.service;

import com.techlabs.app.entity.FileItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    FileItem saveFileAndReturnItem(MultipartFile file) throws IOException;

    FileItem getFileByUUIDName(String name);
}