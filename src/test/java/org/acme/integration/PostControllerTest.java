package org.acme.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.MediaType;

import org.acme.dtos.input.PostInputDTO;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class PostControllerTest {

    @Test
    public void testFindAllEndpoint() {
        RestAssured.given()
          .when().get("/posts")
          .then()
             .statusCode(200);
    }

    @Test
    public void testSaveEndpoint() {
        PostInputDTO dto = PostInputDTO.builder()
                           .content("test")
                           .title("teste")
                           .build();
        RestAssured.given()
          .contentType(MediaType.APPLICATION_JSON)
          .body(dto)
          .when().post("/posts")
          .then()
             .statusCode(201);
    }
}