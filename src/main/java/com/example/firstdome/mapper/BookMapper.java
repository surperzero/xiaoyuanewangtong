package com.example.firstdome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.firstdome.entitys.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
