import Helpers.RequestHelpers;
import io.restassured.RestAssured;
import org.testng.annotations.*;

public class Base {

    protected int postId;
    protected int commentId;
    protected int invalidNumber = 100;

    @Parameters("host")
    @BeforeSuite(alwaysRun = true)
    public void setup(@Optional("http://localhost:9000") String host) {
        System.out.println(String.format("Test Host: %s", host));
        System.out.println("Executing tests, please wait...");
        RestAssured.baseURI = host;
    }

    // before groups

    @BeforeMethod(groups = "useComment")
    void create()
    {
        postId = RequestHelpers.createRandomPostAndGetID();
        commentId = RequestHelpers.createRandomCommentAndGetID(postId);
    }

    @AfterMethod(groups = "useComment")
    void delete()
    {
        RequestHelpers.cleanUpComment(postId, commentId);
        RequestHelpers.cleanUpPost(postId);
    }
}
