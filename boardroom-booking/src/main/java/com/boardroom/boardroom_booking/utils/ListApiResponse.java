package com.boardroom.boardroom_booking.utils;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListApiResponse {
	
	private boolean error;
	
	private List<String> message;

	private List<Object> result;
	
	private int totalPages;
	
	private int currentPage;

	public ListApiResponse() {
		super();
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}

	public List<Object> getResult() {
		return result;
	}

	public void setResult(List<Object> result) {
		this.result = result;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public String toString() {
		return "ListApiResponse [error=" + error + ", message=" + message + ", result=" + result + ", totalPages="
				+ totalPages + ", currentPage=" + currentPage + "]";
	}

}
