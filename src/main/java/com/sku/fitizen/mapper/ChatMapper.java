package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.Message;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ChatMapper {


   int saveMessage(Message message);


}
