package java51.forum.post.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DataPeriodDto {
    LocalDate dateFrom;
    LocalDate dateTo;
}
