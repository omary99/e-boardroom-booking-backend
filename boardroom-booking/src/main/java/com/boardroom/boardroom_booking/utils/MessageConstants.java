package com.boardroom.boardroom_booking.utils;

public interface MessageConstants {
	
	String SUCCESS_MESSAGE = "Action completed Successful!";
	
	String FAIL_MESSAGE = "Request fail at this time, please try again";
	
	String INTERNAL_ERROR_MESSAGE = "Internal Server Error, please try again, if probleme persinst contact system administator! ";
	
	String NOT_FOUND = "Record(s) not found at this time";
	
	String ERROR_MESSAGE = "Error, occour on processing request please try again!";
	
	String FAIL_UPDATE_MESSAGE = "Record fail to update at this time, please try again!";
	
	String INVALID_DATA_FORMAT = "Invalid or Missing Required data";
	
	String FAIL_SAVE_MESSAGE = "Record fail to save at this time, please try again!";
	
	String APPROVAL_FAIL_SAVE_MESSAGE = "Fail to complete approval at this time, please try again!!";
	
	String ASSIGNMENT_FAIL_SAVE_MESSAGE = "Fail to make assignment at this time, please try again!!";
	
	String REQUEST_ALREADY_APPROVED = "Record already approved, can't not assigned or approved";
	
	String REQUEST_FOR_ASSIGNMENT_NOT_FOUND = "Request(Record) for Assignment not found!";
	
	String REQUEST_FOR_APPROVE_NOT_FOUND = "Request(Record) for Approval not found!";
	
	String INCOMPLETE_INFORMATION= " Some of the information are not completed, request can to be approved!";
	
	String USER_TOKEN_EXPIRED = "OTP Token already expired";
	
	String USER_INACTIVE_ACCOUNT = "Inactive account";
	
	String USER_INVALID_OR_TOKEN = "Invalid user or OTP token";
	
}
