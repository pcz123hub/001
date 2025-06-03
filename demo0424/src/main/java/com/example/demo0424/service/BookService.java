package com.example.demo0424.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo0424.entity.Book;

import java.util.List;

public interface BookService extends IService<Book> {
    List<Book> getAllBooks();
}

