package dayp308.chatroom.deprecated.mapper;

import dayp308.chatroom.deprecated.model.ServerFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServerFileMapper {
    List<ServerFile> getAllFilesInServer(@Param("server_id") int serverId);
    List<ServerFile> searchFileInServer(@Param("server_id") int serverId, @Param("keyword") String keyword);
    ServerFile getFileById(@Param("file_id") int fileId);
    int addFile(@Param("file") ServerFile file);
    int deleteFile(@Param("file_id") int fileId);
}
