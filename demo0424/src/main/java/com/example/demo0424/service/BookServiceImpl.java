package com.example.demo0424.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo0424.dao.BookMapper;
import com.example.demo0424.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService{
    @Autowired
    BookMapper bookMapper;

    @Override
    public List<Book> getAllBooks() {

        List<Book> books = bookMapper.selectList(null);

        return books;
    }
}
