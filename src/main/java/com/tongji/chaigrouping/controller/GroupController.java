package com.tongji.chaigrouping.controller;


import com.tongji.chaigrouping.dto.*;
import com.tongji.chaigrouping.service.FindGroupService;
import com.tongji.chaigrouping.service.GroupMemberService;
import com.tongji.chaigrouping.service.GroupOperationService;
import com.tongji.chaigrouping.service.JoinRequestService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<UserGroupListDto>> getUserGroups(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        return ResponseEntity.ok(groupOpService.groupList(userId));
    }

    @GetMapping("/{group_id}")
    public ResponseEntity<Object> getGroupDetails(HttpServletRequest request,
                                                                  @PathVariable Integer group_id) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        try {
        return ResponseEntity.ok(groupOpService.groupDetail(userId, group_id));
        }
        catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createGroup(HttpServletRequest request,
                                                           @RequestBody GroupInfoDto groupInfoDto) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        return ResponseEntity.ok(groupOpService.createGroup(userId, groupInfoDto));
    }

    @PutMapping("/{group_id}")
    public ResponseEntity<Map<String, Object>> updateGroup(HttpServletRequest request,
                                                           @PathVariable Integer group_id,
                                                           @RequestBody GroupInfoDto groupInfoDto) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        return ResponseEntity.ok(groupOpService.updateGroup(userId, group_id, groupInfoDto));
    }

    @DeleteMapping("/{group_id}")
    public ResponseEntity<Object> disbandGroup(HttpServletRequest request,
                                              @PathVariable Integer group_id) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        try {
            return ResponseEntity.ok(groupOpService.disbandGroup(userId, group_id));
        }catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{group_id}/{member_id}")
    public ResponseEntity<Object> getMemberDetail(HttpServletRequest request,
                                            @PathVariable Integer group_id,
                                            @PathVariable Integer member_id) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        try {
            return ResponseEntity.ok(groupMemberService.queryGroupMember(userId, group_id, member_id));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/{group_id}/apply")
    public ResponseEntity<Object> applyJoinGroup(HttpServletRequest request,
                                                 @PathVariable Integer group_id,@RequestBody CreateRequestDto requestDto) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        if(requestDto.getDescription()==null)return ResponseEntity.status(400).body("申请理由不能为空。");
        try {
            joinRequestService.createRequest(userId, group_id, requestDto);
            return ResponseEntity.ok(Map.of("message", "已发送加入请求。"));
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/{join_request_id}/respond")
    public ResponseEntity<Object> respondJoinRequest(HttpServletRequest request,
                                                     @PathVariable Integer join_request_id,
                                                     @RequestBody RespondToRequestDto response) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        try {
            joinRequestService.respondToRequest(userId, join_request_id, response);
            return ResponseEntity.ok(Map.of("message", "已处理请求。"));
        }
        catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/{group_id}/leave")
    public ResponseEntity<Object> leaveGroup(HttpServletRequest request,
                                             @PathVariable Integer group_id) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        try {
            groupMemberService.quitGroup(userId, group_id);
            return ResponseEntity.ok(Map.of("message", "已退出小组。"));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @DeleteMapping("{group_id}/members/{user_id}/remove")
    public ResponseEntity<Object> removeMember(HttpServletRequest request,
                                               @PathVariable Integer group_id,
                                               @PathVariable Integer user_id) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        try {
            groupMemberService.kickMember(userId, group_id, user_id);
            return ResponseEntity.ok( Map.of("message", "已移除成员。"));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("{group_id}/leader_leave")
    public ResponseEntity<Object> transferLeader(HttpServletRequest request,
                                                 @PathVariable Integer group_id,
                                                 @RequestBody GroupTransferLeaderDto new_leader) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        try {
            groupMemberService.transferLeader(userId, group_id, new_leader.getNewLeaderId());
            groupMemberService.quitGroup(userId, group_id);
            return ResponseEntity.ok(Map.of("message", "已转让组长并退出群组。"));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<Object> filterGroup(HttpServletRequest request,
                                              @RequestBody GroupFilterDto groupFilterDto) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        return ResponseEntity.ok(findGroupService.filterGroup(userId,groupFilterDto));
    }

    @PostMapping("/match")
    public ResponseEntity<Object> matchGroup(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        return ResponseEntity.ok(findGroupService.findGroupByAI(userId));
    }
}