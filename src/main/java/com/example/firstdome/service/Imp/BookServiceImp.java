package com.example.firstdome.service.Imp;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.firstdome.entitys.Book;
import com.example.firstdome.entitys.File;
import com.example.firstdome.entitys.User;
import com.example.firstdome.entitys.properties.FileProperties;
import com.example.firstdome.mapper.BookMapper;
import com.example.firstdome.mapper.UserMapper;
import com.example.firstdome.mapper.fileMapper;
import com.example.firstdome.service.BookService;
import com.example.firstdome.utils.FilesUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookServiceImp extends ServiceImpl<BookMapper, Book> implements BookService {
    private final FilesUtils filesUtils;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    private final FileProperties fileProperties;
    private final fileMapper fileMapper;
    /**
     * 保存书籍
     * @param files
     * @param book
     * @return
     */
    @Override
    public SaResult savaBook(MultipartFile[] files, Book book) {
        ArrayList<String> filePath=new ArrayList<>();
        for (MultipartFile file : files) {
            Map<String, Object> upload = filesUtils.upload(file);
            if (upload.get("filePath") == null) {return SaResult.error("请传递.jpg,.jpeg,.png格式的图片");}
            filePath.add(upload.get("filePath").toString());
        }
        book.setCoverImage(filePath.get(0));
        book.setBackImage(filePath.get(1));
        //保存当前用户
        String loginPhone = StpUtil.getLoginId().toString();
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,loginPhone);
        User user = userMapper.selectOne(wrapper);
        if (user==null) return SaResult.error("未查询出当前用户");
        book.setSubmitUser(user.getId());
        return bookMapper.insert(book)>0?SaResult.ok("保存成功"):SaResult.error("保存失败");
    }

    @Override
    public SaResult selectBook(Long id) {
        Book book = bookMapper.selectById(id);
        if (book==null) return SaResult.error("未查询出书籍信息");
        book.setCoverImage(book.getCoverImage().replace(fileProperties.getImgAddress(), fileProperties.getImgHttpAddress()));
        book.setBackImage(book.getBackImage().replace(fileProperties.getImgAddress(), fileProperties.getImgHttpAddress()));
        //todo 书籍内容填充
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getBookId, id);
        File file = fileMapper.selectOne(queryWrapper);
        if (file!=null) book.setFileName(file.getPathFileName().replace(fileProperties.getPdfAddress(),fileProperties.getPdfHttpAddress()));
        return SaResult.data(book);
    }

    @Override
    public SaResult savaBookContent(MultipartFile file, Long id) {
        Book book = bookMapper.selectById(id);
        if (book==null) return SaResult.error("未有已保存的书籍信息");
        String substring = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!".pdf".equals(substring)) return SaResult.error("请传递pdf格式的书籍内容文件");
        Map<String, Object> upload = filesUtils.upload(file);
        Object filePath = upload.get("filePath");
        File build = File.builder().bookId(id).type(0).pathFileName(filePath.toString()).build();
        return fileMapper.insert(build)>0?SaResult.ok("保存成功"):SaResult.error("保存失败");
    }

    @Override
    public SaResult updateBook(MultipartFile[] files, Book book) {
        Book book1 = bookMapper.selectById(book.getId());
        if(book1==null) return  SaResult.error("未查询出要修改的书籍信息");
        if (files!=null&&files[0]!=null){
            Map<String, Object> upload = filesUtils.upload(files[0]);
            if (upload.get("filePath") == null) return SaResult.error("未按照规定格式传递文件");
            book.setCoverImage(upload.get("filePath").toString());
        }
        if (files!=null&&files[1]!=null){
            Map<String, Object> upload = filesUtils.upload(files[0]);
            if (upload.get("filePath") == null) return SaResult.error("未按照规定格式传递文件");
            book.setBackImage(upload.get("filePath").toString());
        }
        return bookMapper.updateById(book)>0?SaResult.ok("修改成功"):SaResult.error("修改失败");
    }

    @Override
    public SaResult updateBookContent(MultipartFile[] file, Long bookId) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        File file1 = fileMapper.selectOne(wrapper);
        if (file1 == null) return SaResult.error("为查询出书记内容信息");
        Map<String, Object> upload = filesUtils.upload(file[0]);
        if (upload.get("filepath") == null) return SaResult.error("传递格式为pdf");
        File filepath = File.builder().bookId(bookId).pathFileName(upload.get("filePath").toString()).build();
        UpdateWrapper<File> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("bookId", bookId).set("path_file_name",filepath);
        return fileMapper.update(file1,updateWrapper)>0?SaResult.ok("修改成功"):SaResult.error("修改错误");
    }

    @Override
    public SaResult deleteBook(Long id) {
        Book book = bookMapper.selectById(id);
        if(book==null) return SaResult.error("未找到需要删除的书籍信息");
        String coverImage = book.getCoverImage();
        String backImage = book.getBackImage();
        filesUtils.deleteFile(coverImage);
        filesUtils.deleteFile(backImage);
        //查询是否有上传书籍内容信息
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getBookId,book.getId());
        File file = fileMapper.selectOne(queryWrapper);
        if(file!=null){
            filesUtils.deleteFile(file.getPathFileName());
            fileMapper.delete(queryWrapper);
        }
        bookMapper.deleteById(id);
        return SaResult.ok("删除成功");
    }

    @Override
    public SaResult fileByBookId(Long id) {
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getBookId,id);
        File file = fileMapper.selectOne(queryWrapper);

        return SaResult.data(file);
    }

    /**
     * 分页查询书籍
     * @param current 当前页码
     * @param size 每页大小
     * @param searchMap 搜索条件
     * @return 分页结果
     */
    @Override
    public SaResult selectPage(Integer current, Integer size, Map<String, Object> searchMap) {
        // 创建分页对象
        Page<Book> page = new Page<>(current, size);
        
        // 构建查询条件
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        
        // 根据标题模糊查询
        if (searchMap.get("title") != null && !"".equals(searchMap.get("title").toString().trim())) {
            wrapper.like(Book::getTitle, searchMap.get("title"));
        }
        
        // 根据作者模糊查询
        if (searchMap.get("author") != null && !"".equals(searchMap.get("author").toString().trim())) {
            wrapper.like(Book::getAuthor, searchMap.get("author"));
        }
        
        // 根据分类查询
        if (searchMap.get("type") != null && !"".equals(searchMap.get("type").toString().trim())) {
            wrapper.eq(Book::getType, searchMap.get("type"));
        }
        
        // 根据提交时间范围查询
        if (searchMap.get("startTime") != null && !"".equals(searchMap.get("startTime").toString().trim())) {
            wrapper.ge(Book::getSubmitTime, searchMap.get("startTime"));
        }
        if (searchMap.get("endTime") != null && !"".equals(searchMap.get("endTime").toString().trim())) {
            wrapper.le(Book::getSubmitTime, searchMap.get("endTime"));
        }
        
        // 根据发布日期范围查询
        if (searchMap.get("startDate") != null && !"".equals(searchMap.get("startDate").toString().trim())) {
            wrapper.ge(Book::getDate, searchMap.get("startDate"));
        }
        if (searchMap.get("endDate") != null && !"".equals(searchMap.get("endDate").toString().trim())) {
            wrapper.le(Book::getDate, searchMap.get("endDate"));
        }
        
        // 按提交时间倒序排列
        wrapper.orderByDesc(Book::getSubmitTime);
        
        // 执行分页查询
        Page<Book> bookPage = bookMapper.selectPage(page, wrapper);
        
        // 处理返回数据，转换图片路径为HTTP访问路径
        for (Book book : bookPage.getRecords()) {
            if (book.getCoverImage() != null) {
                book.setCoverImage(book.getCoverImage().replace(fileProperties.getImgAddress(), fileProperties.getImgHttpAddress()));
            }
            if (book.getBackImage() != null) {
                book.setBackImage(book.getBackImage().replace(fileProperties.getImgAddress(), fileProperties.getImgHttpAddress()));
            }
            
            // 查询并设置书籍内容文件路径
            LambdaQueryWrapper<File> fileWrapper = new LambdaQueryWrapper<>();
            fileWrapper.eq(File::getBookId, book.getId());
            File file = fileMapper.selectOne(fileWrapper);
            if (file != null) {
                book.setFileName(file.getPathFileName().replace(fileProperties.getPdfAddress(), fileProperties.getPdfHttpAddress()));
            }
        }
        
        return SaResult.data(bookPage);
    }

    /**
     * 获取所有书籍列表（不分页）
     * @return 书籍列表
     */
    @Override
    public SaResult selectList() {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Book::getSubmitTime);
        
        List<Book> bookList = bookMapper.selectList(wrapper);
        
        // 处理返回数据，转换图片路径为HTTP访问路径
        for (Book book : bookList) {
            if (book.getCoverImage() != null) {
                book.setCoverImage(book.getCoverImage().replace(fileProperties.getImgAddress(), fileProperties.getImgHttpAddress()));
            }
            if (book.getBackImage() != null) {
                book.setBackImage(book.getBackImage().replace(fileProperties.getImgAddress(), fileProperties.getImgHttpAddress()));
            }
            
            // 查询并设置书籍内容文件路径
            LambdaQueryWrapper<File> fileWrapper = new LambdaQueryWrapper<>();
            fileWrapper.eq(File::getBookId, book.getId());
            File file = fileMapper.selectOne(fileWrapper);
            if (file != null) {
                book.setFileName(file.getPathFileName().replace(fileProperties.getPdfAddress(), fileProperties.getPdfHttpAddress()));
            }
        }
        
        return SaResult.data(bookList);
    }
}
