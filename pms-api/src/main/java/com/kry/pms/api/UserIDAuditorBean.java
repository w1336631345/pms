package com.kry.pms.api;

import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.utils.ShiroUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 功能描述: <br>实现创建人（@CreatedBy）和修改人（@LastModifiedBy）
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2020/3/19 15:33
 */
@Configuration
public class UserIDAuditorBean  implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        User user = ShiroUtils.getUser();
        if(user == null){
            //此处不能return null，这样会报创建者不能为空的错误。返回empty就不会
            return Optional.empty();
        }else {
            return Optional.of(user.getId());
        }
    }
}
