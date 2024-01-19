package com.modsen.passengerservice.util;

import com.modsen.passengerservice.dto.request.PassengerCreationRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;

@UtilityClass
public class RestAssuredClient {

    public Response sendGetByIdRequest(Long id) {
        return get("/" + id);
    }

    public Response sendCreationRequest(PassengerCreationRequest request) {
        return given()
                .request()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post();
    }

    public Response sendUpdateRequest(Long id, PassengerCreationRequest request) {
        return given()
                .request()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/" + id);
    }

    public Response sendBlockRequest(Long id) {
        return post("/" + id + "/block");
    }

    public Response sendGetAllRequest(Integer limit, Integer page, String field) {
        RequestSpecification request = given().request();

        if (limit != null) {
            request.param("limit", limit);
        }
        if (page != null) {
            request.param("page", page);
        }
        if (field != null) {
            request.param("field", field);
        }

        return request
                .when()
                .get();
    }

    public Response sendGetBlockedPassengersRequest() {
        return get("/blocked");
    }


}
