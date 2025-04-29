package dayp308.chatroom.service;

import dayp308.chatroom.exception.FileUploadException;
import dayp308.chatroom.util.FileUtil;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Value("${lebabel.img-path}")
    private String imgPath;

    private String thumbnailDirName = "thumbnails/";

    public String uploadImage(MultipartFile imageFile) throws FileUploadException {
        if (imageFile.isEmpty())
            throw new FileUploadException("empty file is not allowed.");
        String fileName = imageFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        try ( ByteArrayInputStream stream = FileUtil.inputStream2ByteArrayInputStream(imageFile.getInputStream()) ) {
            String md5 = DigestUtils.md5Hex(stream);
            String storeName = md5  + "." + extension;
            stream.reset();
            File thumbnailDir = new File(imgPath, thumbnailDirName);
            if (!thumbnailDir.exists())
                thumbnailDir.mkdirs();
            Path path = Path.of(imgPath, storeName);
            Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
            // 生成缩略图
            File thumbnail = new File(thumbnailDir, md5);
            Thumbnails.of(path.toFile())
                    .outputFormat("jpeg")
                    .size(1280, 720)
                    .allowOverwrite(true)
                    .toFile(thumbnail);
            return storeName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploadException(e.getMessage(), e);
        }
    }

}
