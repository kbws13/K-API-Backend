package xyz.kbws.mapper;

import xyz.kbws.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author hsy
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
 * @createDate 2024-07-14 17:08:16
 * @Entity xyz.kbws.model.entity.UserInterfaceInfo
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}




