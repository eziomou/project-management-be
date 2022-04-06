package io.github.eziomou.pm.service;

import io.github.eziomou.pm.resource.PageResource;
import io.github.eziomou.pm.resource.ProjectResource;
import io.smallrye.mutiny.Uni;

public interface ProjectService {

    Uni<PageResource<ProjectResource>> getProjects(int pageIndex, int pageSize);
}
