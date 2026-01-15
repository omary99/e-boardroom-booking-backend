package com.boardroom.boardroom_booking.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

public class Base64Converter {

    public static MultipartFile converter(String source) {
        String[] charArray = source.split(",");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = new byte[0];
        bytes = decoder.decode(charArray[1]);
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        return Base64Decoder.multipartFile(bytes, charArray[0]);

    }

//    public static String convertInputStreamToBase64(InputStream is){
//        String imageStr = null;
//        try {
//            byte[] bytes = IOUtils.toByteArray(is);
//            imageStr = Base64.getEncoder().encodeToString(bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//            // log.error("Error : {}, occurred while converting file to base 64",e.getMessage());
//        }
//        return imageStr;
//    }
}
