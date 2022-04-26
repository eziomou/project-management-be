package io.github.eziomou.pm.service;

import io.github.eziomou.pm.model.TaskEntity;
import io.github.eziomou.pm.repository.ProjectRepository;
import io.github.eziomou.pm.repository.TaskRepository;
import io.github.eziomou.pm.resource.Mapper;
import io.github.eziomou.pm.resource.PageResource;
import io.github.eziomou.pm.resource.TaskResource;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;

@ApplicationScoped
public class TaskServiceImpl implements TaskService {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    TaskRepository taskRepository;

    @Inject
    Mapper<TaskEntity, TaskResource> taskMapper;

    @ReactiveTransactional
    @Override
    public Uni<TaskResource> createTask(String name, String description, Long projectId) {
        return projectRepository.findById(projectId)
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().transform(project -> {
                    TaskEntity task = new TaskEntity();
                    task.setName(name);
                    task.setDescription(description);
                    task.setCreatedAt(LocalDateTime.now());
                    task.setProject(project);
                    return task;
                })
                .onItem().transformToUni(task -> taskRepository.persist(task))
                .onItem().transform(taskMapper::map);
    }

    @ReactiveTransactional
    @Override
    public Uni<PageResource<TaskResource>> getTasks(Long projectId, int pageIndex, int pageSize) {
        return Uni.combine()
                .all()
                .unis(taskRepository.find("project.id", projectId).page(Page.of(pageIndex, pageSize)).stream()
                                .onItem().transform(taskMapper::map)
                                .collect().asList(),
                        taskRepository.find("project.id", projectId).count())
                .asTuple()
                .map(tuple -> new PageResource<>(tuple.getItem1(), new PageResource.Metadata(tuple.getItem2())));
    }

    @ReactiveTransactional
    @Override
    public Uni<TaskResource> getTask(Long projectId, Long taskId) {
        return taskRepository.find("project.id = :projectId and id = :taskId",
                        Parameters.with("projectId", projectId).and("taskId", taskId).map()).firstResult()
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().transform(taskMapper::map);
    }

    @ReactiveTransactional
    @Override
    public Uni<TaskResource> updateTask(Long projectId, Long taskId, String name, String description) {
        return taskRepository.find("project.id = :projectId and id = :taskId",
                        Parameters.with("projectId", projectId).and("taskId", taskId).map()).firstResult()
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().invoke(task -> {
                    if (name != null) {
                        task.setName(name);
                    }
                    if (description != null) {
                        task.setDescription(description);
                    }
                })
                .onItem().transform(taskMapper::map);
    }

    @ReactiveTransactional
    @Override
    public Uni<Void> deleteTask(Long projectId, Long taskId) {
        return taskRepository.delete("project.id = :projectId and id = :taskId",
                        Parameters.with("projectId", projectId).and("taskId", taskId).map())
                .invoke(count -> {
                    if (count == 0) {
                        throw new NotFoundException();
                    }
                })
                .replaceWithVoid();
    }
}
