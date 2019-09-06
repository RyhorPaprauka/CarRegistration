package by.jnetworks.roadcamera.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class CarFilter {

    private static final Integer DEFAULT_LIMIT = 5;

    private String carNumber;
    private LocalDate date;
    private Integer limit;
    private Integer page;

    public String getCarNumber() {
        return carNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getLimit() {
        return limit == null ? DEFAULT_LIMIT : limit;
    }

    public Integer getPage() {
        return page == null ? 0 : page - 1;
    }
}
