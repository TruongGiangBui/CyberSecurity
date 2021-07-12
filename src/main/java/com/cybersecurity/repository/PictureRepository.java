package com.cybersecurity.repository;

import com.cybersecurity.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {
    @Override
    List<Picture> findAll();
    @Query("select picture.path from Picture picture")
    List<String> findAllPictures();
}
