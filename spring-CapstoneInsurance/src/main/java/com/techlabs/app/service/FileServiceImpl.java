//package com.techlabs.app.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.techlabs.app.entity.FileItem;
//import com.techlabs.app.exception.AllExceptions;
//import com.techlabs.app.repository.FileRepository;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//@Service
//public class FileServiceImpl implements FileService {
//
//    @Value("${project.file}")
//    private String path;
//
//    @Autowired
//    private FileRepository fileRepository;
//
//    @Override
//    public FileItem saveFileAndReturnItem(MultipartFile file) throws IOException {
//        // Trim the path value and ensure no trailing spaces
//        String trimmedPath = path.trim();
//        Path uploadDir = Paths.get(trimmedPath);
//
//        // Ensure the directory exists
//        if (!Files.exists(uploadDir)) {
//            Files.createDirectories(uploadDir);
//        }
//
//        String name = storeFile(file);
//
//        FileItem fileItem = FileItem.builder()
//                .name(name)
//                .type(file.getContentType())
//                .location(uploadDir.resolve(name).toString()) // Use resolve to build the path
//                .build();
//
//        return fileRepository.save(fileItem);
//    }
//
//    private String storeFile(MultipartFile file) throws IOException {
//        String originalFileName = file.getOriginalFilename();
//        
//        // Check if original file name is valid
//        if (originalFileName == null || originalFileName.isEmpty()) {
//            throw new IOException("Invalid file name.");
//        }
//
//        // Generate a unique file name with the original file extension
//        String name = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
//        
//        // Build the full path using Paths.get() and resolve
//        Path fullPath = Paths.get(path.trim()).resolve(name);
//        
//        // Copy the file to the target location
//        Files.copy(file.getInputStream(), fullPath);
//
//        return name;
//    }
//
//    @Override
//    public FileItem getFileByUUIDName(String name) {
//        return fileRepository.findByName(name)
//                .orElseThrow(() -> new AllExceptions.FileNameException("File with UUID name: " + name + " not found"));
//    }
//}
//
//


package com.techlabs.app.service;

import com.techlabs.app.entity.FileItem;
import com.techlabs.app.exception.AllExceptions;
import com.techlabs.app.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

  @Value("${project.file}")
  private String path;

  @Autowired
  private FileRepository fileRepository;

  @Override
  public FileItem saveFileAndReturnItem(MultipartFile file) throws IOException {
    String name = storeFile(file);

    FileItem fileItem = FileItem.builder().name(name).type(file.getContentType())
        .location(path + File.separator + name).build();

    return fileRepository.save(fileItem);
  }

  private String storeFile(MultipartFile file) throws IOException {
    String name = UUID.randomUUID()
        + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
    String fullPath = path + File.separator + name;

    Files.copy(file.getInputStream(), Paths.get(fullPath));

    return name;
  }

  @Override
  public FileItem getFileByUUIDName(String name) {
    return fileRepository.findByName(name)
        .orElseThrow(() -> new AllExceptions.FileNameException("File with UUID name: " + name + " not found"));
  }
}