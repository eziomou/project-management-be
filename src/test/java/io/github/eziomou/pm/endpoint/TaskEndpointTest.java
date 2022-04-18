package io.github.eziomou.pm.endpoint;

import io.github.eziomou.pm.security.Role;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class TaskEndpointTest extends BaseEndpointTest {

    @Test
    public void createTask_validTask() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 1)
                .contentType(ContentType.JSON)
                .body(new JsonObject()
                        .put("name", "Task name")
                        .put("description", "Task description")
                        .toString())
                .when().post("/projects/{projectId}/tasks")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("^.*?/projects/\\d+/tasks/\\d+$"));
    }

    @Test
    public void createTask_blankName() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 1)
                .contentType(ContentType.JSON)
                .body(new JsonObject().put("name", " ").toString())
                .when().post("/projects/{projectId}/tasks")
                .then()
                .statusCode(400);
    }

    @Test
    public void createTask_nullDescription() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 1)
                .contentType(ContentType.JSON)
                .body(new JsonObject()
                        .put("name", "Task name")
                        .put("description", null)
                        .toString())
                .when().post("/projects/{projectId}/tasks")
                .then()
                .statusCode(201);
    }

    @Test
    public void getTasks() {
        given()
                .pathParam("projectId", 1)
                .when().get("/projects/{projectId}/tasks")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("data[0].id", is(1))
                .body("data[0].name", is("Task 1"))
                .body("data[0].description", is("Task description 1"))
                .body("_metadata.totalCount", notNullValue());
    }

    @Test
    public void getTask() {
        given()
                .pathParam("projectId", 1)
                .pathParam("taskId", 1)
                .when().get("/projects/{projectId}/tasks/{taskId}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("id", is(1))
                .body("name", is("Task 1"))
                .body("description", is("Task description 1"))
                .body("createdAt", matchesRegex("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$"));
    }

    @Test
    public void getTask_notExisting() {
        given()
                .pathParam("projectId", 1)
                .pathParam("taskId", 100)
                .when().get("/projects/{projectId}/tasks/{taskId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void updateTask() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 1)
                .pathParam("taskId", 2)
                .contentType(ContentType.JSON)
                .body(new JsonObject()
                        .put("name", "Updated task name")
                        .put("description", "Updated task description")
                        .toString())
                .when().patch("/projects/{projectId}/tasks/{taskId}")
                .then()
                .statusCode(200);

        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 1)
                .pathParam("taskId", 2)
                .when().get("/projects/{projectId}/tasks/{taskId}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("name", is("Updated task name"))
                .body("description", is("Updated task description"));
    }
    @Test
    public void updateTask_notExisting() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 1)
                .pathParam("taskId", 100)
                .contentType(ContentType.JSON)
                .body(new JsonObject().put("name", "Updated task name").toString())
                .when().patch("/projects/{projectId}/tasks/{taskId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteTask() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 1)
                .pathParam("taskId", 3)
                .when().delete("/projects/{projectId}/tasks/{taskId}")
                .then()
                .statusCode(204);

        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 1)
                .pathParam("taskId", 3)
                .when().get("/projects/{projectId}/tasks/{taskId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteTask_notExisting() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 1)
                .pathParam("taskId", 100)
                .when().delete("/projects/{projectId}/tasks/{taskId}")
                .then()
                .statusCode(404);
    }
}
