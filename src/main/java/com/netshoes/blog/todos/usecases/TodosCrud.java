package com.netshoes.blog.todos.usecases;

import com.google.common.collect.Lists;
import com.netshoes.blog.todos.domains.Todo;
import com.netshoes.blog.todos.exceptions.InvalidUserException;
import com.netshoes.blog.todos.exceptions.NotFoundException;
import com.netshoes.blog.todos.gateways.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
@AllArgsConstructor
public class TodosCrud {

    private final TodoRepository repository;
    private final CheckUserExists checkUserExists;

    public void delete(final String id) {
        repository.delete(findOne(id));
    }

    public Collection<Todo> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    public Todo findOne(final String id) {
        final Todo existing = repository.findOne(id);
        if (existing == null) {
            throw new NotFoundException();
        }
        return existing;
    }

    public Todo create(final Todo todo) {
        validateUser(todo.getUsername());
        todo.setId(UUID.randomUUID().toString());
        return repository.save(todo);
    }

    public Todo update(final String id, final Todo todo) {
        validateUser(todo.getUsername());
        final Todo existing = findOne(id);
        existing.setStatus(todo.getStatus());
        existing.setUsername(todo.getUsername());
        existing.setTask(todo.getTask());
        return repository.save(existing);
    }

    private void validateUser(final String username) {
        if (!checkUserExists.execute(username)) {
            throw new InvalidUserException();
        }
    }
}
