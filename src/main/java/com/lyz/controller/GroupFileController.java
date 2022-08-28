package com.lyz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.common.lang.Result;
import com.lyz.entity.Group;
import com.lyz.entity.GroupFile;
import com.lyz.entity.Pdf;
import com.lyz.entity.User;
import com.lyz.service.GroupFileService;
import com.lyz.service.GroupService;
import com.lyz.service.UserService;
import com.lyz.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyz
 * @since 2022-02-03
 */
@RestController
@RequestMapping("/group-file")
public class GroupFileController {

    @Autowired
    GroupFileService groupFileService;
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;

    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostMapping("upload")
    public Result uploadDocument(MultipartFile file, HttpServletRequest req) {
        String originName = file.getOriginalFilename();
        String suffix;
        if(originName.endsWith(".pdf")){
            suffix = ".pdf";
        }else if(originName.endsWith(".doc")){
            suffix = ".doc";
        }else{
            suffix = ".docx";
        }
        String format = sdf.format(new Date());
//        String realPath = "/Users/liyizhou/groupFileStore" + format;
        String realPath = "/usr/local/groupFileStore" + format;
        File folder = new File(realPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String newName = UUID.randomUUID().toString() + suffix;

        try{
            file.transferTo(new File(folder, newName));
            String url = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+format+newName;
            GroupFile newFile = new GroupFile();
            newFile.setFileName(originName);
            String currentDate = sdf1.format(new Date());
            newFile.setAddedDate(Timestamp.valueOf(currentDate));
            newFile.setFilePath(url);
            groupFileService.save(newFile);
            return Result.succ(MapUtil.builder()
                    .put("id", newFile.getId())
                    .put("fileName", newFile.getFileName())
                    .put("addedDate", new Date())
                    .put("filePath", url)
                    .map());
        } catch (IOException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("getFileInfo")
    public Result getFileInfo(@RequestHeader("X-Token") String token, int fileId, int groupId) {
        int userId = Integer.parseInt(jwtUtils.getClaimByToken(token).getSubject());
        User user = userService.getById(userId);
        GroupFile groupFile = groupFileService.getOne(new QueryWrapper<GroupFile>().eq("id", fileId));
        groupFile.setGroupId(groupId);
        groupFile.setUploader(user.getUsername());
        groupFileService.updateById(groupFile);
        return Result.succ(groupFile);
    }

    @GetMapping("getFile")
    public Result getFile(int groupId) {
        List<GroupFile> groupFileInfo;
        groupFileInfo = groupFileService.list(new QueryWrapper<GroupFile>().eq("groupId", groupId));
        return Result.succ(MapUtil.builder()
                .put("items", groupFileInfo)
                .map());
    }

    @PostMapping("updateName")
    public Result updateFile(@RequestBody GroupFile groupFile) {
        groupFileService.updateById(groupFile);
        return Result.succ(groupFile);
    }

    @PostMapping("deleteFile")
    public Result deleteFile(@RequestBody GroupFile groupFile) {
        groupFileService.remove(new QueryWrapper<GroupFile>().eq("id", groupFile.getId()));
        return Result.succ(groupFile);
    }
}
