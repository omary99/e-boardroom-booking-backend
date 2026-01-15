package com.boardroom.boardroom_booking.utils;

import com.boardroom.boardroom_booking.response.ApiResponseWrapperDto;
import com.boardroom.boardroom_booking.response.DefaultNullRespData;
import com.boardroom.boardroom_booking.response.IkmisListResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class GlobalMethod {

    /**
     * The Constant logger.
     */
    private Logger LOGGER = LogManager.getLogger(GlobalMethod.class);

    @Autowired
    ObjectMapper mapper;

    public ResponseEntity<?> response(Integer code,String statusMsg, Object data) {

        ApiResponseWrapperDto responseWrapperDto = new ApiResponseWrapperDto();

        if (data == null) {
            data = new DefaultNullRespData();
        }
        responseWrapperDto.setStatus(ResponseCode.FAILURE.equals(code) ? Boolean.FALSE : Boolean.TRUE);
        responseWrapperDto.setStatusCode(code);
        responseWrapperDto.setDescription(statusMsg);

        responseWrapperDto.setData(data);

        //log.info("Response : " + responseWrapperDto);
        return new ResponseEntity<>(responseWrapperDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> responseError(Integer code,Boolean status,String statusMsg, List<String> errors) {

        ApiResponseWrapperDto responseWrapperDto = new ApiResponseWrapperDto();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errors", errors);
        responseWrapperDto.setData(body);
        responseWrapperDto.setStatus(status);
        responseWrapperDto.setStatusCode(code);
        responseWrapperDto.setDescription(statusMsg);

        //log.info("Response : " + responseWrapperDto);
        return new ResponseEntity<>(responseWrapperDto, HttpStatus.OK);
    }

    public static ResponseEntity<?> customResponse(Integer code, String description, Object data) {

        ApiResponseWrapperDto responseWrapperDto = new ApiResponseWrapperDto();

        if (data == null) {
            data = new DefaultNullRespData();
        }
        responseWrapperDto.setStatus(ResponseCode.FAILURE.equals(code) ? Boolean.FALSE : Boolean.TRUE);
        responseWrapperDto.setStatusCode(code);
        responseWrapperDto.setDescription(description);
        responseWrapperDto.setData(data);

        return new ResponseEntity<>(responseWrapperDto, HttpStatus.OK);
    }


    public String currentFinancialYear() {
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

    /** Paginated list response */
    public ResponseEntity<?> listResponse(Page<?> page, String description) {
        IkmisListResponseWrapper wrapper = new IkmisListResponseWrapper();
        wrapper.setItemList(page.getContent());
        wrapper.setCurrentPage(page.getNumber() + 1); // zero-based page
        wrapper.setNumberOfElements(page.getNumberOfElements());
        wrapper.setTotalPage(page.getTotalPages());
        wrapper.setHasNext(page.hasNext());
        wrapper.setHasPrevious(page.hasPrevious());
        wrapper.setSort(page.getSort().toString());

        ApiResponseWrapperDto response = new ApiResponseWrapperDto();
        response.setData(wrapper);
        response.setStatus(true);
        response.setStatusCode(200);
        response.setDescription(description != null ? description : "List fetched successfully");

        //logger.info("Paginated List Response: " + response);
        return ResponseEntity.ok(response);
    }


    /**
     * sort by parameters
     * @param sort
     * @return
     */
    public List<Sort.Order> sortByParameter(String[] sort){
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }
        return orders;
    }
    /**
     * Sorting Direction
     * @param direction
     * @return
     */
    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    public String encryptUsingBcrypt(String toEncrypt) {

        String encrypted = "";
       
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
            encrypted = encoder.encode(toEncrypt);
        } catch (Exception e) {
        	LOGGER.error("Exception on encrypting {}, error {} ", toEncrypt, e.getMessage());
        }

        return encrypted;
    }


  
    public Date getTodayDate(String dateType) throws ParseException {
        Date today = null;
        if (dateType.toUpperCase().equals("NORMAL")) {
            today = new Date();
        }
        return today;
    }
  
//    public String connectToAnotherSystem(String url, String content, String method, String encoded, HashMap<String, String> header) throws IOException {
//        try {
//
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpPost postRequest = new HttpPost(url);
//            postRequest.addHeader("Content-Type", "application/json");
//            StringEntity userEntity = new StringEntity(content);
//            postRequest.setEntity(userEntity);
//            //postRequest.setEntity((HttpEntity)userEntity);
//            CloseableHttpResponse response = httpClient.execute((HttpUriRequest) postRequest);
//            String responseString = EntityUtils.toString(response.getEntity());
//            response.close();
//            return responseString;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    

    public List<String> getErrorMessage(List<FieldError> fieldErrorList) {
        List<String> messageError = new ArrayList<>();
        String errorMessage;
        StringBuilder stringBuffer;
        try {
            if (!fieldErrorList.isEmpty()) {
                stringBuffer = new StringBuilder();
                for (FieldError error : fieldErrorList) {
                    stringBuffer.append(error.getDefaultMessage()).append(", ");
                }
                stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 2);
                errorMessage = stringBuffer.toString().trim();
                messageError.add(errorMessage);
            }
        } catch (Exception e) {
            LOGGER.error("GET ERROR MESSAGE Exception: ", e);
        }
        return messageError;

    }

    /**
     * This method formats a response to send to API calls for non pageable
     * responses
     *
     * @param result
     * @param error
     * @param message
     * @return
     * @author emhagama
     * @date 2020-Feb-01
     */
    public ResponseEntity<ApiResponse> response(Object result, Boolean error, List<String> message) {
    	ApiResponse r = new ApiResponse();
        r.setData(result);
        r.setCurrentPage(1);
        // System.out.println(new Gson().toJson(result));
        r.setTotalPages(1);
        r.setMessage(message);
        r.setError(error);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }


    public boolean isValidEmailAddress(final String email) {

        Pattern pattern;
        Matcher matcher;
        final String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        pattern = Pattern.compile(emailPattern);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    /**
     * This method formats a response to send to API calls for pageable responses
     *
     * @param result
     * @param error
     * @param message
     * @param totalPages
     * @param currentPage
     * @return
     * @author emhagama
     * @date 2020-Feb-01
     */
    public ResponseEntity<ApiResponse> response(Object result, Boolean error, List<String> message, int totalPages,
                                                int currentPage) {
    	ApiResponse r = new ApiResponse();
        r.setData(result);
        r.setCurrentPage(currentPage + 1);
        r.setTotalPages(totalPages);
        r.setMessage(message);
        r.setError(error);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }


    public MediaType assingContenMediaType(String requestContentMediaType) {

        MediaType contentMediaType;

        switch (requestContentMediaType.toUpperCase()) {

            case "XML":
                contentMediaType = MediaType.APPLICATION_XML;
                break;

            case "JSON":
                contentMediaType = MediaType.APPLICATION_JSON;
                break;

            case "TXML":
                contentMediaType = MediaType.TEXT_XML;
                break;

            default:
                contentMediaType = MediaType.TEXT_PLAIN;
        }

        return contentMediaType;

    }
    
    /**
	 * This method generate random id
	 * 
	 * @return String
	 * @throws ParseException
	 */

	public String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	/*
	 * Generate random String from Java Random numbers Library Returns @String
	 */

	public String generateRandomString(int captchaStringLength) {

		Random random = new Random();
		String gepgCaptchaString = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNOPQRSTUVWXYZ";
		StringBuffer stringBuffer = new StringBuffer();

		while (stringBuffer.length() < captchaStringLength) {
			int index = (int) (random.nextFloat() * gepgCaptchaString.length());
			stringBuffer.append(gepgCaptchaString.substring(index, index + 1));
		}

		return stringBuffer.toString();
	}

}
