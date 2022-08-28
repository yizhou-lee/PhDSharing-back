package com.lyz.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.db.sql.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.common.lang.Result;
import com.lyz.entity.Document;
import com.lyz.entity.Pdf;
import com.lyz.service.DocumentService;
import com.lyz.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyz
 * @since 2022-01-16
 */
@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    PdfService pdfService;
    @Autowired
    DocumentService documentService;

    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");

    @PostMapping("upload")
    public Result uploadDocument(MultipartFile file, HttpServletRequest req) {
        String originName = file.getOriginalFilename();
        if(!originName.endsWith(".pdf")){
            return Result.fail("wrong file type");
        }

        String format = sdf.format(new Date());
//        String realPath = "/Users/liyizhou/pdfStore" + format;
        String realPath = "/usr/local/pdfStore" + format;
        File folder = new File(realPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String newName = UUID.randomUUID().toString() + ".pdf";

        try{
            file.transferTo(new File(folder, newName));
            String url = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+format+newName;
            Pdf newPdf = new Pdf();
            newPdf.setName(originName);
            newPdf.setPath(url);
            pdfService.save(newPdf);
            return Result.succ(MapUtil.builder()
                    .put("id", newPdf.getId())
                    .put("filePath", url)
                    .map());
        } catch (IOException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("get")
    public Result getPdf(int pdfId){
        if(!"null".equals(String.valueOf(pdfId)) && pdfId != 0){
            Pdf pdf = pdfService.getOne(new QueryWrapper<Pdf>().eq("id", pdfId));
            return Result.succ(MapUtil.builder()
                    .put("pdfName", pdf.getName())
                    .put("pdfUrl", pdf.getPath())
                    .map());
        }
        return Result.succ(MapUtil.builder()
                .put("pdfName", "")
                .put("pdfUrl", "")
                .map());
    }

    @PostMapping("delete")
    public Result pdfDelete(int pdfId) {
        pdfService.removeById(pdfId);
        Document document = documentService.getOne(new QueryWrapper<Document>().eq("fileId", pdfId));
        document.setFileId(0);
        documentService.updateById(document);
        return Result.succ(20000, "pdf delete successfully", document);
    }

}
