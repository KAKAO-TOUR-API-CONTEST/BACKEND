package com.example.ai_jeju.dto;

import com.example.ai_jeju.domain.Child;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignUpRequest {

    private String name;
    private String nickname;
    private String email;
    private String profileImg;
    private String provider;
    private String phoneNum;
    private Boolean ifRcmd;
    private Boolean selectedCk1;
    private Boolean selectedCk2;
    /**
     * 함께 보낼 동반 아동에 대한 정보 -> 여러명일 수도 있으므로 배열 형식으로 받는다.
     * private List<Child> child;
     */
    private List<ChildRequest> child;

}