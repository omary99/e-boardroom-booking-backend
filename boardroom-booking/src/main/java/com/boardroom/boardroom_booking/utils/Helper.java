package com.boardroom.boardroom_booking.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Slf4j
public class Helper {
    private static final Logger logger = LoggerFactory.getLogger("IKMIS-GENERAL-LOG");

    @Value("${scanner.url}")
    private String scannerUrl;



    @Value("${scanner.token}")
    private String token;

    ObjectMapper mapper;


    public static long getTimeStamp() {
        return new Date().getTime();
    }

    public static LocalDate stringToLocalDate(String formDate) {
        LocalDate convertedDate = LocalDate.parse(formDate);
        return convertedDate;
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime getLocalDate(long valueToConvert) {
        Date currentDate = new Date(valueToConvert);
        return currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate stringToDate(String dateToConvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dateToConvert, formatter);
    }

    public static LocalDate stringToLocalDateFormat(String dateToConvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateToConvert, formatter);
    }

    public static LocalDate anyStringFormatToLocalDate(String dateToConvert, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDate.parse(dateToConvert, formatter);
    }

    public static LocalDate stringToDateFormat(String dateToConvert) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd-MM-yyyy][yyyy-MM-dd]");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate receivedDate = LocalDate.parse(dateToConvert, formatter);
        String returnDateStr = receivedDate.format(formatter2);

        return LocalDate.parse(returnDateStr, formatter2);
    }

    public static String dateToGePGDateFormat(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static Timestamp dateFromGePGDateFormat(String trxDtTm){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Timestamp trxnDate = null;
        try {
            Date date = sdf.parse(trxDtTm);
            trxnDate = new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return trxnDate;
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }

    public static boolean isObjectHashMap(Object source) {

        try {
            HashMap<String, Object> result = (HashMap<String, Object>) source;
            return true;
        } catch (ClassCastException e) {
        }

        return false;

    }

    public static boolean isObjectString(Object source) {

        try {
            String result = (String) source;
            return true;
        } catch (ClassCastException e) {
        }

        return false;

    }

    public static String harshMethod(String string) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(string.getBytes());

        byte[] byteData = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
        }
        return sb.toString();
    }


    /**
     * This method generate unique number token base on current Date and time
     */
    public static String getNumberToken() throws ParseException {
        Date currDate = new Date();
        String token = "";
        token = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currDate);
        return token;
    }

    /**
     * generate OTP
     *
     * @return
     */
    public int generateOtp() {
        Random random = new Random();
        return random.nextInt(89999999) + 10000001;
    }

    public static void copyNonNullProperties(Object source, Object destination) {
        BeanUtils.copyProperties(source, destination,
                getNullPropertyNames(source));
    }

    /**
     * Returns an array of null properties of an object
     *
     * @param source
     * @return
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set emptyNames = new HashSet();
        for (java.beans.PropertyDescriptor pd : pds) {
            //check if value of this property is null then add it to the collection
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return (String[]) emptyNames.toArray(result);
    }

    public static void print(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String className = "";
        if (obj == null) {
            className = "You've passed null object";
        } else {
            className = obj.getClass().getSimpleName();
        }
        System.out.println("--------------------------" + className
                + "----------------------------------------------------------");
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            System.out.println("########################ERROR########################");
            e.printStackTrace();
        }
        System.out.println(
                "-----------------------------------------------------------------------------------------------------");
    }

    /**
     * convert number to currency format
     * @param number
     * @return
     */
    public static String thousandSeparator(Object number){
        return String.format("%,.2f",number);
    }

    /**
     * Remove Thousand Separator
     * @param number
     * @return
     */
    public static String removeThousandSeparator(String number){

        return number.replace(",", "");
    }

    /**
     * Beautify xml string
     * @param xml
     * @return
     */
    public static String beautifyXmlString(String xml) {

        try {

            InputSource src = new InputSource(new StringReader(xml));
            Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();

            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.

            return writer.writeToString(document);

        } catch (Exception e) {
            //throw new RuntimeException(e);
            return xml;
        }
    }

    /**
     * base 64 validation
     * @param value
     * @return
     */
    public static boolean base64Validation(String value) {
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            decoder.decode(value);
            return true;
        } catch (IllegalArgumentException iae) {
            // That Base64 string wasn't valid.
            logger.error("Base64 string wasn't valid : "+ iae);
            return false;
        }
    }

    public static String storeImage(String uploadDir, String base64Data, String fileName,String extension) {
        String path;

        if (!uploadDir.endsWith("/"))
            uploadDir = uploadDir + "/";

        try {
            if (base64Validation(base64Data)) {

                File attachment = new File(uploadDir + getNumberToken()+"-"+ fileName + "."+extension);
                try (FileOutputStream fos = new FileOutputStream(attachment)) {

                    byte[] decoder = Base64.getDecoder().decode(base64Data);
                    fos.write(decoder);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FileOutputStream outStream = new FileOutputStream(attachment);
                outStream.write(Base64.getDecoder().decode(base64Data));
                outStream.close();
                path = attachment.getAbsolutePath();

            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save attachment" + e.getMessage());
        }
        return path;
    }




    public static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
