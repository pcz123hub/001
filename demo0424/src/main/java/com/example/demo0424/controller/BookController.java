package com.example.demo0424.controller;

import com.example.demo0424.entity.Book;
import com.example.demo0424.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("book")
public class BookController {
    @Autowired
    private BookService bookService;

    @RequestMapping("list")
    public String findList(Model model, HttpSession httpSession) {

        List<Book> books = bookService.getAllBooks();

        model.addAttribute("students",books);

        return "book_list";
    }

    @RequestMapping("deleteBookById/{id}")
    public String deleteBookById(@PathVariable("id") Integer id ,Model model){

        bookService.removeById(id);

        List<Book> books = bookService.getAllBooks();

        model.addAttribute("students",books);

        return "book_list";
    }


    @RequestMapping("admin/manag")
    public String findManagList(Model model, HttpSession httpSession) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("students",books);

        return "book_manag";
    }

}
