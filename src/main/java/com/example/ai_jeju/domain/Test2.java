package com.example.ai_jeju.domain;


import jakarta.persistence.*;
import lombok.*;

@Table(name="test2")
@NoArgsConstructor(access= AccessLevel.PROTECTED) //기본생성자
@Getter
@Entity
@AllArgsConstructor // 모든 필드를 초기화하는 생성자
@Builder // 빌더 패턴
public class Test2 {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storeId", updatable = false, unique = true)
    private Long storeId;

    @Column(name = "nearbyStoreId", updatable = false)
    private Long nearbyStoreId;

    @Column(name = "name",updatable = false)
    String name;

    //이미지 소스
    @Column(name = "imgSrc",updatable = false,nullable = true, columnDefinition = "TEXT")
    String imgSrc;

    @Column(name = "address",updatable = false,nullable = true)
    String address;


    @Column(name = "mapX",updatable = false,nullable = true)
    Double mapX;

    @Column(name = "mapY",updatable = false,nullable = true)
    Double mapY;




    /**
     카테고리 value
     1 : 숙박
     2 : 음식점
     3 : 레져
     */

    //유모차 대여여부
    @Column(name = "stroller",updatable = false,nullable = true)
    Boolean stroller;

    //유모차 편의성
    @Column(name = "strollerVal",updatable = false,nullable = true)
    Boolean strollerVal;

    //아이 스페어 체어
    @Column(name = "babySpareChair",updatable = false,nullable = true)
    Boolean babySpareChair;

    //아이 놀이방
    @Column(name = "playground",updatable = false,nullable = true)
    Boolean playground;

    @Column(name = "noKidsZone",updatable = false,nullable = true)
    String noKidsZone;

    @Column(name = "nokidsdetail",updatable = false,nullable = true)
    String nokidsdetail;

    @Column(name = "categoryId",updatable = false,nullable = true)
    Integer categoryId;

    @Column(name = "operationTime",updatable = false,nullable = true , length = 1024)
    String operationTime;


    @Column(name = "tel",updatable = false,nullable = true)
    String tel;

    @Column(name = "pet",updatable = false,nullable = true)
    Boolean pet;

    @Column(name = "parking",updatable = false,nullable = true)
    Boolean parking;


}
