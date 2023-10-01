package com.example.Administrator.controller;

import cn.hutool.core.io.FileUtil;
import com.example.Administrator.common.AuthAccess;
import com.example.Administrator.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${ip:localhost}")
    String ip;

    @Value("${server.port}")
    String port;

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";//file directory //C:\Users\ACER\IdeaProjects\Administrator\files


    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();//get original file name, included filetype and file name
        ///aaa.png
        String mainName = FileUtil.mainName(originalFileName); ///aaa
        String extName = FileUtil.extName(originalFileName); ///png
        System.out.println("extension name: " + extName);


        //validate whether there is an existing file
        if (!FileUtil.exist(ROOT_PATH)) {
            FileUtil.mkdir(ROOT_PATH);
            //create file directory if not found
        }

        //C:\Users\ACER\IdeaProjects\Administrator\files\\123416523341_aaa.png
        if (FileUtil.exist(ROOT_PATH + File.separator + originalFileName)) { //if there is an existed file, then rename new file name
            originalFileName = System.currentTimeMillis() + "_" + mainName + "." + extName;

        }
        File saveFile = new File(ROOT_PATH + File.separator + originalFileName);//C:\Users\ACER\IdeaProjects\Administrator\files\\123416523341_aaa.png
        file.transferTo(saveFile); //save file to local storage in pc
        String url = "http://" + ip + ":" + port + "/file/download/" + originalFileName;
        return Result.success(url); //return link/path of file, the link/path is provided from backend and these link/path are download link
    }

    @AuthAccess
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {

        // download attachment
//        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

//preview attachment
        response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        String filePath = ROOT_PATH + File.separator + fileName;
        if (!FileUtil.exist(filePath)) {
            return;
        }

        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();

    }
}
