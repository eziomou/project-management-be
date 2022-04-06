package io.github.eziomou.pm.repository;

import io.github.eziomou.pm.model.ProjectEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectRepository implements PanacheRepository<ProjectEntity> {
}
