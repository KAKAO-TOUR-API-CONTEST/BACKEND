package com.example.ai_jeju.repository;

import com.example.ai_jeju.domain.Album;
import com.example.ai_jeju.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findAllByChild(Child child);

    Optional<Album> findByAlbumId(Long albumId);
    List<Album> findAllByChildAndRgtDate(Child child, String rgtDate);

}
