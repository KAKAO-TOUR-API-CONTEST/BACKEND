package com.example.ai_jeju.repository;


import com.example.ai_jeju.domain.Bookmark;
import com.example.ai_jeju.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

//    List<Bookmark> findByUserId();
// userId와 storeId를 기준으로 북마크가 존재하는지 여부를 확인하는 메서드
boolean existsByUserIdAndStoreId(Long userId, Long storeId);


}
