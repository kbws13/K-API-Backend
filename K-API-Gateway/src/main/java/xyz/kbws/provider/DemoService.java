package xyz.kbws.provider;

import java.util.concurrent.CompletableFuture;

/**
 * @author kbws
 * @date 2024/7/18
 * @description:
 */
public interface DemoService {
    String sayHello(String name);

    String sayHello2(String name);

    default CompletableFuture<String> sayHelloAsync(String name) {
        return CompletableFuture.completedFuture(sayHello(name));
    }
}
