package com.tongji.chaigrouping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

@AllArgsConstructor
@Data
public class Notification {
    @TableId(type = AUTO)
    private Integer notificationId;
    private Integer userId;
    private String title;
    private String content;
    private Integer joinRequestId;
    private Integer hasRead;
    private Date creationTime;
}
