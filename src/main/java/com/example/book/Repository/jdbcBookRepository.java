package com.example.book.Repository;


import com.example.book.Domain.Book;
import com.example.book.Domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class jdbcBookRepository implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public jdbcBookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> findAll() {
        String sql = "SELECT * FROM Books";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Book book = new Book();
            book.setIsbn(rs.getString("ISBN"));
            book.setTitle(rs.getString("Title"));
            book.setAuthor(rs.getString("Author"));
            book.setPublisher(rs.getString("Publisher"));
            book.setGenre(rs.getString("Genre"));
            book.setBorrowed(rs.getInt("IsBorrowed") == 1);
            java.sql.Date registrationDate = rs.getDate("RegistrationDate");
            if (registrationDate != null) {
                book.setRegistrationDate(registrationDate.toLocalDate());
            } else {
                // RegistrationDate가 null일 때 null 또는 기본값을 설정
                book.setRegistrationDate(null);  // 예: null로 설정
            }


            //book.setRegistrationDate(rs.getDate("RegistrationDate").toLocalDate());
            return book;
        });
    }


    @Override
    public void save(String isbn, String title, String author, String publisher, String genre) {
        String sql = "INSERT INTO Books (ISBN, Title, Author, Publisher, Genre, IsBorrowed) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, isbn, title, author, publisher, genre, false);
    }

    // ISBN으로 도서 찾기
    @Override
    public Book findByIsbn(String isbn) {
        String sql = "SELECT * FROM Books WHERE ISBN = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{isbn}, (rs, rowNum) -> {
            Book book = new Book();
            book.setIsbn(rs.getString("ISBN"));
            book.setTitle(rs.getString("Title"));
            book.setAuthor(rs.getString("Author"));
            book.setPublisher(rs.getString("Publisher"));
            book.setGenre(rs.getString("Genre"));
            book.setBorrowed(rs.getBoolean("IsBorrowed"));
            book.setRegistrationDate(rs.getDate("RegistrationDate").toLocalDate());
            return book;
        });
    }

    // 도서 대출 상태 업데이트
    @Override
    public void updateBook(Book book) {
        String sql = "UPDATE Books SET IsBorrowed = ? WHERE ISBN = ?";
        jdbcTemplate.update(sql, book.isBorrowed() ? 1 : 0, book.getIsbn());
    }
    @Override
    public List<Book> findBorrowedBooks() {
        String sql = "SELECT * FROM Books WHERE IsBorrowed = 1";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Book book = new Book();
            book.setIsbn(rs.getString("ISBN"));
            book.setTitle(rs.getString("Title"));
            book.setAuthor(rs.getString("Author"));
            book.setPublisher(rs.getString("Publisher"));
            book.setGenre(rs.getString("Genre"));
            book.setBorrowed(rs.getBoolean("IsBorrowed"));
            book.setRegistrationDate(rs.getDate("RegistrationDate") != null
                    ? rs.getDate("RegistrationDate").toLocalDate() : null);
            return book;
        });
    }
        @Override
    public void loanBook(String memberId, String isbn) {
        String sql = "{call LoanBook(?, ?)}";  // 저장 프로시저 호출

        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            // 프로시저의 파라미터 설정
            stmt.setString(1, memberId); // 회원 ID
            stmt.setString(2, isbn); // 도서 ISBN

            // 프로시저 실행
            stmt.execute();
        } catch (SQLException e) {
            // 예외 처리
            e.printStackTrace();
            throw new DataAccessException("프로시저 실행 중 오류 발생", e) {};
        }
    }


    // 회원 ID로 대출한 도서의 ISBN 리스트 조회
    @Override
    public List<Loan> findLoansByMemberId(String memberId) {
        String sql = "SELECT * FROM Loan WHERE memberId = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            Loan loan = new Loan();
            loan.setLoanId(rs.getInt("loan_Id"));
            loan.setMemberId(rs.getString("memberId"));
            loan.setIsbn(rs.getString("isbn"));
            loan.setLoandate(rs.getDate("loanDate").toLocalDate());  // Date -> LocalDate로 변환
            loan.setReturndeudate(rs.getDate("returnDueDate").toLocalDate());  // Date -> LocalDate로 변환
            loan.setReturnDate(rs.getDate("returnDate") != null ? rs.getDate("returnDate").toLocalDate() : null);  // 반납일 null 체크
            loan.setOverdueStatus(rs.getInt("overdueStatus"));
            return loan;
        });
    }

    // loanId로 대출 기록 삭제
    public void deleteLoanById(Integer loanId) {
        String sql = "DELETE FROM Loan WHERE loan_Id = ?";
        jdbcTemplate.update(sql, loanId);
    }
    public String findIsbnByLoanId(Integer loanId) {
        String sql = "SELECT isbn FROM Loan WHERE loan_Id = ?";

        // query 메서드를 사용하여 List로 결과를 반환
        List<String> isbnList = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("isbn"),  // ResultSet에서 ISBN을 추출
                loanId  // 파라미터로 loanId 전달
        );

        // 결과가 없으면 null 반환, 아니면 첫 번째 결과 반환
        return (isbnList.isEmpty()) ? null : isbnList.get(0);
    }
    public void updateBookBorrowStatus(String isbn) {
        String sql = "UPDATE Books SET isBorrowed = 0 WHERE isbn = ?";
        jdbcTemplate.update(sql, isbn);
    }
}