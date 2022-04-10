package io.github.eziomou.pm.resource;

import io.github.eziomou.pm.model.ProjectEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectMapper implements Mapper<ProjectEntity, ProjectResource> {

    @Override
    public ProjectResource map(ProjectEntity entity) {
        ProjectResource resource = new ProjectResource();
        resource.setId(entity.getId());
        resource.setName(entity.getName());
        resource.setCreatedAt(entity.getCreatedAt());
        return resource;
    }
}
