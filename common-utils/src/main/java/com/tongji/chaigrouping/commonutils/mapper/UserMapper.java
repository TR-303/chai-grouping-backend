package com.tongji.chaigrouping.commonutils.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.chaigrouping.commonutils.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
