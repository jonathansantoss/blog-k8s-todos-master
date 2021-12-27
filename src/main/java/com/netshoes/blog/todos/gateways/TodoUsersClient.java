package com.netshoes.blog.todos.gateways;

import com.netshoes.blog.todos.domains.User;
import feign.Param;
import feign.RequestLine;

import java.util.Optional;

public interface TodoUsersClient {
    @RequestLine("GET /{username}")
    Optional<User> getUser(@Param("username") String username);
}
