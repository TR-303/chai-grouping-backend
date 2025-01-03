package com.tongji.chaigrouping.commonutils.mapper;
import com.tongji.chaigrouping.commonutils.dto.GroupListDto;
import com.tongji.chaigrouping.commonutils.dto.GroupFilterDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.chaigrouping.commonutils.dto.*;
import com.tongji.chaigrouping.commonutils.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {

    @Select("""
    SELECT
        g.group_id AS groupId,
        g.name,
        CASE WHEN g.leader_id = #{userId} THEN TRUE ELSE FALSE END AS isLeader,
        COUNT(m2.user_id) AS memberCount,
        g.volume,
        m.join_date AS joinDate
    FROM `group` g
    JOIN `membership` m ON g.group_id = m.group_id
    LEFT JOIN `membership` m2 ON g.group_id = m2.group_id
    WHERE m.user_id = #{userId} AND g.disbanded = 0
    GROUP BY g.group_id, g.name, g.leader_id, g.volume, m.join_date
    order by m.join_date desc
    """)
    List<UserGroupListDto> getGroupList(@Param("userId") Integer userId);

    @Select("""
        SELECT 
            g.group_id AS groupId,
            g.name,
            g.description,
            g.volume,
            g.visibility AS visibility
        FROM `group` g
        WHERE g.group_id = #{groupId} AND g.disbanded = 0
    """)
    GroupDetailResponseDto getGroupDetail(@Param("groupId") Integer groupId);

    @Select("""
        SELECT 
            m.user_id AS userId,
            u.username,
            CASE WHEN g.leader_id = m.user_id THEN TRUE ELSE FALSE END AS isLeader
        FROM `membership` m
        JOIN `user` u ON m.user_id = u.user_id
        JOIN `group` g ON m.group_id = g.group_id
        WHERE m.group_id = #{groupId} AND g.disbanded = 0
        order by isLeader desc
    """)
    List<GroupMemberBriefDto> getGroupMembers(@Param("groupId") Integer groupId);

    @Select("""
        SELECT 
            g.leader_id
        FROM `group` g
        WHERE g.group_id = #{groupId}
    """)
    Integer getGroupLeaderId(Integer groupId);

    @Select("""
        SELECT 
            COUNT(m.user_id)
        FROM `membership` m
        WHERE m.group_id = #{groupId}
    """)
    Integer getMemberCount(Integer groupId);

    /**
     * 更新群组信息
     *
     * @param groupId            群组 ID
     * @param name               群组名称
     * @param description        群组描述
     * @param volume             群组容量
     * @param visibility         群组可见性
     * @param approvalRequired   是否需要审批
     * @return 受影响的行数
     */
    @Update("""
        UPDATE `group` 
        SET 
            name = #{name}, 
            description = #{description}, 
            volume = #{volume}, 
            visibility = #{visibility}, 
            approval_required = #{approvalRequired}
        WHERE 
            group_id = #{groupId} AND 
            disbanded = 0
    """)
    int updateGroupByDtoInfo(@Param("groupId") Integer groupId,
                             @Param("name") String name,
                             @Param("description") String description,
                             @Param("volume") Integer volume,
                             @Param("visibility") Integer visibility,
                             @Param("approvalRequired") Integer approvalRequired);


    /**
     * 查询所有可见的群组
     * @return 可见群组的详细信息
     */
    @Select("SELECT group_id AS groupId, name, description, volume, visibility, approval_required AS approvalRequired " +
            "FROM `group` WHERE visibility = 1 && `group`.disbanded = 0 and #{userId} not in (select user_id from membership where group_id = `group`.group_id)")
    List<GroupListDto> findVisibleGroups(Integer userId);

    @Select("""
        SELECT group_id AS groupId, name, description, volume, visibility, approval_required AS approvalRequired
        FROM `group`
        WHERE visibility = 1 AND disbanded = 0 AND #{userId} NOT IN (SELECT user_id FROM membership WHERE group_id = `group`.group_id)
        AND name LIKE CONCAT('%', #{keyword}, '%')
    """)
    List<GroupListDto> matchVisibleGroups(@Param("userId") Integer userId, @Param("keyword") String keyword);

    @Select("""
        SELECT #{userId} = g.leader_id
        FROM `group` g
        WHERE group_id = #{groupId}
    """)
    Boolean isLeader(Integer groupId, Integer userId);

    List<GroupListDto> filterGroup(@Param("userId") Integer userId,@Param("filter")  GroupFilterDto filter);
}
