package dayp308.chatroom.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ServerFile {
    private Integer fileId;
    private String fileName;
    private String hash;
    private int userId;
    private Integer serverId;
    private String extension;
    private String storeName;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyyMMddhhmmss")
    private Date uploadDate;

    public ServerFile() {

    }

    public ServerFile(String fileName, String storeName, String hash, int userId, String extension, Date uploadDate, int serverId) {
        this.fileName = fileName;
        this.storeName = storeName;
        this.hash = hash;
        this.userId = userId;
        this.extension = extension;
        this.uploadDate = uploadDate;
        this.serverId = serverId;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

}
