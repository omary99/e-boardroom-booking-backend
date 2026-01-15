
package com.boardroom.boardroom_booking.utils;

import java.util.List;

public class ApiResponse {

    private boolean error;

    private List<String> message = null;

    private Object data = null;

    private int totalPages;

    private int currentPage;

    public boolean getError() {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

	@Override
	public String toString() {
		return "ApiResponse [error=" + error + ", message=" + message + ", data=" + data + ", totalPages=" + totalPages
				+ ", currentPage=" + currentPage + "]";
	}

   
}
