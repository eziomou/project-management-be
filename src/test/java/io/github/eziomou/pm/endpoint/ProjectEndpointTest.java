package io.github.eziomou.pm.endpoint;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.matchesRegex;

@QuarkusTest
public class ProjectEndpointTest {

    @Test
    public void createProject() {
        given()
                .contentType(ContentType.JSON)
                .body(new JsonObject().put("name", "Project name").toString())
                .when().post("/projects")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex("^.*?/projects/\\d+$"));
    }

    @Test
    public void createProject_blankName() {
        given()
                .contentType(ContentType.JSON)
                .body(new JsonObject().put("name", " ").toString())
                .when().post("/projects")
                .then()
                .statusCode(400);
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
                .body("data[1].id", is(2))
                .body("data[1].name", is("Project 2"))
                .body("data[2].id", is(3))
                .body("data[2].name", is("Project 3"))
                .body("_metadata.totalCount", is(40));
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
                .body("name", is("Project 1"));
    }

    @Test
    public void getProject_notExisting() {
        given()
                .pathParam("projectId", 100)
                .when().get("/projects/{projectId}")
                .then()
                .statusCode(404);
    }
}
