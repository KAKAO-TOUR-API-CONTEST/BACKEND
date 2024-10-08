package com.example.ai_jeju.controller;

import com.example.ai_jeju.dto.ChildRequest;
import com.example.ai_jeju.dto.ModifyChildProfileRequest;
import com.example.ai_jeju.dto.ModifyMyPageRequest;
import com.example.ai_jeju.exception.UserNotFoundException;
import com.example.ai_jeju.jwt.TokenProvider;
import com.example.ai_jeju.service.MyJejuService;
import com.example.ai_jeju.service.UserService;
import com.example.ai_jeju.util.ResponseDto;
import com.example.ai_jeju.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MyJejuController {

    private final TokenProvider tokenProvider;
    @Autowired
    private MyJejuService myJejuService;
    @Autowired
    private UserService userService;

    public MyJejuController(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    // 마이제주 홈

    @GetMapping("/myjeju")
    public ResponseDto getMyJeju(@RequestHeader("Authorization") String token){
        // Bearer 토큰 형식에서 "Bearer " 부분 제거
        String accessToken = token.replace("Bearer ", "");
        if (tokenProvider.validToken(accessToken)) {
            Long userId = tokenProvider.getUserId(accessToken);
            return ResponseUtil.SUCCESS("마이제주 조회에 성공하였습니다.", myJejuService.getMyJeju(userId));
        } else {
            return ResponseUtil.ERROR("마이제주 조회 중 문제가 발생하였습니다.", null);
        }

    }

    // 마이제주 : 내 정보 조회하기
    @GetMapping("/myjeju/mypage")
    public ResponseDto myPage(@RequestHeader("Authorization") String token){
        // Bearer 토큰 형식에서 "Bearer " 부분 제거
        String accessToken = token.replace("Bearer ", "");
        if (tokenProvider.validToken(accessToken)) {
            Long userId = tokenProvider.getUserId(accessToken);
            return ResponseUtil.SUCCESS("마이페이지 조회에 성공하였습니다.", userService.getMyPage(userId));
        } else {
            return ResponseUtil.ERROR("마이페이지 조회 중 문제가 발생하였습니다.", null);
        }
    }

    // 마이제주 : 아이 조회하기

    @GetMapping("/myjeju/child")
    public ResponseDto myPaPge(@RequestParam Long childId){
       return ResponseUtil.SUCCESS("아이 상세조회에 성공하였습니다.", myJejuService.getMyChild(childId));
    }

    @DeleteMapping("/myjeju/child")
    public ResponseDto deleteMyChild(Long childId){
        try{
            myJejuService.deleteMyChild(childId);
            return ResponseUtil.SUCCESS("아이 삭제에 성공하였습니다",null);
        }catch(Exception e){
            return ResponseUtil.ERROR(e.getMessage(),null);
        }


    }

    // 마이제주 : 아이 추가하기
    @PostMapping("/myjeju/child")
    public ResponseDto myPageAddChild(@RequestHeader("Authorization") String token, @RequestBody ChildRequest childRequest){
        // Bearer 토큰 형식에서 "Bearer " 부분 제거
        String accessToken = token.replace("Bearer ", "");
        if (tokenProvider.validToken(accessToken)) {
            Long userId = tokenProvider.getUserId(accessToken);
            try{
                userService.registerChild(userId,childRequest);
                return ResponseUtil.SUCCESS("아이 추가에 성공하였습니다.", null);
            }catch (Exception e){
                return ResponseUtil.ERROR(e.getMessage(), null);
            }
        } else {
            return ResponseUtil.ERROR("토큰 유효성 문제가 발생하였습니다.", null);
        }
    }

    @PutMapping("/myjeju/mypage/profileimg")
    public ResponseEntity<String> updateProfileImage(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {

        String accessToken = token.replace("Bearer ", "");

        if (tokenProvider.validToken(accessToken)) {
            Long userId = tokenProvider.getUserId(accessToken);

            String profileimg = request.get("profileimg");

            myJejuService.updateProfile(userId, profileimg);

            return ResponseEntity.ok("success");
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지않은토큰");
        }
    }

    @PutMapping("/myjeju/mypage/nickname")
    public ResponseEntity<String> updateNickname(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {

        String accessToken = token.replace("Bearer ", "");


        if (tokenProvider.validToken(accessToken)) {

            Long userId = tokenProvider.getUserId(accessToken);


            String nickname = request.get("nickname");


            myJejuService.updateNickname(userId, nickname);


            return ResponseEntity.ok("success");
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지않은토큰");
        }
    }


    // 마이제주 : 마이페이지 프로필 이미지 삭제
    @DeleteMapping("/myjeju/mypage/profileimg")
    public ResponseEntity<String> deleteProfileImage(@RequestParam Long userId) {
        myJejuService.deleteProfileImage(userId);
        return ResponseEntity.ok("deleted");
    }


//    @PostMapping("/myjeju/mypage/presign")
//    public ResponseEntity<String> createPresignedUrl(@RequestBody Map<String, String> request) {
//        String filePath = request.get("filePath");
//        String presignedUrl = s3Service.createPresignedUrl(filePath);
//        return ResponseEntity.ok(presignedUrl);
//    }


    @PutMapping("/myjeju/mypage/update")
    public ResponseDto updateMyPage (@RequestHeader("Authorization") String token, @RequestBody ModifyMyPageRequest modifyMyPageRequest) {

        String accessToken = token.replace("Bearer ", "");
        if (tokenProvider.validToken(accessToken)) {
            try {
                Long userId = tokenProvider.getUserId(accessToken);
                myJejuService.updateUser(userId, modifyMyPageRequest);
                return  ResponseUtil.SUCCESS("마이페이지 수정에 성공하였습니다.", null);
            } catch (UserNotFoundException e) {
                return ResponseUtil.FAILURE(e.getMessage(), null);
            } catch (Exception e) {
                return ResponseUtil.FAILURE(e.getMessage(), null);
            }
        }else{
            return ResponseUtil.FAILURE("고객 정보를 찾지 못하였습니다.", null);
        }
    }

    @PutMapping("/myjeju/mypage/child/profile")
    public ResponseEntity<String> updateChildProfile(@RequestHeader("Authorization") String token,
                                                     @RequestBody ModifyChildProfileRequest modifychildProfileRequest) {

        String accessToken = token.replace("Bearer ", "");

        // 토큰이 유효한지 확인
        if (tokenProvider.validToken(accessToken)) {

            Long childId = modifychildProfileRequest.getChildId();

            if (childId == null) {
                return ResponseEntity.badRequest().body("ChildID가 존재하지 않음");
            }

            // 서비스 호출하여 프로필 업데이트 수행
            myJejuService.updateChildProfile(childId,
                    modifychildProfileRequest.getBirthDate(),
                    modifychildProfileRequest.getRelation(),
                    modifychildProfileRequest.getGender());

            return ResponseEntity.ok("Profile updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PutMapping("/myjeju/mypage/child/profileimg")
    public ResponseEntity<String> updateChildProfileImage(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {

        String accessToken = token.replace("Bearer ", "");

        // 토큰이 유효한지 확인
        if (tokenProvider.validToken(accessToken)) {

            Long userId = tokenProvider.getUserId(accessToken);


            String profileimg = request.get("profileimg");
            String childIdStr = request.get("childId");


            if (profileimg == null || childIdStr == null) {
                return ResponseEntity.badRequest().body("Invalid request parameters");
            }

            try {

                Long childId = Long.parseLong(childIdStr);


                myJejuService.updateChildImgProfile(childId, profileimg);

                return ResponseEntity.ok("success");
            } catch (NumberFormatException e) {

                return ResponseEntity.badRequest().body("Invalid childId format");
            }
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지않은토큰");
        }
    }




}
