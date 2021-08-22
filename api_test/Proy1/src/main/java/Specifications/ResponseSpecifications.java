package Specifications;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;

public class ResponseSpecifications {
    public static ResponseSpecification validatePositiveResponse()
    {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(200);
        builder.expectBody("message", Matchers.notNullValue());
        builder.expectContentType("application/json");
        return builder.build();
    }

    public static ResponseSpecification UnauthorizedResponse()
    {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(401);
        builder.expectBody("message", Matchers.equalTo("Please login first"));
        return builder.build();
    }
}
