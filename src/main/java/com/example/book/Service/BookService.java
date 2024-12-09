package com.example.book.Service;


import com.example.book.Domain.Book;
import com.example.book.Domain.Loan;
import com.example.book.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    public void saveBook(String isbn, String title, String author, String publisher, String genre) {
        bookRepository.save(isbn, title, author, publisher, genre);
    }
//    public Book getBookByIsbn(String isbn) {
//        return bookRepository.findByIsbn(isbn);
//    }
//
//    public void addBook(Book book) {
//        bookRepository.save(book);
//    }

//    public void deleteBook(String isbn) {
//        bookRepository.deleteByIsbn(isbn);
//    }
// 특정 도서를 대출 처리
public boolean borrowBook(String isbn) {
    Book book = bookRepository.findByIsbn(isbn);


    if (book != null && !book.isBorrowed()) {
        System.out.println("대출 상태업데이트");
        bookRepository.updateBook(book); // 대출 상태 업데이트

        return true;
    }
    return false;
}

    // 대출된 도서 리스트 반환
    public List<Book> getBorrowedALLBooks() {
        return bookRepository.findBorrowedBooks();
    }


    public void returnBook(Integer loanId) {
        // loanId를 기반으로 Loan 정보 조회

        String isbn = bookRepository.findIsbnByLoanId(loanId);
        System.out.println("test" + isbn);
        Book book = bookRepository.findByIsbn(isbn);
        // Books 테이블의 isBorrowed 값을 0으로 변경 (반납 처리)
        bookRepository.updateBookBorrowStatus(isbn);

        // Loan 테이블에서 해당 대출 기록 삭제
        bookRepository.deleteLoanById(loanId);
    }

    public void loanBook(String memberId, String isbn) {
        bookRepository.loanBook(memberId, isbn); // 레퍼지토리에서 프로시저 실행
    }

    // 대출한 도서 리스트 가져오기
    public List<Loan> getBorrowedBooks(String memberId) {
        // Loan 테이블에서 대출한 도서 리스트 가져오기
        List<Loan> isbnList = bookRepository.findLoansByMemberId(memberId);
        // ISBN에 해당하는 도서 리스트를 Books 테이블에서 가져오기
        return isbnList;
    }
}
