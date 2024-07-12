package xyz.kbws.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.kbws.model.entity.ApiKey;
import xyz.kbws.service.ApiKeyService;
import xyz.kbws.mapper.ApiKeyMapper;
import org.springframework.stereotype.Service;

/**
* @author hsy
* @description 针对表【api_key(API签名密钥表)】的数据库操作Service实现
* @createDate 2024-07-12 23:36:19
*/
@Service
public class ApiKeyServiceImpl extends ServiceImpl<ApiKeyMapper, ApiKey>
    implements ApiKeyService{

}




