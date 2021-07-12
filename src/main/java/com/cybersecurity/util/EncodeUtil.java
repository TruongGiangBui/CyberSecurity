package com.cybersecurity.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class EncodeUtil {
    public String getSHA256(String password){
        String rs=null;
        try{
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            rs=bytesToHex(md.digest());
        }catch (Exception e){e.printStackTrace();}
        return rs;
    }
    public String bytesToHex(byte[] bytes){
        StringBuilder result=new StringBuilder();
        for(byte b:bytes){
            result.append(Integer.toString((b&0xff)+0x100,16).substring(1));
        }
        return result.toString();
    }

}
