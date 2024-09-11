package com.sku.fitizen.mapper.challenge;

import com.sku.fitizen.domain.ChallComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface challCommentMapper {


    List<ChallComment> challCommentList(Integer challlengeId);


}
