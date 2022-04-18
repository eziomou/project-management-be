package io.github.eziomou.pm.endpoint;

import io.github.eziomou.pm.resource.PageResource;
import io.github.eziomou.pm.resource.ProjectResource;
import io.github.eziomou.pm.security.Role;
import io.github.eziomou.pm.service.ProjectService;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/projects")
public class ProjectEndpoint {

    @Inject
    ProjectService projectService;

    @RolesAllowed(Role.USER)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<?> createProject(@NotNull @Valid CreateProjectRequest request, @Context UriInfo uriInfo) {
        return projectService.createProject(request.getName())
                .map(project -> RestResponse.created(uriInfo.getAbsolutePathBuilder().path(project.getId().toString()).build()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<PageResource<ProjectResource>>> getProjects(@Valid @BeanParam PageRequest pageRequest) {
        return projectService.getProjects(pageRequest.getPage(), pageRequest.getSize()).map(RestResponse::ok);
    }

    @GET
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<ProjectResource>> getProject(@PathParam("projectId") Long projectId) {
        return projectService.getProject(projectId).map(RestResponse::ok);
    }

    @RolesAllowed(Role.USER)
    @PATCH
    @Path("/{projectId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<?> updateProject(@PathParam("projectId") Long projectId, @NotNull @Valid UpdateProjectRequest request) {
        return projectService.updateProject(projectId, request.getName(), request.getDescription());
    }

    @RolesAllowed(Role.USER)
    @DELETE
    @Path("/{projectId}")
    public Uni<?> deleteProject(@PathParam("projectId") Long projectId) {
        return projectService.deleteProject(projectId)
                .replaceWith(RestResponse::noContent);
    }
}
