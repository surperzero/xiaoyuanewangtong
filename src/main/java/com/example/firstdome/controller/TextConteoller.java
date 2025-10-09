package com.example.firstdome.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.firstdome.utils.FilesUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("text")
@RequiredArgsConstructor
public class TextConteoller {
    private final FilesUtils filesUtils;

    @PostMapping("/file")
    public SaResult testFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> map = filesUtils.upload(file);
        return SaResult.ok(map.get("fileName").toString());

    }
}
