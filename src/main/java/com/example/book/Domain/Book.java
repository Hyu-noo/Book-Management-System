package com.example.book.Domain;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Book {
    @Id
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String genre;
    private boolean isBorrowed;
    private LocalDate registrationDate;

    public Book(String isbn, String title, String author, String publisher, String genre, boolean isBorrowed, LocalDate registrationDate) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
        this.isBorrowed = isBorrowed;
        this.registrationDate = registrationDate;
    }

    public Book() {

    }


    // Getters and Setters
    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public boolean isBorrowed() { return isBorrowed; }
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }


}
