package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookWebController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("mode", "create");
        return "books/form";
    }

    @PostMapping
    public String create(@ModelAttribute Book book) {
        bookService.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        model.addAttribute("mode", "edit");
        return "books/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Book book) {
        bookService.updateBook(id, book);
        return "redirect:/books";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
