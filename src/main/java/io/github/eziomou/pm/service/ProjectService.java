package io.github.eziomou.pm.service;

import io.github.eziomou.pm.resource.PageResource;
import io.github.eziomou.pm.resource.ProjectResource;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

public interface ProjectService {

    Uni<ProjectResource> createProject(String name);

    Uni<PageResource<ProjectResource>> getProjects(int pageIndex, int pageSize);

    Uni<ProjectResource> getProject(Long projectId);

    Uni<ProjectResource> updateProject(Long projectId, String name);

    Uni<Void> deleteProject(Long projectId);
}
