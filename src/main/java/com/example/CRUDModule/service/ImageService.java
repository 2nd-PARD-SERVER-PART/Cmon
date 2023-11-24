package com.example.CRUDModule.service;

import com.example.CRUDModule.entity.Image;
import com.example.CRUDModule.repo.ImageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
