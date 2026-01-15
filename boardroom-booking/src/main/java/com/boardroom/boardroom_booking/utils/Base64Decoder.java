package com.boardroom.boardroom_booking.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class Base64Decoder implements MultipartFile {

    private final byte[] UPLOADED_FILE;

    private final String HEADER;

    public  Base64Decoder(byte[] uploadedFile, String header){

        UPLOADED_FILE = uploadedFile;
        HEADER = header;
    }

    public static MultipartFile multipartFile(byte[] uploadedFile,String header){
        return new Base64Decoder(uploadedFile,header);
    }

    @Override
    public String getName() {
        return System.currentTimeMillis()+Math.random()+"."+HEADER.split("/")[1];
    }

    @Override
    public String getOriginalFilename() {
        return System.currentTimeMillis()+(int)Math.random()*10000+"."+HEADER.split("/")[1];
    }

    @Override
    public String getContentType() {
        return HEADER.split(":")[1];

    }

    @Override
    public boolean isEmpty() {
        return UPLOADED_FILE == null || UPLOADED_FILE.length == 0;
    }

    @Override
    public long getSize() {
        return UPLOADED_FILE.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return UPLOADED_FILE;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(UPLOADED_FILE);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {

        new FileOutputStream(file).write(UPLOADED_FILE);
    }
}
