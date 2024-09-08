package com.example.ai_jeju.repository;

import com.example.ai_jeju.domain.Store;
import com.example.ai_jeju.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreId(Long storeId);

    Optional<Store> findById(Long id);

    List<Store> findByCategoryId(int categoryId);
    @Query(value = "SELECT * FROM stores ORDER BY RAND()", nativeQuery = true)
    List<Store> findAllOrderByRandomNative();

    @Query(value = "SELECT * FROM stores WHERE name LIKE %:keyword%", nativeQuery = true)
    List<Store> findBySearch(String keyword);

    @Query(value = "SELECT * FROM stores WHERE name LIKE %:keyword% AND categoryId = :categoryId", nativeQuery = true)
    List<Store> findByKeywordAndCategoryId(String keyword, int categoryId);

    List<Store> findByCategoryId(Long categoryId);


}
