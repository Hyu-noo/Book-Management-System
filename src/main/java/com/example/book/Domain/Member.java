package com.example.book.Domain;

import java.util.Date;

public class Member {
    private String memberId;        // 회원 ID
    private String memberPassword;  // 비밀번호
    private String memberName;      // 이름
    private String phoneNumber;     // 전화번호
    private String address;         // 주소
    private String membership;      // 회원 등급
    private Date joinDate;          // 가입일

    // 기본 생성자
    public Member() {};

    // 모든 필드를 받는 생성자
    public Member(String memberId, String memberPassword, String memberName,
                  String phoneNumber, String address, String membership, Date joinDate) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.membership = membership;
        this.joinDate = joinDate;
    }

    // Getter와 Setter 메서드
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
