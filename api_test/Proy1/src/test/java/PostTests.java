import Model.Post;
import Specifications.RequestSpecifications;
import Specifications.ResponseSpecifications;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;

public class PostTests extends Base{

    @Test(description = "This test aims to create a post")
    public void createPostTest()
    {
        Post postTest = new Post("post creation", "additional information");
        given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .body(postTest)
                .when()
                .post("/v1/post")
                .then()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.equalTo("Post created"));
    }

    @Test(description = "This test aims to create an unauthorized post")
    public void unauthorizedCreatePostTest()
    {
        Post postTest = new Post("post creation", "additional information");
        given()
                .spec(RequestSpecifications.useFakeJWTAuthentication())
                .body(postTest)
                .when()
                .post("/v1/post")
                .then()
                .spec(ResponseSpecifications.UnauthorizedResponse());
    }

    @Test(description = "This test aims get all posts", groups = "usePost")
    public void getAllPostsTest()
    {
        given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/posts")
                .then()
                .statusCode(200)
                .body("results[0].data[0].id", Matchers.equalTo(postId));
    }

    @Test(description = "This test aims get all posts without authorization")
    public void unauthorizedGetAllPostsTest()
    {
        given()
                .spec(RequestSpecifications.useFakeJWTAuthentication())
                .when()
                .get("/v1/posts")
                .then()
                .spec(ResponseSpecifications.UnauthorizedResponse());
    }

    @Test(description = "This test aims get one post", groups = "usePost")
    public void getOnePostTest()
    {
        given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/post/" + postId)
                .then()
                .statusCode(200)
                .body("data.id", Matchers.equalTo(postId));
    }

    @Test(description = "This test aims get one post without authorization", groups = "usePost")
    public void unauthorizedGetOnePostTest()
    {
        given()
                .spec(RequestSpecifications.useFakeJWTAuthentication())
                .when()
                .get("/v1/post/" + postId)
                .then()
                .spec(ResponseSpecifications.UnauthorizedResponse());
    }

    @Test(description = "This test aims get an invalid post", groups = "usePost")
    public void invalidGetOnePostTest()
    {
        given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/post/" + (postId * invalidNumber))
                .then()
                .statusCode(404)
                .body("Message", Matchers.equalTo("Post not found"))
                .body("error", Matchers.equalTo("sql: no rows in result set"));
    }

    @Test(description = "This test aims update a post", groups = "usePost")
    public void updatePostTest()
    {
        Post postTest = new Post("Some post", "Some text");
        given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .body(postTest)
                .when()
                .put("/v1/post/" + postId)
                .then()
                .statusCode(200)
                .body("message", Matchers.equalTo("Post updated"));
    }

    @Test(description = "This test aims update a post without authorization", groups = "usePost")
    public void unauthorizedUpdatePostTest()
    {
        Post postTest = new Post("Some post", "Some text");
        given()
                .spec(RequestSpecifications.useFakeJWTAuthentication())
                .body(postTest)
                .when()
                .put("/v1/post/" + postId)
                .then()
                .spec(ResponseSpecifications.UnauthorizedResponse());
    }

    @Test(description = "This test aims update an invalid post", groups = "usePost")
    public void invalidUpdatePostTest()
    {
        Post postTest = new Post("Some post", "Some text");
        given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .body(postTest)
                .when()
                .put("/v1/post/" + (postId * invalidNumber))
                .then()
                .statusCode(406)
                .body("message", Matchers.equalTo("Post could not be updated"))
                .body("error", Matchers.equalTo("Post not found"));
    }

    @Test(description = "This test aims delete a post", groups = "singleUsePost")
    public void deletePostTest()
    {
        given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .delete("/v1/post/" + postId)
                .then()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.equalTo("Post deleted"));
    }
}
