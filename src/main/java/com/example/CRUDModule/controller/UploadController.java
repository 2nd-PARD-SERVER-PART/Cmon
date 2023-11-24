package com.example.CRUDModule.controller;

import com.example.CRUDModule.entity.Image;
import com.example.CRUDModule.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UploadController {
    private final ImageService imageService;


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

            //저장할 파일 이름 중간에 "_"를 이용해 구분
            String saveName = uploadPath + File.separator + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    // add image - post
    @PostMapping("/image/sendToBackend")
    public String addImagePost(HttpServletRequest request, @RequestParam("image") MultipartFile file) throws IOException, SerialException, SQLException
    {
        byte[] bytes = file.getBytes();
        Blob blob = new SerialBlob(bytes);
        Image image = new Image();
        image.setImage(blob);
        imageService.createImage(image);
        return "redirect:/";
    }

    private String makeFolder() {
        return "uplopad 시간별로 정렬";
    }
}








