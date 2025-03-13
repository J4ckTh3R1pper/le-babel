package dayp308.chatroom.service;

import dayp308.chatroom.exception.FileUploadException;
import dayp308.chatroom.util.FileUtil;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
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

@Service
public class FileService {

    @Value("${dayp308.chatroom.imgPath}")
    private String imgPath;

    private String thumbnailDir = "thumbnails";

    public String uploadImage(MultipartFile imageFile) throws FileUploadException {
        if (imageFile.isEmpty())
            throw new FileUploadException("empty file is not allowed.");
        String fileName = imageFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        String storeName = FileUtil.getMD5(imageFile) + "." + extension;
        try {
            InputStream stream = imageFile.getInputStream();
            File fileDir = new File(imgPath + "/" + thumbnailDir);
            if (!fileDir.exists())
                fileDir.mkdirs();
            Path path = Path.of(imgPath, storeName);
            Path thumbnailPath = Path.of(fileDir.getPath());
            Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
            // 生成缩略图
            Thumbnails.of(path.toFile())
                    .outputFormat("webp")
                    .size(1280, 720)
                    .allowOverwrite(true)
                    .toFiles(thumbnailPath.toFile(), Rename.SUFFIX_HYPHEN_THUMBNAIL);
            return "/images/" + storeName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploadException(e.getMessage(), e);
        }
    }

}
