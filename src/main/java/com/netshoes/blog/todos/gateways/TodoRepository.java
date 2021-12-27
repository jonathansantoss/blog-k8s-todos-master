package com.netshoes.blog.todos.gateways;

import com.netshoes.blog.todos.domains.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<Todo, String> {
}
