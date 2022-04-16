package io.github.eziomou.pm.endpoint;

import io.github.eziomou.pm.resource.PageResource;
import io.github.eziomou.pm.resource.TaskResource;
import io.github.eziomou.pm.service.TaskService;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/projects")
public class TaskEndpoint {

    @Inject
    TaskService taskService;

    @POST
    @Path("/{projectId}/tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<?> createTask(@PathParam("projectId") Long projectId, @NotNull @Valid CreateTaskRequest request,
                             @Context UriInfo uriInfo) {
        return taskService.createTask(request.getName(), request.getDescription(), projectId)
                .map(task -> RestResponse.created(uriInfo.getAbsolutePathBuilder().path(task.getId().toString()).build()));
    }

    @GET
    @Path("/{projectId}/tasks")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<PageResource<TaskResource>>> getTasks(@PathParam("projectId") Long projectId,
                                                                             @Valid @BeanParam PageRequest pageRequest) {
        return taskService.getTasks(projectId, pageRequest.getPage(), pageRequest.getSize()).map(RestResponse::ok);
    }

    @GET
    @Path("/{projectId}/tasks/{taskId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<TaskResource>> getTask(@PathParam("projectId") Long projectId, @PathParam("taskId") Long taskId) {
        return taskService.getTask(projectId, taskId).map(RestResponse::ok);
    }

    @PATCH
    @Path("/{projectId}/tasks/{taskId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<?> updateTask(@PathParam("projectId") Long projectId, @PathParam("taskId") Long taskId,
                             @NotNull @Valid UpdateTaskRequest request) {
        return taskService.updateTask(projectId, taskId, request.getName(), request.getDescription());
    }

    @DELETE
    @Path("/{projectId}/tasks/{taskId}")
    public Uni<?> deleteTask(@PathParam("projectId") Long projectId, @PathParam("taskId") Long taskId) {
        return taskService.deleteTask(projectId, taskId).replaceWith(RestResponse::noContent);
    }
}
