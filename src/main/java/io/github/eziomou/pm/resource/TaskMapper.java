package io.github.eziomou.pm.resource;

import io.github.eziomou.pm.model.TaskEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskMapper implements Mapper<TaskEntity, TaskResource> {

    @Override
    public TaskResource map(TaskEntity entity) {
        TaskResource resource = new TaskResource();
        resource.setId(entity.getId());
        resource.setName(entity.getName());
        resource.setDescription(entity.getDescription());
        resource.setCreatedAt(entity.getCreatedAt());
        resource.setProjectId(entity.getProject().getId());
        return resource;
    }
}
