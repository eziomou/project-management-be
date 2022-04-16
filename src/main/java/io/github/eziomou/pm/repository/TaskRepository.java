package io.github.eziomou.pm.repository;

import io.github.eziomou.pm.model.TaskEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<TaskEntity> {
}
