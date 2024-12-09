package com.example.book.Repository;

import com.example.book.Domain.Book;
import com.example.book.Domain.Loan;

import java.util.List;

public interface BookRepository {
    public List<Book> findAll();

    public void loanBook(String memberId, String isbn);
    public void save(String isbn, String title, String author, String publisher, String genre);
    public Book findByIsbn(String isbn);
    public void updateBook(Book book);
    List<Book> findBorrowedBooks(); // 대출한 도서 조회
    public void deleteLoanById(Integer loanId);
    // 회원 ID로 대출한 도서의 ISBN 리스트 조회
    List<Loan> findLoansByMemberId(String memberId);
    public String findIsbnByLoanId(Integer loanId);
    public void updateBookBorrowStatus(String isbn);
}
