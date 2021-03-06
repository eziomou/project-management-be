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
public class ProjectEndpointTest extends BaseEndpointTest {

    @Test
    public void createProject_validProject() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .contentType(ContentType.JSON)
                .body(new JsonObject()
                        .put("name", "Project name")
                        .put("description", "Project description")
                        .toString())
                .when().post("/projects")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("^.*?/projects/\\d+$"));
    }

    @Test
    public void createProject_blankName() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .contentType(ContentType.JSON)
                .body(new JsonObject().put("name", " ").toString())
                .when().post("/projects")
                .then()
                .statusCode(400);
    }

    @Test
    public void createProject_nullDescription() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .contentType(ContentType.JSON)
                .body(new JsonObject()
                        .put("name", "Project name")
                        .put("description", null)
                        .toString())
                .when().post("/projects")
                .then()
                .statusCode(201);
    }

    @Test
    public void getProjects() {
        given()
                .when().get("/projects")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("data[0].id", is(1))
                .body("data[0].name", is("Project 1"))
                .body("data[0].description", is("Project description 1"))
                .body("data[1].id", is(2))
                .body("data[1].name", is("Project 2"))
                .body("data[1].description", is("Project description 2"))
                .body("data[2].id", is(3))
                .body("data[2].name", is("Project 3"))
                .body("data[2].description", is("Project description 3"))
                .body("_metadata.totalCount", notNullValue());
    }

    @Test
    public void getProject() {
        given()
                .pathParam("projectId", 1)
                .when().get("/projects/{projectId}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("id", is(1))
                .body("name", is("Project 1"))
                .body("description", is("Project description 1"))
                .body("createdAt", matchesRegex("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$"));
    }

    @Test
    public void getProject_notExisting() {
        given()
                .pathParam("projectId", 100)
                .when().get("/projects/{projectId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void updateProject() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 20)
                .contentType(ContentType.JSON)
                .body(new JsonObject()
                        .put("name", "Updated project name")
                        .put("description", "Updated project description")
                        .toString())
                .when().patch("/projects/{projectId}")
                .then()
                .statusCode(200);

        given()
                .pathParam("projectId", 20)
                .when().get("/projects/{projectId}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("name", is("Updated project name"))
                .body("description", is("Updated project description"));
    }

    @Test
    public void updateProject_notExisting() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 100)
                .contentType(ContentType.JSON)
                .body(new JsonObject().put("name", "Updated project name").toString())
                .when().patch("/projects/{projectId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteProject() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 40)
                .when().delete("/projects/{projectId}")
                .then()
                .statusCode(204);

        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 40)
                .when().get("/projects/{projectId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteProject_notExisting() {
        given()
                .auth().oauth2(getAccessToken("alice", Role.USER))
                .pathParam("projectId", 100)
                .when().delete("/projects/{projectId}")
                .then()
                .statusCode(404);
    }
}
