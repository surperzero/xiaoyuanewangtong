package com.example.firstdome.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.firstdome.entitys.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional
public interface BookService extends IService<Book> {

    SaResult savaBook(MultipartFile[] files, Book book);

    SaResult selectBook(Long id);

    SaResult savaBookContent(MultipartFile file, Long id);

    SaResult updateBook(MultipartFile[] files, Book book);

    SaResult updateBookContent(MultipartFile[] file, Long bookId);

    SaResult deleteBook(Long id);

    SaResult fileByBookId(Long id);

    /**
     * 分页查询书籍
     * @param current 当前页码
     * @param size 每页大小
     * @param searchMap 搜索条件
     * @return 分页结果
     */
    SaResult selectPage(Integer current, Integer size, java.util.Map<String, Object> searchMap);

    /**
     * 获取所有书籍列表
     * @return 书籍列表
     */
    SaResult selectList();
}
