package dayp308.chatroom.service;

import dayp308.chatroom.exception.FileUploadException;
import dayp308.chatroom.util.FileUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

@Service
public class FileService {
    private final SimpleDateFormat dateFormat;

    @Value("${dayp308.chatroom.fileStorePath}")
    private String filePath;

    @Value("${dayp308.chatroom.assetsPath}")
    private String assetsPath;


    @Value("${dayp308.chatroom.imgPath}")
    private String imgPath;

    public FileService() {
        this.dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    }

    public String uploadImage(MultipartFile imageFile) throws FileUploadException {
        if (imageFile.isEmpty())
            throw new FileUploadException("empty file is not allowed.");
        String fileName = imageFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        String storeName = FileUtil.getMD5(imageFile) + File.separator + extension;
        try {
            InputStream stream = imageFile.getInputStream();
            File fileDir = new File(assetsPath + File.separator + imgPath + File.separator + "avatar");
            if (!fileDir.exists())
                fileDir.mkdirs();
            Files.copy(stream, Path.of(fileDir.getPath(), storeName), StandardCopyOption.REPLACE_EXISTING);
            return "/images/" + "avatar/" + storeName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploadException(e.getMessage(), e);
        }
    }

}
