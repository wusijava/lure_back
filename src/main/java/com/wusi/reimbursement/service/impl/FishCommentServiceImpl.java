package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.FishComment;
import com.wusi.reimbursement.entity.LureFishGet;
import com.wusi.reimbursement.entity.RequestContext;
import com.wusi.reimbursement.mapper.FishCommentMapper;
import com.wusi.reimbursement.service.FishCommentService;
import com.wusi.reimbursement.service.LureFishGetService;
import com.wusi.reimbursement.service.UserService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.vo.FishCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Java
 * @date 2024-04-23 09:05:19
 **/
@Service
public class FishCommentServiceImpl extends BaseMybatisServiceImpl<FishComment,Long> implements FishCommentService {

    @Autowired
    private FishCommentMapper fishCommentMapper;
    @Autowired
    private LureFishGetService lureFishGetService;
   // @Autowired
   // private UserService userService;


    @Override
    protected BaseMapper<FishComment, Long> getBaseMapper() {
        return fishCommentMapper;
    }

    @Override
    public List<FishCommentVo> queryComment(Long id) {
        if(DataUtil.isEmpty(id)){
            return null;
        }
        LureFishGet fishGet = lureFishGetService.queryById(id);
        if(DataUtil.isEmpty(fishGet)){
            return null;
        }
        List<FishComment> fishComments = fishCommentMapper.queryByFishIdAndType(fishGet.getId(),1);
        if(DataUtil.isEmpty(fishComments)){
            return null;
        }
        List<FishCommentVo> dataList=new ArrayList<>();
        for (FishComment fishComment : fishComments) {
            FishCommentVo vo=new FishCommentVo();
            vo.setId(fishComment.getId());
            vo.setComment(fishComment.getComment());
            vo.setFishId(fishComment.getFishId());
            vo.setName(fishComment.getCommentName());
            vo.setReply(getReply(fishComment.getId()));
            vo.setTime(new SimpleDateFormat("MM-dd HH:mm").format(fishComment.getCreateTime()));
            //vo.setImg(userService.findByUid(fishComment.getCommentUid()).getImg());
            dataList.add(vo);
        }
         return dataList;
    }

    @Override
    public void add(Long fishId, Long replyId, String comment) {
        FishComment data=new FishComment();
        data.setComment(comment);
        if(DataUtil.isEmpty(fishId)){
            data.setReplyId(replyId);
        }else{
            data.setFishId(fishId);
        }
        data.setComment(comment);
        data.setCreateTime(new Date());
        data.setCommentUid(RequestContext.getCurrentUser().getUid());
        data.setCommentName(RequestContext.getCurrentUser().getNickName());
        data.setType(1);
        fishCommentMapper.insert(data);
    }

    @Override
    public List<String> queryZan(Long id) {
        List<FishComment> fishComments = fishCommentMapper.queryByFishIdAndType(id, 2);
        return fishComments.stream().map(w->w.getCommentName()).distinct().collect(Collectors.toList());
    }

    @Override
    public void zan(Long id) {
        FishComment data=new FishComment();
        data.setCreateTime(new Date());
        data.setFishId(id);
        data.setCommentUid(RequestContext.getCurrentUser().getUid());
        data.setCommentName(RequestContext.getCurrentUser().getNickName());
        data.setType(2);
        fishCommentMapper.insert(data);
    }

    @Override
    public void cancelZan(Long id) {
        fishCommentMapper.cancelZan(id,RequestContext.getCurrentUser().getUid());
    }

    private List<FishCommentVo> getReply(Long id) {
        List<FishComment> fishComments = fishCommentMapper.queryByReplyId(id);
        if(DataUtil.isEmpty(fishComments)) {
            return new ArrayList<>();
        }
        List<FishCommentVo> dataList=new ArrayList<>();
        for (FishComment fishComment : fishComments) {
            FishCommentVo vo=new FishCommentVo();
            vo.setId(fishComment.getId());
            vo.setName(fishComment.getCommentName());
            vo.setComment(fishComment.getComment());
            vo.setReply(getReply(fishComment.getId()));
            vo.setTime(new SimpleDateFormat("MM-dd HH:mm").format(fishComment.getCreateTime()));
           // vo.setImg(userService.findByUid(fishComment.getCommentUid()).getImg());
            dataList.add(vo);
        }
        return dataList;
    }

}
