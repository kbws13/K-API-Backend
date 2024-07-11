package xyz.kbws;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.kbws.client.ApiClient;
import xyz.kbws.model.User;

/**
 * @author kbws
 * @date 2024/7/11
 * @description:
 */
public class ApiTest {

    public static void main(String[] args) {
        ApiClient apiClient = new ApiClient();
        System.out.println(apiClient.getNameByGet("kbws"));
        System.out.println(apiClient.getNameByPost("kbws"));
        User user = new User();
        user.setUserName("Eternal");
        System.out.println(apiClient.getUserNameByPost(user));
    }
}
