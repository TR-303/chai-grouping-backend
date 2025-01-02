package com.tongji.chaigrouping.commonutils.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

@Data
@TableName("`group`")
public class Group {
    @TableId(type = AUTO)
    private Integer groupId;
    private String name;
    private String description;
    private Date creationDate;
    private Integer volume;
    private Integer visibility;
    private Integer approvalRequired;
    private Integer disbanded;
    private Integer leaderId;

}