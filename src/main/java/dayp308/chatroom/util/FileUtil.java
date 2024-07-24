package dayp308.chatroom.util;
import java.io.*;
import java.security.MessageDigest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Hex;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	public static String getMD5(MultipartFile file) {
		FileInputStream stream = null;
		try {
			byte[] uploadBytes = file.getBytes();
			//file->byte[],生成md5
			String md5Hex = DigestUtils.md5Hex(uploadBytes);
			return md5Hex;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (stream != null){
                    stream.close();
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}
