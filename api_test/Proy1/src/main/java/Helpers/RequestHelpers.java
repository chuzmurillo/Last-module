package Helpers;

import Model.Comment;
import Model.Post;
import Specifications.RequestSpecifications;
import Specifications.ResponseSpecifications;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import Model.User;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;

public class RequestHelpers {

    public static String TOKEN = "";

    //   get a json token from the login request
    public static String getAuthToken () {

        User testUser = new User(
                "juan@jose.com",
                "Juan Perez",
                "password");

        Response response = given().body(testUser).post("/v1/user/login");
        JsonPath jsonPath = response.jsonPath();
        TOKEN = jsonPath.get("token.access_token");
        return TOKEN;
    }

    public static int createRandomPostAndGetID () {
        Post randomPost = new Post("Some post", "Lorem impusn");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(randomPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get("id");
    }

    public static int createRandomCommentAndGetID (int postId) {
        Comment comment = new Comment("Some comment", "Some text");
        Response response = given().spec(RequestSpecifications.useJWTBasicAuthentication())
                .body(comment)
                .when()
                .post("/v1/comment/" + postId);

        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get("id");
    }

    public static void cleanUpPost (int id) {
        given().spec(RequestSpecifications.useJWTAuthentication())
                .delete("/v1/post/"+ id);
    }

    public static void cleanUpComment (int id, int comment) {
        given().spec(RequestSpecifications.useJWTBasicAuthentication())
                .delete("/v1/comment/" + String.valueOf(id) + "/" + String.valueOf(comment));
    }

}
