package com.wipro.bookservice.service;

import com.wipro.bookservice.dto.NotificationRequest;
import com.wipro.bookservice.entity.Book;
import com.wipro.bookservice.repository.BookRepository;
import com.wipro.bookservice.exception.BookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final String notificationServiceUrl = "http://localhost:8085/notifications";

    public BookService(BookRepository bookRepository, RestTemplate restTemplate) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
    }

    private void sendNotification(String message) {
        try {
            restTemplate.postForObject(notificationServiceUrl, new NotificationRequest(message), Void.class);
        } catch (Exception e) {
            // Log the error, but don't fail the main operation
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }

    public Book addBook(Book book) {
        Book savedBook = bookRepository.save(book);
        sendNotification("Book added: " + book.getTitle());
        return savedBook;
    }

    public List<Book> getAllBooks(String title, String author, String category) {
        return bookRepository.findByTitleAuthorCategory(title, author, category);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setPublisher(updatedBook.getPublisher());
        existingBook.setPublishedYear(updatedBook.getPublishedYear());
        Book savedBook = bookRepository.save(existingBook);
        sendNotification("Book updated: " + existingBook.getTitle());
        return savedBook;
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
        sendNotification("Book deleted: " + book.getTitle());
    }
}