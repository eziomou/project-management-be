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
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    Mapper<ProjectEntity, ProjectResource> projectMapper;

    @ReactiveTransactional
    @Override
    public Uni<ProjectResource> createProject(String name, String description) {
        return Uni.createFrom().item(() -> {
                    ProjectEntity project = new ProjectEntity();
                    project.setName(name);
                    project.setDescription(description);
                    project.setCreatedAt(LocalDateTime.now());
                    return project;
                })
                .onItem().transformToUni(project -> projectRepository.persist(project))
                .onItem().transform(projectMapper::map);
    }

    @ReactiveTransactional
    @Override
    public Uni<PageResource<ProjectResource>> getProjects(int pageIndex, int pageSize) {
        return Uni.combine()
                .all()
                .unis(projectRepository.findAll().page(Page.of(pageIndex, pageSize)).stream()
                                .onItem().transform(projectMapper::map)
                                .collect().asList(),
                        projectRepository.findAll().count())
                .asTuple()
                .map(tuple -> new PageResource<>(tuple.getItem1(), new PageResource.Metadata(tuple.getItem2())));
    }

    @ReactiveTransactional
    @Override
    public Uni<ProjectResource> getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().transform(projectMapper::map);
    }

    @ReactiveTransactional
    @Override
    public Uni<ProjectResource> updateProject(Long projectId, String name, String description) {
        return projectRepository.findById(projectId)
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().invoke(project -> {
                    if (name != null) {
                        project.setName(name);
                    }
                    if (description != null) {
                        project.setDescription(description);
                    }
                })
                .onItem().transform(projectMapper::map);
    }

    @ReactiveTransactional
    @Override
    public Uni<Void> deleteProject(Long projectId) {
        return projectRepository.deleteById(projectId)
                .invoke(deleted -> {
                    if (!deleted) {
                        throw new NotFoundException();
                    }
                })
                .replaceWithVoid();
    }
}
