package com.example.book.Controller;

import com.example.book.Domain.Book;
import com.example.book.Domain.Loan;
import com.example.book.Domain.Member;
import com.example.book.Service.BookService;
import com.example.book.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String index(){
        return "home";
    };

    @GetMapping("/books")
    public String listBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        System.out.println(books);
        return "books";
    }
    @GetMapping("/books/register")
    public String showRegisterForm() {
        return "register_book"; // Thymeleaf 템플릿 이름
    }

    @PostMapping("/books/register")
    public String registerBook(
            @RequestParam("isbn") String isbn,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("genre") String genre,
            Model model) {


        bookService.saveBook(isbn, title, author, publisher, genre);

        model.addAttribute("message", "Book registered successfully!");
        return "register_book";
    }
    // 대출 버튼을 눌렀을 때 실행되는 메소드
//    @PostMapping("/books/borrow")
//    public String borrowBook(@RequestParam("isbn") String isbn, Model model) {
//        System.out.println(isbn);
//        boolean success = bookService.borrowBook(isbn);
//        if (success) {
//            model.addAttribute("message", "도서 대출이 성공적으로 이루어졌습니다.");
//            return "/borrowed_book";
//        } else {
//            model.addAttribute("message", "해당 도서는 이미 대출되었습니다.");
//        }
//        return "redirect:/books"; // 대출 후 도서 리스트 페이지로 리다이렉트
//    }


    @PostMapping("/books/borrow")
    public String borrowBook(@RequestParam("isbn") String isbn, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUser"); // 세션에서 memberId 가져오기


        if (loggedInUser == null) {
            // 로그인하지 않은 경우, 로그인 페이지로 리다이렉트
            return "redirect:/member/login";
        }

        try {
            bookService.loanBook(loggedInUser, isbn); // 서비스에서 레퍼지토리 호출
            return "redirect:/books"; // 대출 성공 후 도서 목록 페이지로 리다이렉트
        } catch (Exception e) {
            e.printStackTrace();
            return "errorPage"; // 오류 발생 시 에러 페이지로 이동
        }
    }

    // 대출된 도서 리스트 페이지
    @GetMapping("/borrowed_book")
    public String listBorrowedBooks(Model model) {
        List<Book> borrowedBooks = bookService.getBorrowedALLBooks();
        System.out.println("Borrowed Books: " + borrowedBooks);  // 디버깅 로그 추가
        model.addAttribute("books", borrowedBooks);
        return "borrowed_book"; // borrowed_books.html 템플릿 렌더링
    }

    @PostMapping("/book/return")
    public String returnBook(@RequestParam("loanId") Integer loanId) {
        try {
            System.out.println("Controller" + loanId);
            bookService.returnBook(loanId);
            return "redirect:/borrowed_book";  // 반납 후 대출한 도서 리스트로 리다이렉트
        } catch (Exception e) {
            return "error";  // 에러 처리 페이지
        }
    }
    @GetMapping("/borrow")
    public String getBorrowedBooks(Model model,HttpSession session) {
        // 대출한 도서 리스트 가져오기
        String loggedInUser = (String) session.getAttribute("loggedInUser"); // 세션에서 memberId 가져오기
        List<Loan> borrowedBooks = bookService.getBorrowedBooks(loggedInUser);
        System.out.println(borrowedBooks);
        model.addAttribute("loans", borrowedBooks);  // books 리스트를 모델에 담아서 전달
        return "borrowed_book";  // borrowed-books.html로 뷰 반환
    }
}