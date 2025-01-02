package com.tongji.chaigrouping.commonutils.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.chaigrouping.commonutils.dto.GroupMemberDetailDto;
import com.tongji.chaigrouping.commonutils.entity.Membership;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MembershipMapper extends BaseMapper<Membership> {
    /**
     * 查询群组中的所有成员（包括成员的详细信息）
     * @param groupId 群组 ID
     * @return 群组成员的详细信息列表
     */
    @Select("SELECT u.user_id AS userId, u.username, " +
            "IF(m.role = 1, 'leader', 'member') AS role, " +
            "m.join_date AS joinDate, u.school, u.grade, u.skill_description AS skillDescription " +
            "FROM membership m " +
            "JOIN users u ON m.user_id = u.user_id " +
            "WHERE m.group_id = #{groupId} and u.id = #{memberId}")
    GroupMemberDetailDto queryGroupMember(Integer groupId, Integer memberId);

    @Select("SELECT COUNT(*) FROM membership WHERE group_id = #{groupId} AND user_id = #{userId}")
    Boolean isMember(Integer groupId, Integer userId);
}
