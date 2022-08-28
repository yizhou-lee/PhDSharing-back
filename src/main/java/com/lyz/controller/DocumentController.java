package com.lyz.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.common.lang.Result;
import com.lyz.entity.Document;
import com.lyz.entity.Pdf;
import com.lyz.service.DocumentService;
import com.lyz.service.PdfService;
import com.lyz.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/document/list")
    public Result fetchList(@RequestHeader("X-Token") String token, String title){
        int userId = Integer.parseInt(jwtUtils.getClaimByToken(token).getSubject());
        List<Document> documentInfo;
        documentInfo = documentService.list(new QueryWrapper<Document>().eq("userId", userId).like("title", title));
        int total = documentInfo.size();
        return Result.succ(MapUtil.builder()
                .put("total", total)
                .put("items", documentInfo)
                .map());
    }

    @PostMapping("/document/update")
    public Result updateDocument(@RequestBody Document document){
        documentService.updateById(document);
        return Result.succ(document);
    }

    @PostMapping("/document/create")
    public Result createDocument(@RequestHeader("X-Token") String token, @RequestBody Document document){
        int userId = Integer.parseInt(jwtUtils.getClaimByToken(token).getSubject());
        document.setUserId(userId);
        documentService.save(document);
        return Result.succ(document);
    }

    @PostMapping("/document/delete")
    public Result deleteDocument(@RequestBody Document document){
        pdfService.remove(new QueryWrapper<Pdf>().eq("id", document.getFileId()));
        documentService.removeById(document.getId());
        return Result.succ(document);
    }


}
