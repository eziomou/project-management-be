package io.github.eziomou.pm.endpoint;

import io.github.eziomou.pm.resource.PageResource;
import io.github.eziomou.pm.resource.ProjectResource;
import io.github.eziomou.pm.service.ProjectService;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/projects")
public class ProjectEndpoint {

    @Inject
    ProjectService projectService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<PageResource<ProjectResource>>> getProjects(@Valid @BeanParam PageRequest pageRequest) {
        return projectService.getProjects(pageRequest.getPage(), pageRequest.getSize()).map(RestResponse::ok);
    }
}
