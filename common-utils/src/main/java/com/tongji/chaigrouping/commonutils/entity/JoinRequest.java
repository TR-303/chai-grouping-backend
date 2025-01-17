package com.tongji.chaigrouping.commonutils.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tongji.chaigrouping.commonutils.dto.JoinRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

@AllArgsConstructor
@Data
public class JoinRequest {
    @TableId(type = AUTO)
    private Integer joinRequestId;
    private Integer userId;
    private Integer groupId;
    private Date creationTime;
    private String description;
    private String state;
}