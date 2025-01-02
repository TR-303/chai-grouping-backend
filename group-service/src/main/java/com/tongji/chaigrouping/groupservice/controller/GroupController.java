package com.tongji.chaigrouping.groupservice.controller;


import com.tongji.chaigrouping.commonutils.dto.GroupDetailResponseDto;
import com.tongji.chaigrouping.commonutils.dto.GroupInfoDto;
import com.tongji.chaigrouping.commonutils.dto.UserGroupListDto;
import com.tongji.chaigrouping.groupservice.service.GroupOperationService;
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

    // 获取用户的组
    @GetMapping
    public ResponseEntity<List<UserGroupListDto>> getUserGroups(@RequestHeader("X-User-id") Integer userId) {
        return ResponseEntity.ok(groupOpService.groupList(userId));
    }

    @GetMapping("/{group_id}")
    public ResponseEntity<GroupDetailResponseDto> getGroupDetails(@RequestHeader("X-User-id") Integer userId,
                                                                  @PathVariable Integer group_id) {
        return ResponseEntity.ok(groupOpService.groupDetail(userId, group_id));
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
}
