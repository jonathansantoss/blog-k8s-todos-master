package com.netshoes.blog.todos.http;

import com.netshoes.blog.todos.domains.Todo;
import com.netshoes.blog.todos.usecases.TodosCrud;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/todos")
@AllArgsConstructor
public class TodosController {

    private final TodosCrud todosCrud;

    @GetMapping
    private Collection<Todo> findAll() {
        return todosCrud.findAll();
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") final String id) {
        todosCrud.delete(id);
    }

    @PostMapping()
    public Todo create(@Valid @RequestBody Todo todo) {
        return todosCrud.create(todo);
    }

    @PutMapping(value = "/{id}")
    public Todo update(@PathVariable("id") final String id, @Valid @RequestBody Todo todo) {
        return todosCrud.update(id, todo);
    }
}
