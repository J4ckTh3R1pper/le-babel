package dayp308.chatroom.mapper;

import dayp308.chatroom.bean.ServerFile;
import org.apache.catalina.Server;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ServerFileMapper {
    List<ServerFile> getAllFilesInServer(@Param("server_id") int serverId);
    List<ServerFile> searchFileInServer(@Param("server_id") int serverId, @Param("keyword") String keyword);
    ServerFile getFileById(@Param("file_id") int fileId);
    int addFile(@Param("file") ServerFile file);
    int deleteFile(@Param("file_id") int fileId);
}
