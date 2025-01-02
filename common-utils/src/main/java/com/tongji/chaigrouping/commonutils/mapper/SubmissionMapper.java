package com.tongji.chaigrouping.commonutils.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.chaigrouping.commonutils.entity.Submission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubmissionMapper extends BaseMapper<Submission> {
    @Select("SELECT s.*, u.* FROM submission s " +
            "JOIN user u ON s.user_id = u.user_id " +
            "WHERE s.task_id = #{taskId}")
    List<Submission> selectByTaskId(Integer taskId);

}