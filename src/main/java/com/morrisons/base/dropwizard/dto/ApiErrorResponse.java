package com.morrisons.base.dropwizard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorResponse {
    private Integer httpResponseCode;
    private String errorCode;
    private String errorMessage;
}
