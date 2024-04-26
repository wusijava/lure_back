package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.FishComment;
import com.wusi.reimbursement.vo.FishCommentVo;

import java.util.List;

/**
 * @author Java
 * @date 2024-04-23 11:30:49
 **/
public interface FishCommentService extends BaseService<FishComment,Long> {

    List<FishCommentVo> queryComment(Long id);

    void add(Long fishId, Long replyId, String comment);

    List<String> queryZan(Long id);

    void zan(Long id);

    void cancelZan(Long id);
}
