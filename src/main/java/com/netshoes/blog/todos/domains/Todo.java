package com.netshoes.blog.todos.domains;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@RedisHash("Todo")
public class Todo implements Serializable {
    public enum Status {PENDING, COMPLETED}

    private String id;

    @NotBlank
    private String task;

    @NotBlank
    private String username;

    @NotNull
    private Status status = Status.PENDING;
}
