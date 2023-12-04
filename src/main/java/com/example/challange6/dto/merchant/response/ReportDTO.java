package com.example.challange6.dto.merchant.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportDTO {
    private List<ReportItemDTO> Reported;
}
