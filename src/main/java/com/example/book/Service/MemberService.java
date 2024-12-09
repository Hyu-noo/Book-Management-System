package com.example.book.Service;

import com.example.book.Domain.Member;
import com.example.book.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원가입
    public void register(Member member) {
        // ID 중복 체크
        if (memberRepository.findByMemberId(member.getMemberId()) != null) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 회원 정보 저장
        memberRepository.registerMember(member);
    }
    // 로그인
    public Member login(String memberId, String memberPassword) {
        return memberRepository.loginMember(memberId, memberPassword);
    }

    public Member findMemberById(String loggedInUser){
        System.out.println("service"+ loggedInUser);
        return memberRepository.findByMemberId1(loggedInUser);
    }
    @Transactional
    public void deleteMember(String memberId) {
        // 탈퇴 로직 호출
        memberRepository.removeMember(memberId);
    }
}
