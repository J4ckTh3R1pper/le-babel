package dayp308.chatroom.controller;

import com.github.pagehelper.PageInfo;
import dayp308.chatroom.bean.Identity;
import dayp308.chatroom.bean.ServerFile;
import dayp308.chatroom.bean.ServerMember;
import dayp308.chatroom.bean.User;
import dayp308.chatroom.service.ChatServerService;
import dayp308.chatroom.service.UserService;
import dayp308.chatroom.service.FileService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;


@RestController
public class FileController {
	private final UserService userService;
	private final ChatServerService chatServerService;
	private final FileService fileService;
	private final ObjectMapper objectMapper;

	@Autowired
	public FileController(UserService userService, ChatServerService chatServerService, FileService fileService) {
		this.userService = userService;
		this.chatServerService = chatServerService;
		this.fileService = fileService;
		this.objectMapper = new ObjectMapper();
	}

	@PostMapping(value = "/upload_file", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String uploadFile(@RequestPart("file") MultipartFile file, @RequestPart("serverId") String serverId, HttpServletRequest req) {
		String str;
		User user;
		try {
			int sid = Integer.parseInt(serverId);
			if (req.getSession().getAttribute("user") instanceof User)
				user = (User) (req.getSession().getAttribute("user"));
			else return "{code: 403, message: \"Unauthorized\" }";
			if (chatServerService.getUserIdentityOfServer(sid, user.getUserId()).getIndex() >= 0)
				str = this.fileService.uploadFile(file, user.getUserId(), Integer.parseInt(serverId));
			else return "{code: 403, message: \"You currently don't belong to this server.\" }";
		} catch (Exception e) {
			str = "{code: 500, message: \"" + e.getMessage() + "\"}";
		}
		return str;
	}

	@GetMapping(value = "/list_files", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getFileList(@RequestParam("serverId") int serverId, @Nullable @RequestParam(value = "page", defaultValue = "1") Integer page, @Nullable @RequestParam("keyword") String keyword) throws JsonProcessingException {
		PageInfo<ServerFile> pageInfo;
		if (page == null)
			page = 1;
		if (keyword == null || keyword.isEmpty())
			pageInfo = this.fileService.getPagedFiles(page, serverId);
		else pageInfo = this.fileService.getPagedFilesByKeyword(page, serverId, keyword);
		return this.objectMapper.writeValueAsString(pageInfo);
	}

	@GetMapping("delete_file")
	public void deleteFile(@RequestParam("fileId") int fileId) throws IOException {
		this.fileService.deleteFile(fileId);
	}

	@GetMapping("/download")
	public void downloadFile(@RequestParam("fileId") int fileId, HttpServletResponse response) throws IOException {
		File file;
		ServerFile serverFile = this.fileService.getFileById(fileId);
		file = this.fileService.getFile(serverFile);
		response.setContentType("application/octet-stream");
		response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
				.filename(serverFile.getFileName() + "." + serverFile.getExtension(), StandardCharsets.UTF_8)
				.build().toString()
		);
		byte[] buffer = new byte[1024];
		FileInputStream fileStream = null;
		BufferedInputStream bufferedStream = null;

		try {
			fileStream = new FileInputStream(file);
			bufferedStream = new BufferedInputStream(fileStream);
			ServletOutputStream outputStream = response.getOutputStream();
			int i = bufferedStream.read(buffer);
			while (i != -1) {
				outputStream.write(buffer, 0 ,i);
				i = bufferedStream.read(buffer);
			}
			System.out.println("dispense success");
		} catch (IOException e) {
			e.printStackTrace();
			response.sendError(500, "internal server error");
		} finally {
			try {
				if (bufferedStream != null)
					bufferedStream.close();
				if (fileStream != null)
					fileStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				response.sendError(500, "internal server error");
			}
		}
	}
}
