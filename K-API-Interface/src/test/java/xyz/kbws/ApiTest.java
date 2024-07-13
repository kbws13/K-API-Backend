package xyz.kbws;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.kbws.client.KApiClient;
import xyz.kbws.model.User;

import javax.annotation.Resource;

/**
 * @author kbws
 * @date 2024/7/11
 * @description:
 */
@SpringBootTest
public class ApiTest {

    @Resource
    private KApiClient kApiClient;

    @Test
    public void test01() {
        String name = kApiClient.getNameByGet("Eternal");
        User user = new User();
        user.setUserName("hsy");
        String userNameByPost = kApiClient.getUserNameByPost(user);
        System.out.println(userNameByPost);
    }
}
