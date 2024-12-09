package com.example.book.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Loan {
    @Id
    private Integer loanId;  // 대출 ID
    private String memberId;  // 대출한 회원의 ID
    private String isbn;  // 대출한 도서의 ISBN

    private LocalDate loandate;
    private LocalDate returndeudate;

    public LocalDate getLoandate() {
        return loandate;
    }

    public void setLoandate(LocalDate loandate) {
        this.loandate = loandate;
    }

    public LocalDate getReturndeudate() {
        return returndeudate;
    }

    public void setReturndeudate(LocalDate returndeudate) {
        this.returndeudate = returndeudate;
    }

    private LocalDate returnDate;  // 반납일
    private Integer overdueStatus;  // 연체 상태 (0: 연체 아님, 1: 연체)

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(Integer overdueStatus) {
        this.overdueStatus = overdueStatus;
    }
}
