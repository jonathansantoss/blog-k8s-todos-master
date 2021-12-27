package com.netshoes.blog.todos.usecases;

import com.netshoes.blog.todos.gateways.TodoUsersClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CheckUserExists {

    private final TodoUsersClient todoUsersClient;

    public Boolean execute(final String username) {
        return todoUsersClient.getUser(username).isPresent();
    }

}
