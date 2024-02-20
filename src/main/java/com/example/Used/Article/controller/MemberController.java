package com.example.Used.Article.controller;
import org.springframework.ui.Model;

import com.example.Used.Article.domain.Member;
import com.example.Used.Article.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    //회원가입
    @GetMapping("/signup")
    public String signup(MemberForm memberCreateForm) {
        return "signupForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signupForm";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signupForm";
        }

        try {
            memberService.createMember(memberCreateForm.getUsername(), memberCreateForm.getEmail(),memberCreateForm.getAddress(), memberCreateForm.getPassword1());

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signupForm";

        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signupForm";
        }

        return "redirect:/";
    }

    //로그인
    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }


    //사용자 정보 페이지
    @GetMapping("/profile")
    public String myPage(Model model, Principal principal) {
        String username = principal.getName();
        Member member = memberService.getMemberByUsername(username);
        model.addAttribute("member", member);
        return "myPageForm";
    }

}
