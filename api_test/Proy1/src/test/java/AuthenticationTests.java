import Specifications.RequestSpecifications;
import Specifications.ResponseSpecifications;
import Model.User;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AuthenticationTests extends Base{

    @Test(description = "This test aims to register an user")
    public void testRegister()
    {
        User user = new User("Pablo Juan", "password");
        String email = user.getEmail();
        given()
                .body(user)
                .when()
                .post("/v1/user/register")
                .then()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("user.email", Matchers.equalTo(email));
    }

    @Test(description = "This test aims to register an user")
    public void testInvalidRegister()
    {
        User user = new User("juan@jose.com","Juan", "password");
        given()
                .body(user)
                .when()
                .post("/v1/user/register")
                .then()
                .statusCode(406)
                .body("message", Matchers.equalTo("User already exists"));
    }
    @Test(description = "This test aims to login an user")
    public void testLogin()
    {
        User user = new User("juan@jose.com","Pablo Juan", "password");
        String email = user.getEmail();
        given()
                .body(user)
                .when()
                .post("/v1/user/login")
                .then()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("token.access_token", Matchers.notNullValue())
                .body("user.email", Matchers.equalTo(email));
    }
    @Test(description = "This test aims to not login an user")
    public void testInvalidLogin()
    {
        User user = new User("papapapa@papappa.com","Pablo Juan", "pasasasassword");
        given()
                .body(user)
                .when()
                .post("/v1/user/login")
                .then()
                .statusCode(404)
                .body("error", Matchers.equalTo("sql: no rows in result set"))
                .body("message", Matchers.equalTo("Invalid login details"));
    }

    @Test(description = "This aims to logout")
    public void testLogOut()
    {
        given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/user/logout")
                .then()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.equalTo("Successfully logged out"));
    }

    @Test(description = "This aims to an invalid logout")
    public void testFakeLogOut()
    {
        given()
                .spec(RequestSpecifications.useFakeJWTAuthentication())
                .when()
                .get("/v1/user/logout")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("User not logged in"));
    }

}
