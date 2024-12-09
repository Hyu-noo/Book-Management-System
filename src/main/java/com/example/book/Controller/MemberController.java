package com.example.book.Controller;

import com.example.book.Domain.Member;
import com.example.book.Service.MemberService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupForm() {
        return "signup";  // signup.html 페이지를 반환
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Member member) {
        System.out.println("회원가입 정보: " + member);


        // 회원 등록 서비스 호출 (회원 등록 로직)
        memberService.register(member);

        // 회원가입 후 로그인 페이지로 리다이렉트
        return "redirect:/member/login";
    }
    @GetMapping("/info")
    public String memberInfo(HttpSession session, Model model){
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        System.out.println("Session LoginMember : "  + loggedInUser);
        Member member = memberService.findMemberById(loggedInUser);
        model.addAttribute("member",member);
        return "member";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "login";  // login.html 페이지를 반환
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String memberId, @RequestParam String memberPassword, Model model, HttpServletRequest request) {
        Member member = memberService.login(memberId, memberPassword);
        if (member != null) {
            model.addAttribute("message", "로그인 성공! " + ((Member) member).getMemberName() + "님 환영합니다.");
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser",memberId);
            return "home";  // 로그인 성공 후 홈 페이지로 이동 (home.html을 반환)
        } else {
            model.addAttribute("message", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "login";  // 로그인 실패 시 다시 로그인 페이지로
        }
    }

    @PostMapping("/delete")
    public String deleteMember(@RequestParam("memberId") String memberId) {
        try {
            // 회원 탈퇴 서비스 호출
            memberService.deleteMember(memberId);
        } catch (Exception e) {
            // 예외 처리: 탈퇴 실패 시 에러 메시지 출력 (필요 시 수정)
            return "errorPage"; // 에러 페이지로 리다이렉트 (디자인 필요)
        }

        // 탈퇴 후 로그인 페이지로 리다이렉트
        return "redirect:/member/login";
    }
}
