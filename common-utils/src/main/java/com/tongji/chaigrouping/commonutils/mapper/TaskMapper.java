package com.tongji.chaigrouping.commonutils.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.chaigrouping.commonutils.dto.TaskDetailDto;
import com.tongji.chaigrouping.commonutils.dto.TaskListItemDto;
import com.tongji.chaigrouping.commonutils.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    TaskDetailDto getTaskDetailById(Integer taskId);

    List<TaskListItemDto> getGroupTaskList(Integer groupId);

    List<TaskListItemDto> getUserTaskList(Integer userId);

    @Select("select * from task where group_id = #{groupId}")
    List<Task> selectListByGroupId(Integer groupId);
}