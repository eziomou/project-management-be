package io.github.eziomou.pm.endpoint;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ProjectEndpointTest {

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
                .body("_metadata.totalCount", is(3));
    }
}
