//package com.techlabs.app.controller;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import org.apache.tomcat.util.http.fileupload.FileItem;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.techlabs.app.service.FileService;
//
//@RestController
//@RequestMapping("/E-Insurance/file")
//public class FileController {
//
//    @Autowired
//    private FileService fileService;
//    
//    
//
//    public FileController(FileService fileService) {
//		super();
//		this.fileService = fileService;
//	}
//
//	@PostMapping(value = {"/upload"}, consumes = {"multipart/form-data"})
//    public ResponseEntity<FileItem> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//        FileItem fileItem = fileService.saveFileAndReturnItem(file);
//        return new ResponseEntity<>(fileItem, HttpStatus.CREATED);
//    }
//
////    @GetMapping("/view/{name}")
////    public ResponseEntity<byte[]> viewFileByName(@PathVariable String name) throws IOException {
////        FileItem fileItem = fileService.getFileByUUIDName(name);
////        if (fileItem != null) {
////            File file = new File(fileItem.getLocation());
////            byte[] content = Files.readAllBytes(file.toPath());
////
////            return ResponseEntity.ok()
////                    .contentType(MediaType.parseMediaType(fileItem.getType()))
////                    .body(content);
////        } else {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
////        }
////    }
//
//	
//	@GetMapping("/view/{name}")
//	public ResponseEntity<byte[]> viewFileByName(@PathVariable String name) throws IOException {
//	    FileItem fileItem = fileService.getFileByUUIDName(name);
//	    if (fileItem != null) {
//	        // Normalize the file path to avoid issues with backslashes
//	        String normalizedPath = fileItem.getLocation().replace("\\", "/");
//
//	        // Use Paths.get() to construct a valid path
//	        Path filePath = Paths.get(normalizedPath);
//
//	        byte[] content = Files.readAllBytes(filePath);
//
//	        return ResponseEntity.ok()
//	                .contentType(MediaType.parseMediaType(fileItem.getType()))
//	                .body(content);
//	    } else {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//	    }
//	}
//
//
//}

package com.techlabs.app.controller;

import com.techlabs.app.entity.FileItem;
import com.techlabs.app.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/E-Insurance/file")
public class FileController {

    @Autowired
    private FileService fileService;
    
    

    public FileController(FileService fileService) {
		super();
		this.fileService = fileService;
	}

	@PostMapping(value = {"/upload"}, consumes = {"multipart/form-data"})
    public ResponseEntity<FileItem> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        FileItem fileItem = fileService.saveFileAndReturnItem(file);
        return new ResponseEntity<>(fileItem, HttpStatus.CREATED);
    }

    @GetMapping("/view/{name}")
    public ResponseEntity<byte[]> viewFileByName(@PathVariable String name) throws IOException {
        FileItem fileItem = fileService.getFileByUUIDName(name);
        if (fileItem != null) {
            File file = new File(fileItem.getLocation());
            byte[] content = Files.readAllBytes(file.toPath());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileItem.getType()))
                    .body(content);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}