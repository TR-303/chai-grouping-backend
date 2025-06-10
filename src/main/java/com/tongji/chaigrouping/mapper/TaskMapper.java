package com.tongji.chaigrouping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.chaigrouping.dto.TaskDetailDto;
import com.tongji.chaigrouping.dto.TaskListItemDto;
import com.tongji.chaigrouping.entity.Task;
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