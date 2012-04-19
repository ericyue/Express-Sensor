/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   EncryptString.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-7-21
 */
package info.ericyue.es.util;
import java.security.MessageDigest;
public class EncryptString {
	public static String encryptString(String str,String mode) throws Exception{
		MessageDigest md5=MessageDigest.getInstance(mode);
		byte[] srcBytes=str.getBytes();
		md5.update(srcBytes);	
		byte[]resultBytes=md5.digest();
		String result=new String(BytetoHex(resultBytes));
		return result;		
	}
	private static String BytetoHex(byte[] bin){
	    StringBuffer buf = new StringBuffer();
	    for (int i = 0; i < bin.length; ++i) {
	        int x = bin[i] & 0xFF, h = x >>> 4, l = x & 0x0F;
	        buf.append((char) (h + ((h < 10) ? '0' : 'a' - 10)));
	        buf.append((char) (l + ((l < 10) ? '0' : 'a' - 10)));
	    }
	    return buf.toString();
	}
}
