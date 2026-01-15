package com.boardroom.boardroom_booking.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Component
public final class CustomGeneratedData {
	
	
	private static final Logger logger = LoggerFactory.getLogger(CustomGeneratedData.class);


	public static String GenerateFinancialYear() {
		LocalDate today = LocalDate.now();
		int currentMonth = today.getMonthValue();
		int months[] = {1, 2, 3, 4, 5, 6}; // first six month of normal year
		String financialYear;
		if (Arrays.stream(months).anyMatch(value -> value == currentMonth)) {
			int lastYear = today.minusYears(1).getYear();
			financialYear = lastYear + "/" + today.format(DateTimeFormatter.ofPattern("yy"));
		} else {
			LocalDate nextYear = today.plusYears(1);
			financialYear = today.getYear() + "/" + nextYear.format(DateTimeFormatter.ofPattern("yy"));
		}
		return financialYear;
	}

//	public static String GenerateNextCode(T model) {
//		UUID uuid = Generators.timeBasedGenerator().generate();
//		String uuidStr = uuid.toString().replace("-", "");
//		return uuidStr;
//	}

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

	public static LocalDate stringToDateFormat(String dateToConvert) throws ParseException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd-MM-yyyy][yyyy-MM-dd]");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		LocalDate receivedDate = LocalDate.parse(dateToConvert, formatter);
		String returnDateStr = receivedDate.format(formatter2);

		return LocalDate.parse(returnDateStr, formatter2);
	}
	
	private PrivateKey getPrivateKey(String keyPass,String keyFilePath) throws Exception {

		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream is = new FileInputStream(keyFilePath);
		keyStore.load(is,keyPass.toCharArray());
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(null,keyPass.toCharArray());
		return privateKey;
		}

		public String CreateSignature(String content, String privateKeyPass,
		String privateKeyFilePath) throws Exception {

		byte[] data = content.getBytes();
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initSign(getPrivateKey(privateKeyPass,privateKeyFilePath));
		sig.update(data);
		byte[] signatureBytes = sig.sign();

		//String resultSig = new BASE64Encoder().encode(signatureBytes);
		// System.out.println(resultSig);

		String resultSig = Base64.getEncoder().encodeToString(signatureBytes);
		//String resultSig = org.apache.commons.codec.binary.Base64.encodeBase64String(signatureBytes);
		// System.out.println(resultSig);
		return resultSig;
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
	


	public static final LocalDateTime convertToLocalDateTime (String localDateTime){
		LocalDateTime dateTime = LocalDateTime.parse(localDateTime);
		return dateTime;
	}

}
