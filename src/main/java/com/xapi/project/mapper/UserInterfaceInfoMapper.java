package com.xapi.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xapi.xapicommon.model.entity.UserInterfaceInfo;
import java.util.List;

/**
* @author 15304
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
*
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    //select interfaceInfoId, sum(totalNum) as totalNum
    //from user_interface_info
    //group by interfaceInfoId
    //order by totalNum desc
    //limit 3;
    List<UserInterfaceInfo> listTopInvokeInterface(int limit);
}




