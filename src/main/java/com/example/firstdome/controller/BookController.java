package com.example.firstdome.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.firstdome.entitys.Book;
import com.example.firstdome.mapper.BookMapper;
import com.example.firstdome.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("book")
public class BookController {
    private final BookService bookService;

    @PostMapping("savaBook")
    public SaResult savaBook(@RequestPart("file") MultipartFile[] files, @RequestPart Book book) {
        return bookService.savaBook(files,book);
    }

    @GetMapping("id")
    public SaResult selectBook(@RequestParam Long id){
        return bookService.selectBook(id);
    }

    @PostMapping("savaBookContent")
    public SaResult savaBookContent(@RequestPart("file")MultipartFile file,@RequestPart Long id) {
        return bookService.savaBookContent(file,id);
    }
    @PostMapping("updateBook")
    public SaResult updateBook(@RequestPart(required = false) MultipartFile[] files,@RequestPart Book book){
        return bookService.updateBook(files,book);
    }
    @PostMapping("/updateBookContent")
    public SaResult updateBookContent(@RequestPart(required = false) MultipartFile[] file,@RequestPart Long bookId,@RequestPart Book book){
        return bookService.updateBookContent(file,bookId);
    }
    @GetMapping("deleteBook")
    public SaResult deleteBook(@RequestParam Long id){
        return bookService.deleteBook(id);
    }

    /**
     *
     * 更具书籍id查询内容
     * @param id
     * @return
     */
    @GetMapping("fileByBookId")
    public SaResult fileByBookId(@RequestParam Long id){
        return bookService.fileByBookId(id);
    }
    /**
     * 分页 分两步
     */
    /**
     * 分页查询书籍
     * @param current 当前页码，默认为1
     * @param size 每页大小，默认为10
     * @param searchMap 搜索条件
     * @return 分页结果
     */
    @GetMapping("page")
    public SaResult page(
        @RequestParam(defaultValue = "1") Integer current,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam Map<String, Object> searchMap
    ) {
        return bookService.selectPage(current, size, searchMap);
    }

    /**
     * 获取所有书籍（不分页）
     * @return 所有书籍列表
     */
    @GetMapping("list")
    public SaResult list() {
        return bookService.selectList();
    }
}
