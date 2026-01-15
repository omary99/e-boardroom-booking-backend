package com.boardroom.boardroom_booking.utils;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {

    public static String harshMethod(String string) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(string.getBytes());

        byte[] byteData = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
        }
        return sb.toString();
    }

    //Check Dto fields
    public static String checkNullFields(Object dto) {
        List<String> nullFields = new ArrayList<>();

        // Get all fields of the DTO class
        Field[] fields = dto.getClass().getDeclaredFields();

        // Iterate through fields and check for null values
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(dto);
                if (value == null) {
                    nullFields.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // Handle or log exception
            }
        }

        // Generate descriptive message
        if (nullFields.isEmpty()) {
            return "true";
        } else {
            return "The following field(s) are null: " + String.join(", ", nullFields);
        }
    }

    public static boolean isStrongPassword(String password) {
        // At least 8 characters
        if (password.length() < 8) {
            return false;
        }

        // At least one uppercase letter
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Matcher upperCaseMatcher = upperCasePattern.matcher(password);
        if (!upperCaseMatcher.find()) {
            return false;
        }

        // At least one lowercase letter
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Matcher lowerCaseMatcher = lowerCasePattern.matcher(password);
        if (!lowerCaseMatcher.find()) {
            return false;
        }

        // At least one digit
        Pattern digitPattern = Pattern.compile("\\d");
        Matcher digitMatcher = digitPattern.matcher(password);
        if (!digitMatcher.find()) {
            return false;
        }

        // At least one special character (you can customize this pattern)
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
        Matcher specialCharMatcher = specialCharPattern.matcher(password);
        if (!specialCharMatcher.find()) {
            return false;
        }

        // Passed all checks
        return true;
    }

    public static LocalDate parseToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static void copyNonNullProperties(Object source, Object destination){
        BeanUtils.copyProperties(source, destination,
                getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            //check if value of this property is null then add it to the collection
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return (String[]) emptyNames.toArray(result);
    }

    public static LocalDateTime parseDate(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime);
        } catch (Exception e) {
            try {
                return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
            } catch (Exception ex) {
                try {
                    return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception ec) {
                    try {
                        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S"));
                    } catch (Exception exc) {
                        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    }
                }
            }
        }
    }

    public static String incrementString(String input) {

        String numericPart = input.replaceAll("[^0-9]", "");
        int numericValue = Integer.parseInt(numericPart);

        String incrementedString = autoIncrementAndFormat(numericValue);
//        System.out.println("Result for " + input + ": " + incrementedString);
        return incrementedString;
    }

    public static String autoIncrementAndFormat(int input) {
        int incrementedValue = input+1;
        String formattedValue = String.format("%05d", incrementedValue);
//        System.out.println("formattedValue for " + input + ": " + formattedValue);
        return formattedValue;
    }



    public static String  getWrappedRandomFileName(String fileName){
        String fileExtension = getFileExtension(fileName);
        return getRandomString()+"-"+getRandomString()+"."+fileExtension;
    }

    public static String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1)).orElse(null);
    }

    public static String getFileBaseName(String fileName){
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static String getRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String getReferenceNumber() {
        UUID idOne = UUID.randomUUID();
        UUID idTwo = UUID.randomUUID();
        UUID idThree = UUID.randomUUID();
        UUID idFour = UUID.randomUUID();

        String time = idOne.toString().replace("-", "");
        String time2 = idTwo.toString().replace("-", "");
        String time3 = idThree.toString().replace("-", "");
        String time4 = idFour.toString().replace("-", "");

        StringBuilder data = new StringBuilder();
        data.append(System.currentTimeMillis());
        data.append(time);
        data.append(time2);
        data.append(2);
        data.append(time3);
        data.append(time4);

        SecureRandom random = new SecureRandom();
        int beginIndex = random.nextInt(100);       //Begin index + length of your string < data length
        int endIndex = beginIndex + 6;            //Length of string which you want

        return data.substring(beginIndex, endIndex).toUpperCase();
    }

    public static boolean userHasAllPermissions(HttpServletRequest request, List<String> permissions){
        boolean hasPermissions = true;
        for(String permission : permissions){
            if (!request.isUserInRole(permission)){
                hasPermissions = false;
                break;
            }
        }
        return hasPermissions;
    }

    public static boolean userHasAnyPermission(HttpServletRequest request, List<String> permissions){
        boolean hasAnyPermission = false;
        for(String permission : permissions){
            if (request.isUserInRole(permission)){
                hasAnyPermission = true;
                break;
            }
        }
        return hasAnyPermission;
    }

    public static  String convertArrayToString(List<String> listOfString){
        return listOfString.toString().replaceAll("[^a-zA-Z,]+","");
    }


}
