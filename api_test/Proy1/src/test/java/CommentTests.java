import Model.Comment;
import Specifications.RequestSpecifications;
import Specifications.ResponseSpecifications;
import org.hamcrest.Matchers;
import org.hamcrest.text.MatchesPattern;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;

public class CommentTests extends Base{

    @Test(description = "This test aims to create a comment", groups = {"usePost"})
    public void createCommentTest()
    {
        Comment comment = new Comment("Some comment", "Some text");
        given()
                .spec(RequestSpecifications.useJWTBasicAuthentication())
                .body(comment)
                .when()
                .post("/v1/comment/" + postId)
                .then()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.equalTo("Comment created"));
    }

    @Test(description = "This test aims to create a comment without authorization", groups = {"usePost"})
    public void unauthorizedCreateCommentTest() {
        Comment comment = new Comment("Some comment", "Some text");
        given()
                .spec(RequestSpecifications.useFakeJWTAuthentication())
                .body(comment)
                .when()
                .post("/v1/comment/" + postId)
                .then()
                .spec(ResponseSpecifications.UnauthorizedResponse());
    }


    @Test(description = "This test aims to get all comments", groups = "useComment")
    public void getAllCommentsTest()
    {
        given()
                .spec(RequestSpecifications.useJWTBasicAuthentication())
                .when()
                .get("/v1/comments/" + postId)
                .then()
                .statusCode(200)
                .body("results[0].data[0].id", Matchers.equalTo(commentId));
    }


    @Test(description = "This test aims to get all comments without authorization")
    public void unauthorizedGetAllCommentsTest()
    {
        given()
                .spec(RequestSpecifications.useFakeJWTBasicAuthentication())
                .when()
                .get("/v1/comments/" + postId)
                .then()
                .spec(ResponseSpecifications.UnauthorizedResponse());
    }


    @Test(description = "This test aims to get one comment", groups = "useComment")
    public void getOneCommentTest()
    {
        given()
                .spec(RequestSpecifications.useJWTBasicAuthentication())
                .when()
                .get("/v1/comment/" + postId + "/" + commentId)
                .then()
                .statusCode(200)
                .body("data.id", Matchers.equalTo(commentId))
                .body("data.post_id", Matchers.equalTo(String.valueOf(postId)));
    }


    @Test(description = "This test aims to get one comment without authorization")
    public void unauthorizedGetOneCommentTest()
    {
        given()
                .spec(RequestSpecifications.useFakeJWTBasicAuthentication())
                .when()
                .get("/v1/comments/" + postId + "/" + commentId)
                .then()
                .statusCode(404)
                .body(Matchers.containsString("Opss!! 404 again?"));
    }


    @Test(description = "This test aims to get an invalid comment", groups = "useComment")
    public void invalidGetOneCommentTest()
    {
        given()
                .spec(RequestSpecifications.useJWTBasicAuthentication())
                .when()
                .get("/v1/comment/" + postId + "/" + (commentId * invalidNumber))
                .then()
                .statusCode(404)
                .body("Message", Matchers.equalTo("Comment not found"))
                .body("error", Matchers.equalTo("sql: no rows in result set"));
    }


    @Test(description = "This test aims to update a comment", groups = "useComment")
    public void updateCommentTest()
    {
        Comment commentTest = new Comment("Some comment", "Some text");
        given()
                .spec(RequestSpecifications.useJWTBasicAuthentication())
                .body(commentTest)
                .when()
                .put("/v1/comment/" + postId + "/" + commentId)
                .then()
                .spec(ResponseSpecifications.validatePositiveResponse());
    }


    @Test(description = "This test aims to update a comment without authorization", groups = "useComment")
    public void unauthorizedUpdateCommentTest()
    {
        Comment commentTest = new Comment("Some comment", "Some text");
        given()
                .spec(RequestSpecifications.useFakeJWTBasicAuthentication())
                .body(commentTest)
                .when()
                .put("/v1/comment/" + postId + "/" + commentId)
                .then()
                .spec(ResponseSpecifications.UnauthorizedResponse());
    }


    @Test(description = "This test aims to update an invalid comment", groups = "useComment")
    public void invalidUpdateCommentTest()
    {
        Comment commentTest = new Comment("Some comment", "Some text");
        given()
                .spec(RequestSpecifications.useJWTBasicAuthentication())
                .body(commentTest)
                .when()
                .put("/v1/comment/" + postId + "/" + (commentId * invalidNumber))
                .then()
                .statusCode(406)
                .body("message", Matchers.equalTo("Comment could not be updated"))
                .body("error", Matchers.equalTo("Comment not found"));
    }


    @Test(description = "This test aims to delete a comment", groups = "useComment")
    public void deleteCommentTest()
    {
        given()
                .spec(RequestSpecifications.useJWTBasicAuthentication())
                .when()
                .delete("/v1/comment/" + postId + "/" + commentId)
                .then()
                .spec(ResponseSpecifications.validatePositiveResponse());
    }


    @Test(description = "This test aims to delete a comment without authorization", groups = "useComment")
    public void unauthorizedDeleteCommentTest()
    {
        given()
                .spec(RequestSpecifications.useFakeJWTBasicAuthentication())
                .when()
                .delete("/v1/comment/" + postId + "/" + commentId)
                .then()
                .spec(ResponseSpecifications.UnauthorizedResponse());
    }


    @Test(description = "This test aims to delete an invalid comment", groups = "useComment")
    public void invalidDeleteCommentTest()
    {
        given()
                .spec(RequestSpecifications.useJWTBasicAuthentication())
                .when()
                .put("/v1/comment/" + postId + "/" + (commentId * invalidNumber))
                .then()
                .statusCode(406)
                .body("message", Matchers.equalTo("Invalid form"));
    }

}
