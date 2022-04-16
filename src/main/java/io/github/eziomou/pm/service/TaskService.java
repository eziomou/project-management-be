package io.github.eziomou.pm.service;

import io.github.eziomou.pm.resource.PageResource;
import io.github.eziomou.pm.resource.TaskResource;
import io.smallrye.mutiny.Uni;

public interface TaskService {

    Uni<TaskResource> createTask(String name, String description, Long projectId);

    Uni<PageResource<TaskResource>> getTasks(Long projectId, int pageIndex, int pageSize);

    Uni<TaskResource> getTask(Long projectId, Long taskId);

    Uni<TaskResource> updateTask(Long projectId, Long taskId, String name, String description);

    Uni<Void> deleteTask(Long projectId, Long taskId);
}
