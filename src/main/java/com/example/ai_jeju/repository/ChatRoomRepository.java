package com.example.ai_jeju.repository;

import com.example.ai_jeju.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findByRoomId(String roomId);


    @Query("SELECT r.roomId FROM ChatRoom r")
    List<String> findAllRoomIds();

}