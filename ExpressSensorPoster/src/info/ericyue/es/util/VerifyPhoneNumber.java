/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   VerifyPhoneNumber.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-18
 */
package info.ericyue.es.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyPhoneNumber {
	public static boolean isPhoneNumberValid(String phoneNumber){  
	   boolean isValid = false;  
	   String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";  
	   String expression2 ="^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";  
	   CharSequence inputStr = phoneNumber;  
	   Pattern pattern = Pattern.compile(expression);  
	   Matcher matcher = pattern.matcher(inputStr);  
	   Pattern pattern2 =Pattern.compile(expression2);  
	   Matcher matcher2= pattern2.matcher(inputStr);  
	   if(matcher.matches()||matcher2.matches()){  
		   isValid = true;  
	   }  
	   return isValid;   
	 }  
}