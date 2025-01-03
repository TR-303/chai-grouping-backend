package com.tongji.chaigrouping.commonutils.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

@Data
public class Notification {
    @TableId(type = AUTO)
    private Integer notificationId;
    private Integer userId;
    private Integer taskId;
    private String title;
    private String content;
    private Integer joinRequestId;
}
