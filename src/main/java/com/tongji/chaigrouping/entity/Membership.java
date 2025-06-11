package com.tongji.chaigrouping.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Membership {

    private Integer groupId;

    private Integer userId;

    private Date joinDate;

}
