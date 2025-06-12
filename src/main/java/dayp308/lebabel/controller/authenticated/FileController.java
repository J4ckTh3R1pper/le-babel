package dayp308.lebabel.controller.authenticated;

import dayp308.lebabel.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/api/auth/upload_image")
    public String uploadImage(@RequestPart("image") MultipartFile image) {
        String storeName = fileService.uploadImage(image);
        return "/api/images/" + storeName;
    }

}
