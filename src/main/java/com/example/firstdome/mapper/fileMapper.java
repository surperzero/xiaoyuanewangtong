package com.example.firstdome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.firstdome.entitys.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface fileMapper extends BaseMapper<File> {
}
