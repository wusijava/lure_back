package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.FishComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Java
 * @date 2024-04-23 09:05:19
 **/
@Mapper
public interface FishCommentMapper extends BaseMapper<FishComment, Long> {

    List<FishComment> queryByFishIdAndType(Long fishId,Integer type);

    List<FishComment> queryByReplyId(Long id);

    void cancelZan(Long fishId, String uid);
}
