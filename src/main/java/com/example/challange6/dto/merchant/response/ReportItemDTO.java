package com.example.challange6.dto.merchant.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemDTO {
    private int weekNumber;
    private int monthNumber;
    private double totalIncome;
    private List<OrderDTO> orders;
}