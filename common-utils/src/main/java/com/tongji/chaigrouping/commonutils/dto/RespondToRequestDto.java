package com.tongji.chaigrouping.commonutils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespondToRequestDto {
    String action; // APPROVE, REJECT
}
