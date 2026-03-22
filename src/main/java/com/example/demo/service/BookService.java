package com.example.demo.service;

import com.example.demo.model.Book;
import org.springframework.stereotype.Service;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

@Service
public class BookService {
    private final Map<Long, Book> books = new HashMap<>();
    private long nextId = 1;
    private static final String LOG_PATH = "c:\\CODE PROJECT 3\\JAVA\\.cursor\\debug.log";

    public BookService() {
        // #region agent log
        try {
            FileWriter fw = new FileWriter(LOG_PATH, true);
            fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"D\",\"location\":\"BookService.java:15\",\"message\":\"BookService constructor entry\",\"data\":{},\"timestamp\":" + Instant.now().toEpochMilli() + "}\n");
            fw.close();
        } catch (IOException e) {}
        // #endregion
        
        // Initialize with sample data
        books.put(1L, new Book(1L, "Java Programming", "Nguyễn Văn A"));
        books.put(2L, new Book(2L, "Spring Boot Guide", "Trần Văn B"));
        books.put(3L, new Book(3L, "REST API Design", "Lê Văn C"));
        books.put(4L, new Book(4L, "Clean Code", "Robert C. Martin"));
        books.put(5L, new Book(5L, "The Pragmatic Programmer", "David Thomas"));
        books.put(6L, new Book(6L, "Design Patterns", "Gang of Four"));
        books.put(7L, new Book(7L, "Effective Java", "Joshua Bloch"));
        books.put(8L, new Book(8L, "Java Concurrency in Practice", "Brian Goetz"));
        books.put(9L, new Book(9L, "Spring in Action", "Craig Walls"));
        books.put(10L, new Book(10L, "Microservices Patterns", "Chris Richardson"));
        nextId = 11;
        
        // #region agent log
        try {
            FileWriter fw = new FileWriter(LOG_PATH, true);
            fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"D\",\"location\":\"BookService.java:30\",\"message\":\"BookService constructor exit\",\"data\":{\"booksSize\":" + books.size() + "},\"timestamp\":" + Instant.now().toEpochMilli() + "}\n");
            fw.close();
        } catch (IOException e) {}
        // #endregion
    }

    // Get all books
    public List<Book> getAllBooks() {
        // #region agent log
        try {
            FileWriter fw = new FileWriter(LOG_PATH, true);
            fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"C\",\"location\":\"BookService.java:36\",\"message\":\"getAllBooks() entry\",\"data\":{\"booksMapSize\":" + books.size() + "},\"timestamp\":" + Instant.now().toEpochMilli() + "}\n");
            fw.close();
        } catch (IOException e) {}
        // #endregion
        
        List<Book> result = new ArrayList<>(books.values());
        
        // #region agent log
        try {
            FileWriter fw = new FileWriter(LOG_PATH, true);
            fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"C\",\"location\":\"BookService.java:43\",\"message\":\"getAllBooks() exit\",\"data\":{\"resultSize\":" + result.size() + ",\"resultNull\":\"" + (result == null) + "\"},\"timestamp\":" + Instant.now().toEpochMilli() + "}\n");
            fw.close();
        } catch (IOException e) {}
        // #endregion
        
        return result;
    }

    // Get book by ID
    public Book getBookById(Long id) {
        return books.get(id);
    }

    // Add new book
    public Book addBook(Book book) {
        book.setId(nextId);
        books.put(nextId, book);
        nextId++;
        return book;
    }

    // Update book
    public Book updateBook(Long id, Book book) {
        if (books.containsKey(id)) {
            book.setId(id);
            books.put(id, book);
            return book;
        }
        return null;
    }

    // Delete book
    public boolean deleteBook(Long id) {
        return books.remove(id) != null;
    }
}
