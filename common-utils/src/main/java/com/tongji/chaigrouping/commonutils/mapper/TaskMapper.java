package com.tongji.chaigrouping.commonutils.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.chaigrouping.commonutils.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    @Select("select * from task where group_id = #{groupId}")
    @Results({
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "groupId", column = "group_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "state", column = "state"),
            @Result(property = "deadline", column = "deadline"),
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.tongji.chaigrouping.commonutils.mapper.UserMapper.selectById")),
            @Result(property = "group", column = "group_id",
                    one = @One(select = "com.tongji.chaigrouping.commonutils.mapper.GroupMapper.selectById")),
            @Result(property = "submissions", column = "task_id",
                    many = @Many(select = "com.tongji.chaigrouping.commonutils.mapper.SubmissionMapper.selectByTaskId"))
    })
    List<Task> selectListByGroupId(Integer groupId);

    @Select("select * from task where task_id = #{taskId}")
    @Results({
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "groupId", column = "group_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "state", column = "state"),
            @Result(property = "deadline", column = "deadline"),
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.tongji.chaigrouping.commonutils.mapper.UserMapper.selectById")),
            @Result(property = "submissions", column = "task_id",
                    many = @Many(select = "com.tongji.chaigrouping.commonutils.mapper.SubmissionMapper.selectByTaskId")),
            @Result(property = "group", column = "group_id",
                    one = @One(select = "com.tongji.chaigrouping.commonutils.mapper.GroupMapper.selectById"))
    })
    Task selectDetailById(Integer taskId);

    @Select("select * from task where user_id = #{userId}")
    @Results({
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "groupId", column = "group_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "state", column = "state"),
            @Result(property = "deadline", column = "deadline"),
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.tongji.chaigrouping.commonutils.mapper.UserMapper.selectById")),
            @Result(property = "group", column = "group_id",
                    one = @One(select = "com.tongji.chaigrouping.commonutils.mapper.GroupMapper.selectById")),
            @Result(property = "submissions", column = "task_id",
                    many = @Many(select = "com.tongji.chaigrouping.commonutils.mapper.SubmissionMapper.selectByTaskId"))
    })
    List<Task> selectListByUserId(Integer userId);
}