package dayp308.chatroom.deprecated;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dayp308.chatroom.model.*;
import org.apache.commons.io.FilenameUtils;

import dayp308.chatroom.deprecated.mapper.ChatServerMapper;
import dayp308.chatroom.deprecated.mapper.ServerFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import dayp308.chatroom.deprecated.mapper.MessageMapper;
import org.springframework.web.multipart.MultipartFile;
import dayp308.chatroom.exception.FileUploadException;

import java.io.*;
import dayp308.chatroom.util.FileUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class FileService {
	private final ChatServerMapper chatServerMapper;
	private final SimpleDateFormat dateFormat;
	private final MessageMapper messageMapper;
	private final ServerFileMapper serverFileMapper;

	@Value("${dayp308.chatroom.fileStorePath}")
	private String filePath;

	@Value("${dayp308.chatroom.resourcePath}")
	private String resourcePath;

	@Autowired
	public FileService(ChatServerMapper chatServerMapper, MessageMapper messageMapper, ServerFileMapper serverFileMapper) {
		this.chatServerMapper = chatServerMapper;
		this.messageMapper = messageMapper;
		this.serverFileMapper = serverFileMapper;
		this.dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
	}
//	FIXME: 设置异常回退机制
	public String uploadFile(MultipartFile file, int userId, int serverId) throws FileUploadException {
		if (file.isEmpty())
			throw new FileUploadException("empty file is not allowed.");
		String fileName = file.getOriginalFilename();
		String baseName = FilenameUtils.getBaseName(fileName);
		String extension = FilenameUtils.getExtension(fileName);
		String md5 = FileUtil.getMD5(file);
		Date uploadDate = Date.from(Instant.now());
		String storeName = this.dateFormat.format(uploadDate) + "_" + md5;
		ServerFile savedFile = new ServerFile(baseName, storeName, md5, userId, extension, uploadDate, serverId);
		try {
			InputStream stream = file.getInputStream();
			File fileDir = new File(filePath + File.separator + serverId + File.separator);
			if (!fileDir.exists())
				fileDir.mkdirs();
			this.serverFileMapper.addFile(savedFile);
			Files.copy(stream, Paths.get(filePath, serverId + "", storeName + "." + extension), StandardCopyOption.REPLACE_EXISTING);
			return storeName + "." + extension;
		} catch (Exception e) {
			e.printStackTrace();
			if (savedFile.getFileId() != null)
				this.serverFileMapper.deleteFile(savedFile.getFileId());
			throw new FileUploadException(e.getMessage(), e);
		}
	}

	public String uploadImage(MultipartFile imageFile) throws FileUploadException {
		if (imageFile.isEmpty())
			throw new FileUploadException("empty file is not allowed.");
		String fileName = imageFile.getOriginalFilename();
		String extension = FilenameUtils.getExtension(fileName);
		String storeName = FileUtil.getMD5(imageFile) + "." + extension;
		try {
			InputStream stream = imageFile.getInputStream();
			File fileDir = new File(resourcePath + File.separator + "images" );
			if (!fileDir.exists())
				fileDir.mkdirs();
			Files.copy(stream, Paths.get(resourcePath, "images", storeName), StandardCopyOption.REPLACE_EXISTING);
			return "/images/" + storeName;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileUploadException(e.getMessage(), e);
		}
	}
	public File getFile(ServerFile serverFile) {
		return new File(filePath + File.separator + serverFile.getServerId()+"", serverFile.getStoreName() + "." + serverFile.getExtension());
	}

	public void deleteFile(int fileId) throws IOException {
		ServerFile file = getFileById(fileId);
		this.serverFileMapper.deleteFile(fileId);
		Files.deleteIfExists(Paths.get(filePath, file.getServerId()+"", file.getStoreName()+"."+file.getExtension()));
	}

	public List<ServerFile> getFilesInServer(int serverId) {
		return this.serverFileMapper.getAllFilesInServer(serverId);
	}

	public PageInfo<ServerFile> getPagedFiles(Integer page, int serverId) {
		PageHelper.startPage(page, 8);
		List<ServerFile> fileList = getFilesInServer(serverId);
		PageInfo<ServerFile> pageInfo = new PageInfo<>(fileList);
		PageHelper.clearPage();
		return pageInfo;
	}

	public PageInfo<ServerFile> getPagedFilesByKeyword(Integer page, int serverId, String keyword) {
		PageHelper.startPage(page, 8);
		List<ServerFile> fileList = this.serverFileMapper.searchFileInServer(serverId, keyword);
		PageInfo<ServerFile> pageInfo = new PageInfo<>(fileList);
		PageHelper.clearPage();
		return pageInfo;
	}
	public ServerFile getFileById(int fileId) {
		return this.serverFileMapper.getFileById(fileId);
	}
}
