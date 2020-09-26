package data;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA-1加密工具类
 */
public class SHA1Util {

    /**
     * SHA-1加密字符串
     *
     * @param info
     * @return
     */
    public static String getSHA(String info) {
        byte[] bytesSHA = null;
        try {
            // 得到一个SHA-1的消息摘要
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            // 添加要进行计算摘要的信息
            messageDigest.update(info.getBytes());
            // 得到该摘要
            bytesSHA = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String strSHA = byteToHex(bytesSHA);
        return strSHA;
    }

    /**
     * Bytes数组转换为字符串
     *
     * @param bytes
     * @return
     */
    private static String byteToHex(byte[] bytes) {
        String hs = "";
        String temp;
        for (byte b : bytes) {
            temp = (Integer.toHexString(b & 0XFF));
            if (temp.length() == 1) {
                hs = hs + "0" + temp;
            } else {
                hs = hs + temp;
            }
        }
        return hs;
    }
}

