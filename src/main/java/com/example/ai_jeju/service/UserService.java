package com.example.ai_jeju.service;

import com.example.ai_jeju.domain.Child;
import com.example.ai_jeju.domain.RefreshToken;
import com.example.ai_jeju.domain.User;
import com.example.ai_jeju.dto.WithdrawRequest;
import com.example.ai_jeju.dto.SignUpRequest;
import com.example.ai_jeju.generator.NickNameGenerator;
import com.example.ai_jeju.handler.SignUpHandler;
import com.example.ai_jeju.jwt.TokenProvider;
import com.example.ai_jeju.repository.ChildRepository;
import com.example.ai_jeju.repository.RefreshTokenRepository;
import com.example.ai_jeju.repository.UserRepository;
import com.example.ai_jeju.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    private SignUpHandler signUpHandler;

    public String signUp(SignUpRequest signUpRequest) throws IOException {

        // 이미 가입한 회원인지 확인한다.
        Optional<User> existingUserByEmail = userRepository.findByEmail(signUpRequest.getEmail());

        // DB에 회원이 있을 때 -> 기존 회원일 경우
        /*--------------------------------------------------------------------------------------------------*/
        if (existingUserByEmail.isPresent()) {
            User user = this.findByEmail(signUpRequest.getEmail());
            String token = tokenProvider.generateToken(user,ACCESS_TOKEN_DURATION);
            //accessToken반환
            //여기 부분 바꿔야함.
            return token;
        }
        /*--------------------------------------------------------------------------------------------------*/
        // db에 회원정보 없음 -> 새로운 회원 추가
        else{

            /*
            //요청에서 들어온 닉네임
            String nick = signUpRequest.getNickname();

            if(nick==null){
                //System.out.println("nickname is null");
                nick = new NickNameGenerator().getNickname();
            }

            // Save new user using builder pattern
            User newUser = User.builder()
                    .name(signUpRequest.getName())
                    .nickname(nick)
                    .provider(signUpRequest.getProvider())
                    .email(signUpRequest.getEmail())
                    //.profile(signUpRequest.getProfile())
                    .provider(signUpRequest.getProvider())
                    .build();

            userRepository.save(newUser);//User정보 저장

            //동반아동
            List<Child> childList = signUpRequest.getChild();
            //일단 한번 해보자.
            for(int i=0; i<childList.size(); i++){
                Child child = Child.builder()
                        .userId(newUser.getId())
                        .childName(childList.get(i).getChildName())
                        .birthDate(childList.get(i).getBirthDate())
                        .gender(childList.get(i).getGender())
                        .build();

                childRepository.save(child);
            }

            String token = tokenProvider.generateToken(newUser, ACCESS_TOKEN_DURATION);
            //String형 refresh_token과 RefreshToken형 refreshToken 헷갈리지 말 것
            String refresh_token = tokenProvider.generateToken(newUser, REFRESH_TOKEN_DURATION);

            RefreshToken refreshToken = RefreshToken.builder()
                    .refresh_token(refresh_token)
                    .userId(newUser.getId())
                    .build();

            refreshTokenRepository.save(refreshToken);

            return refresh_token;*/

            String accessToken = signUpHandler.successHadler(signUpRequest);

            return accessToken;
        }
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(
                        ()-> new IllegalArgumentException("unexpected user"));
    }





    //회원 탈퇴하기
    public String withDraw(WithdrawRequest withDrawRequest){

        String email = withDrawRequest.getEmail();
        String accessToken = withDrawRequest.getAccessToken();
        //기본 빈 url
        String url ="";
        //provider 추출하기
        //email 기반으로 삭제할 user 객체 찾기
        User delUser = this.findByEmail(withDrawRequest.getEmail());
        String provider = delUser.getProvider();
        /*--------------------------------------------------------------------------------------------------*/
        switch (provider){
            case "kakao":
                url = "https://kapi.kakao.com/v1/user/unlink";
                //헤더 만들기
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.set("Authorization", "Bearer " + accessToken);

                //전달할 Header 기반 HttpEntity 만들기
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

                ResponseEntity<String> result = ResponseEntity.status(response.getStatusCode()).body(response.getBody());

                System.out.println("카카오 탈퇴 결과값 :"+result);
                //맞을때
                if(result.equals("200")){
                    userRepository.delete(delUser);
                    return("delete success");
                }
                //아니면 그냥 break..
                else{
                    return("delete fail");
                }

                /*--------------------------------------------------------------------------------------------------*/
            case "google":
                url  = "https://accounts.google.com/o/oauth2/revoke?token="+accessToken;
                ResponseEntity<String> googleRes = restTemplate.getForEntity(url, String.class);
                ResponseEntity<String> googleResult = ResponseEntity.status(googleRes.getStatusCode()).body(googleRes.getBody());
                System.out.println("구글 탈퇴 결과값 :"+googleResult);

                /*--------------------------------------------------------------------------------------------------*/

                //맞을때
                if(googleResult.equals("200")){
                    userRepository.delete(delUser);
                    return("delete success");
                }

                //아니면 그냥 break..
                else{
                    return("delete fail");
                }

        }

        return "result";
    }
}
