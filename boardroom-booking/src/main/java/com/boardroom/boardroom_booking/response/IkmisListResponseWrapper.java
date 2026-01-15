package com.boardroom.boardroom_booking.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IkmisListResponseWrapper extends IkmisDefaultRespData {
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

    public void setAdditionalInfo(Map<String, BigDecimal> percentageChange) {
    }

    public void setResponseWithoutPaging(List<?> list){
        this.itemList = list;
    }
}
