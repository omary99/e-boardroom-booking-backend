package com.boardroom.boardroom_booking.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListResponseWrapper extends DefaultRespData {
    private List<?> itemList;
    public int totalPage;
    public int numberOfElements;
    public int currentPage;
    public Boolean hasNext;
    public Boolean hasPrevious;
    public String sort;

    public void setResponse(Page<?> page) {
        this.itemList = page.getContent();
        this.currentPage = page.getNumber() + 1;
        this.numberOfElements = page.getNumberOfElements();
        this.totalPage = page.getTotalPages();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.sort = page.getSort().toString();
    }
}
