package com.example.CRUDModule.service;

import com.example.CRUDModule.entity.Image;
import com.example.CRUDModule.repo.ImageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepo imageRepo;
// 이미지 받으면 db에 저장하는 api
    public Image createImage(Image image) {
        imageRepo.save(image);

        return image;
    }
// 이미지 업로드 순서로 정렬하는 api
    public List<Image> listAllByOrderByUploadTimeDesc() {
        return imageRepo.findAllByOrderByUploadTimeAsc();
    }
// 이미지 List로 다시 보내는 api + 이미지 보내고 db에서 전부 삭제
    public String deleteAll() {
        imageRepo.deleteAll();
        return "이미지 전부 삭제 완료";
    }

//    업로드 된 순서대로 정렬하는 api
    @Value("/Users/juhyun/Desktop/cmon")
    private String uploadPath;
    public List<Path> getSortedImages() {
        try {
            Path dirPath = Paths.get(uploadPath);

            // directory에 있는 모든 파일들 list로 가져오기
            List<Path> files = Files.list(dirPath)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            // 파일 이름에 있는 timestamp를 기준으로 정렬
            files.sort((p1, p2) -> {
                String name1 = p1.getFileName().toString();
                String name2 = p2.getFileName().toString();

                // 이름에 있는 timestamp 가져오기
                return extractTimestamp(name1).compareTo(extractTimestamp(name2));
            });

            int count = 1;
            for (Path file : files) {
                Path newPath = dirPath.resolve(count + ".jpg");
                Files.move(file, newPath);
                count++;
            }

            return files;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public void renameFile(List<Path> sortedFiles) {
        try {
            int count = 1;
            for (Path file : sortedFiles) {
                String fileExtension = getFileExtension(file.getFileName().toString());
                Path newPath = Paths.get(uploadPath).resolve(count + "." + fileExtension);
                Files.move(file, newPath);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return dotIndex >= 0 ? fileName.substring(dotIndex + 1) : "";
    }



    private String extractTimestamp(String filename) {
        // 파일 이름에서 timestamp 부분만 추출
        try {
            return filename.split("_")[1]; // timestamp 추출
        } catch (Exception e) {
            return ""; //파일 이름이 사용 불가면
        }
    }

}
