package com.lyz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.common.lang.Result;
import com.lyz.entity.GroupComment;
import com.lyz.entity.GroupDiscussion;
import com.lyz.entity.GroupFile;
import com.lyz.entity.User;
import com.lyz.service.GroupCommentService;
import com.lyz.service.GroupDiscussionService;
import com.lyz.service.UserService;
import com.lyz.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyz
 * @since 2022-02-09
 */
@RestController
@RequestMapping("/group-discussion")
public class GroupDiscussionController {

    @Autowired
    GroupDiscussionService groupDiscussionService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;
    @Autowired
    GroupCommentService groupCommentService;

    @GetMapping("getDiscussion")
    public Result fetchDiscussion(int groupId, String title) {
        List<GroupDiscussion> groupDiscussionList;
        groupDiscussionList = groupDiscussionService.list(new QueryWrapper<GroupDiscussion>().eq("groupId", groupId).like("title", title));
        int total = groupDiscussionList.size();
        return Result.succ(MapUtil.builder()
                .put("total", total)
                .put("items", groupDiscussionList)
                .map());
    }

    @PostMapping("addDiscussion")
    public Result addDiscussion(@RequestHeader("X-Token") String token, @RequestBody GroupDiscussion groupDiscussion) {
        int userId = Integer.parseInt(jwtUtils.getClaimByToken(token).getSubject());
        groupDiscussion.setAddedDate(LocalDateTime.now());
        groupDiscussion.setUserId(userId);
        groupDiscussionService.saveOrUpdate(groupDiscussion);
        return Result.succ(groupDiscussion);
    }

    @PostMapping("deleteDiscussion")
    public Result deleteDiscussion(@RequestBody GroupDiscussion groupDiscussion) {
        groupDiscussionService.remove(new QueryWrapper<GroupDiscussion>().eq("id", groupDiscussion.getId()));
        return Result.succ(groupDiscussion);
    }

    @GetMapping("getDetail")
    public Result getDetail(int discussionId) {
        GroupDiscussion gd = groupDiscussionService.getById(discussionId);
        int uploaderId = gd.getUserId();
        User uploader = userService.getById(uploaderId);
        String name = uploader.getUsername();
        return Result.succ(MapUtil.builder()
                .put("item", gd)
                .put("uploader", name)
                .map());
    }

    @GetMapping("getComments")
    public Result getComments(int discussionId) {
        List<GroupComment> groupCommentList;
        groupCommentList = groupCommentService.list(new QueryWrapper<GroupComment>().eq("discussionId", discussionId));
        return Result.succ(MapUtil.builder()
                .put("comments", groupCommentList)
                .map());
    }

    @PostMapping("addComment")
    public Result addComment(@RequestHeader("X-Token") String token, @RequestBody GroupComment groupComment) {
        int userId = Integer.parseInt(jwtUtils.getClaimByToken(token).getSubject());
        User uploader = userService.getById(userId);
        String name = uploader.getUsername();
        groupComment.setAddedDate(LocalDateTime.now());
        groupComment.setUsername(name);
        groupCommentService.saveOrUpdate(groupComment);
        return Result.succ(groupComment);
    }

    @PostMapping("deleteComment")
    public Result deleteComment(int commentId) {
        groupCommentService.removeById(commentId);
        return Result.succ(commentId);
    }
}
