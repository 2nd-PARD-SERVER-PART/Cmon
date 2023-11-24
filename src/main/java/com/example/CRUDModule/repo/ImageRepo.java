package com.example.CRUDModule.repo;

import com.example.CRUDModule.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {
//    이미지 업로드 순서로 정렬하는 api
    List<Image> findAllByOrderByUploadTimeAsc();
}
