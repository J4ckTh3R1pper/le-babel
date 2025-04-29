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

    public static ByteArrayInputStream inputStream2ByteArrayInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte [] buf = new byte[1024];
        int len;
        while ( (len=is.read(buf)) > -1 ) {
            output.write(buf, 0, len);
        }
        output.flush();
        is.close();
        return new ByteArrayInputStream(output.toByteArray());
    }
}
