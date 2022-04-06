package io.github.eziomou.pm.service;

import io.github.eziomou.pm.repository.ProjectRepository;
import io.github.eziomou.pm.model.ProjectEntity;
import io.github.eziomou.pm.resource.Mapper;
import io.github.eziomou.pm.resource.PageResource;
import io.github.eziomou.pm.resource.ProjectResource;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    Mapper<ProjectEntity, ProjectResource> projectMapper;

    @ReactiveTransactional
    @Override
    public Uni<PageResource<ProjectResource>> getProjects(int pageIndex, int pageSize) {
        return Uni.combine()
                .all()
                .unis(projectRepository.findAll().page(Page.of(pageIndex, pageSize)).stream()
                                .onItem().transform(project -> projectMapper.map(project))
                                .collect().asList(),
                        projectRepository.findAll().count())
                .asTuple()
                .map(tuple -> new PageResource<>(tuple.getItem1(), new PageResource.Metadata(tuple.getItem2())));
    }
}
