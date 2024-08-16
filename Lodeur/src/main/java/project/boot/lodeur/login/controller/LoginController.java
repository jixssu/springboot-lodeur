//package project.boot.project.boot.lodeur.login.controller;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import project.boot.project.boot.lodeur.dto.request.auth.LogInRequestDTO;
//import project.boot.project.boot.lodeur.dto.response.auth.LogInResponseDTO;
//import project.boot.project.boot.lodeur.login.service.LoginService;
//import project.boot.project.boot.lodeur.member.entity.MemberEntity;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//
//@Controller
//public class LoginController {
//
//    private final LoginService loginService;
//
//    @Autowired
//    public LoginController(LoginService loginService) {
//        this.loginService = loginService;
//    }
//
//    @GetMapping("/log-in")
//    public String loginForm(Model model) {
//        model.addAttribute("memberEntity", new MemberEntity());
//        return "login";
//    }
//
//    @PostMapping("/log-in")
//    public ResponseEntity<? super LogInResponseDTO> logIn(
//            @RequestBody @Valid LogInRequestDTO requestBody, HttpServletResponse response) {
//        ResponseEntity<? super LogInResponseDTO> responseEntity = loginService.logIn(requestBody, response);
//        return responseEntity;
//    }
//
//    @GetMapping("/log-out")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return "redirect:/index";
//    }
//    
//    @GetMapping("/log-in/oauth/kakao")
//    public String loginKakao(OAuth2AuthenticationToken authentication, Model model) {
//        OAuth2User oAuth2User = authentication.getPrincipal();
//        model.addAttribute("name", oAuth2User.getAttribute("properties.nickname"));
//        return "redirect:/index";
//    }
//    
//    @GetMapping("/login/oauth2/code/naver")
//    public String loginNaver(OAuth2AuthenticationToken authentication) {
//        OAuth2User oAuth2User = authentication.getPrincipal();
//        String naverId = (String) ((Map<String, Object>) oAuth2User.getAttribute("response")).get("id");
//        // JWT 토큰 생성 및 쿠키 저장은 CustomOAuth2UserService에서 처리
//        return "redirect:/index";
//    }
//}
package project.boot.lodeur.login.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import project.boot.lodeur.dto.request.auth.LogInRequestDTO;
import project.boot.lodeur.dto.request.auth.SignUpRequestDTO;
import project.boot.lodeur.dto.response.auth.LogInResponseDTO;
import project.boot.lodeur.login.service.LoginService;
import project.boot.lodeur.member.entity.MemberEntity;
import project.boot.lodeur.member.repository.MemberRepository;
import project.boot.lodeur.member.service.MemberService;
import project.boot.lodeur.provider.JwtProvider;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public LoginController(LoginService loginService, MemberService memberService, MemberRepository memberRepository,JwtProvider jwtProvider) {
        this.loginService = loginService;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/log-in")
    public String loginForm(Model model) {
        model.addAttribute("memberEntity", new MemberEntity());
        return "login";
    }

    @PostMapping("/log-in")
    public ResponseEntity<? super LogInResponseDTO> logIn(
            @RequestBody @Valid LogInRequestDTO requestBody, HttpServletResponse response) {
        ResponseEntity<? super LogInResponseDTO> responseEntity = loginService.logIn(requestBody, response);
        return responseEntity;
    }

    @GetMapping("/log-out")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/index";
    }

    @GetMapping("/log-in/oauth/kakao")
    public String loginKakao(OAuth2AuthenticationToken authentication, Model model) {
        OAuth2User oAuth2User = authentication.getPrincipal();
        model.addAttribute("name", oAuth2User.getAttribute("properties.nickname"));
        return "redirect:/index";
    }

//    @GetMapping("/login/oauth2/code/naver")
//    public String loginNaver(OAuth2AuthenticationToken authentication) {
//        OAuth2User oAuth2User = authentication.getPrincipal();
//        String naverId = (String) ((Map<String, Object>) oAuth2User.getAttribute("response")).get("id");
//        String email = (String) ((Map<String, Object>) oAuth2User.getAttribute("response")).get("email");
//        String phone = (String) ((Map<String, Object>) oAuth2User.getAttribute("response")).get("mobile");
//
//        Optional<MemberEntity> existingMember = memberRepository.findByMemberEmail(email);
//        if (existingMember.isPresent()) {
//            return "redirect:/";
//        } else {
//            String redirectUrl = UriComponentsBuilder.fromPath("/member/additional-info")
//                    .queryParam("memberId", "naver_" + naverId)
//                    .queryParam("memberEmail", email)
//                    .queryParam("memberPhone", phone)
//                    .toUriString();
//            return "redirect:" + redirectUrl;
//        }
//    }
    @GetMapping("/login/oauth2/code/naver")
    public String loginNaver(OAuth2AuthenticationToken authentication, HttpServletResponse response) {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String naverId = (String) ((Map<String, Object>) oAuth2User.getAttribute("response")).get("id");
        String email = (String) ((Map<String, Object>) oAuth2User.getAttribute("response")).get("email");
        String phone = (String) ((Map<String, Object>) oAuth2User.getAttribute("response")).get("mobile");

        Optional<MemberEntity> existingMember = memberRepository.findByMemberEmail(email);
        if (existingMember.isPresent()) {
            // JWT 토큰 생성
            String jwtToken = jwtProvider.create(existingMember.get().getId(), existingMember.get().getMemberId(), existingMember.get().getMemberAuth());

            // JWT 토큰을 쿠키에 설정
            Cookie jwtCookie = new Cookie("jwtToken", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            System.out.println("JWT Token created and added to cookie: " + jwtToken);
            
            return "redirect:/";
        } else {
            String redirectUrl = UriComponentsBuilder.fromPath("/member/additional-info")
                    .queryParam("memberId", "naver_" + naverId)
                    .queryParam("memberEmail", email)
                    .queryParam("memberPhone", phone)
                    .toUriString();
            return "redirect:" + redirectUrl;
        }
    }


    @GetMapping("/member/additional-info")
    public String showAdditionalInfoForm(@RequestParam("memberId") String memberId,
                                         @RequestParam("memberEmail") String memberEmail,
                                         @RequestParam("memberPhone") String memberPhone,
                                         Model model) {
        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setMemberId(memberId);
        signUpRequestDTO.setMemberEmail(memberEmail);
        signUpRequestDTO.setMemberPhone(memberPhone);
        model.addAttribute("member", signUpRequestDTO);
        return "member/additional-info";
    }

//    @PostMapping("/member/additional-info")
//    public ResponseEntity<?> handleAdditionalInfo(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
//        try {
//            Optional<MemberEntity> memberOptional = memberRepository.findByMemberId(signUpRequestDTO.getMemberId());
//            if (memberOptional.isPresent()) {
//                MemberEntity member = memberOptional.get();
//                member.setMemberName(signUpRequestDTO.getMemberName());
//                member.setMemberAddress(signUpRequestDTO.getMemberAddress());
//                memberService.updateMember(member);
//                return ResponseEntity.ok(Collections.singletonMap("code", "SU"));
//            } else {
//                return ResponseEntity.status(404).body(Collections.singletonMap("code", "NF")); // Not Found
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Collections.singletonMap("code", "FA"));
//        }
//    }
}
