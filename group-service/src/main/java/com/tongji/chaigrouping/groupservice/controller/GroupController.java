package com.tongji.chaigrouping.groupservice.controller;


import com.tongji.chaigrouping.commonutils.dto.*;
import com.tongji.chaigrouping.groupservice.service.FindGroupService;
import com.tongji.chaigrouping.groupservice.service.GroupMemberService;
import com.tongji.chaigrouping.groupservice.service.GroupOperationService;
import com.tongji.chaigrouping.groupservice.service.JoinRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupOperationService groupOpService;
    @Autowired
    private GroupMemberService groupMemberService;
    @Autowired
    private JoinRequestService joinRequestService;
    @Autowired
    private FindGroupService findGroupService;

    // 获取用户的组
    @GetMapping
    public ResponseEntity<List<UserGroupListDto>> getUserGroups(@RequestHeader("X-User-id") Integer userId) {
        return ResponseEntity.ok(groupOpService.groupList(userId));
    }

    @GetMapping("/{group_id}")
    public ResponseEntity<Object> getGroupDetails(@RequestHeader("X-User-id") Integer userId,
                                                                  @PathVariable Integer group_id) {
        try {
        return ResponseEntity.ok(groupOpService.groupDetail(userId, group_id));
        }
        catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createGroup(@RequestHeader("X-User-id") Integer userId,
                                                           @RequestBody GroupInfoDto groupInfoDto) {
        return ResponseEntity.ok(groupOpService.createGroup(userId, groupInfoDto));
    }

    @PutMapping("/{group_id}")
    public ResponseEntity<Map<String, Object>> updateGroup(@RequestHeader("X-User-id") Integer userId,
                                                           @PathVariable Integer group_id,
                                                           @RequestBody GroupInfoDto groupInfoDto) {
        return ResponseEntity.ok(groupOpService.updateGroup(userId, group_id, groupInfoDto));
    }

    @DeleteMapping("/{group_id}")
    public ResponseEntity<Object> disbandGroup(@RequestHeader("X-User-id") Integer userId,
                                              @PathVariable Integer group_id) {
        return ResponseEntity.ok(groupOpService.disbandGroup(userId, group_id));
    }

    @GetMapping("/{group_id}/{member_id}")
    public ResponseEntity<Object> getMemberDetail(@RequestHeader("X-User-id") Integer userId,
                                            @PathVariable Integer group_id,
                                            @PathVariable Integer member_id) {
        try {
            return ResponseEntity.ok(groupMemberService.queryGroupMember(userId, group_id, member_id));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/{group_id}/apply")
    public ResponseEntity<Object> applyJoinGroup(@RequestHeader("X-User-id") Integer userId,
                                                 @PathVariable Integer group_id,@RequestBody CreateRequestDto request) {
        try {
            joinRequestService.createRequest(userId, group_id, request);
            return ResponseEntity.ok(Map.of("message", "已发送加入请求。"));
        }
        catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/{join_request_id}/respond")
    public ResponseEntity<Object> respondJoinRequest(@RequestHeader("X-User-id") Integer userId,
                                                     @PathVariable Integer join_request_id,
                                                     @RequestBody RespondToRequestDto response) {
        try {
            joinRequestService.respondToRequest(userId, join_request_id, response);
            return ResponseEntity.ok(Map.of("message", "已处理请求。"));
        }
        catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/{group_id}/leave")
    public ResponseEntity<Object> leaveGroup(@RequestHeader("X-User-id") Integer userId,
                                             @PathVariable Integer group_id) {
        try {
            groupMemberService.quitGroup(userId, group_id);
            return ResponseEntity.ok(Map.of("message", "已退出小组。"));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @DeleteMapping("{group_id}/members/{user_id}/remove")
    public ResponseEntity<Object> removeMember(@RequestHeader("X-User-id") Integer userId,
                                               @PathVariable Integer group_id,
                                               @PathVariable Integer user_id) {
        try {
            groupMemberService.kickMember(userId, group_id, user_id);
            return ResponseEntity.ok( Map.of("message", "已移除成员。"));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("{group_id}/leader_leave")
    public ResponseEntity<Object> transferLeader(@RequestHeader("X-User-id") Integer userId,
                                                 @PathVariable Integer group_id,
                                                 @RequestBody GroupTransferLeaderDto new_leader) {
        try {
            groupMemberService.transferLeader(userId, group_id, new_leader.getNewLeaderId());
            groupMemberService.quitGroup(userId, group_id);
            return ResponseEntity.ok(Map.of("message", "已转让组长并退出群组。"));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<Object> filterGroup(@RequestHeader("X-User-id") Integer userId,
                                              @RequestBody GroupFilterDto groupFilterDto) {
        return ResponseEntity.ok(findGroupService.filterGroup(userId,groupFilterDto));
    }

    @PostMapping("/match")
    public ResponseEntity<Object> matchGroup(@RequestHeader("X-User-id") Integer userId) {
        return ResponseEntity.ok(findGroupService.findGroupByAI(userId));
    }
}
