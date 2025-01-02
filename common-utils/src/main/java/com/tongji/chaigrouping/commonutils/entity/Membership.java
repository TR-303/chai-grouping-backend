package com.tongji.chaigrouping.commonutils.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

@Data
@AllArgsConstructor
public class Membership {

    private Integer groupId;

    private Integer userId;

    private Date joinDate;

}
