package com.example.CRUDModule.controller;

import com.example.CRUDModule.entity.Image;
import com.example.CRUDModule.repo.ImageRepo;
import com.example.CRUDModule.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UploadController {
    private final ImageService imageService;
    private final ImageRepo imageRepository;


    @Value("/Users/juhyun/Desktop/cmon")
    private String uploadPath;
    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadFile(@RequestParam(value = "image") MultipartFile uploadFile) {
            // 실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();

            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
//            String folderPath = makeFolder();

//            파일이름이 고유한지 확인
            String uuid = UUID.randomUUID().toString();

        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());

            //저장할 파일 이름 중간에 "_"를 이용해 구분
        String saveName = uploadPath + File.separator + uuid + "_" + timestamp + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @GetMapping("/numOfImages")
    public ResponseEntity<Integer> getNumOfImages() {
        Path dirPath = Paths.get(uploadPath);
        try {
            long numOfImages = Files.list(dirPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".jpg") || path.toString().toLowerCase().endsWith(".jpeg"))
                    .count();

            return ResponseEntity.ok((int)numOfImages);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/download/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        // Ensure the list of images is sorted and up-to-date
        List<Path> sortedImages = imageService.getSortedImages(); // This call updates the sorted list

        // Rest of your existing serveFile logic...
        Path filePath = Paths.get(uploadPath).resolve(filename);
        Resource file = new UrlResource(filePath.toUri());

        if (file.exists() && file.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/sort/images")
    public ResponseEntity<List<Path>> listSortedImages() {
        List<Path> sortedImages = imageService.getSortedImages();
        return ResponseEntity.ok(sortedImages);
    }

    private String makeFolder() {
        return "uplopad 시간별로 정렬";
    }
}








