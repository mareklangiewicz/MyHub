package pl.mareklangiewicz.mygithub.io;

import com.google.common.truth.Expect;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.MyHandler;
import com.noveogroup.android.log.MyLogger;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import retrofit.Call;
import retrofit.Response;

import static pl.mareklangiewicz.myutils.MyTextUtils.str;

/**
 * Created by Marek Langiewicz on 30.12.15.
 */
public class MyHttpTest {

    private static final MyLogger log = new MyLogger("UT");

    @Rule public final Expect EXPECT = Expect.create();

    @Before
    public void setUp() throws Exception {
        MyHandler.sPrintLnLevel = Logger.Level.VERBOSE;
    }

    @After
    public void tearDown() throws Exception {

    }




    // Manual tests for user to launch by hand (one by one) and to inspect results on console
    // or to set breakpoints and analyse step by step.



    @Ignore
    @Test
    public void testGitHubGetUser() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<MyHttp.GitHub.User> call = service.getUser("langara");
        Response<MyHttp.GitHub.User> response = call.execute();
        MyHttp.GitHub.User body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
        call = service.getUser("JakeWharton");
        response = call.execute();
        body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }


    // WARNING: You need base64 encoded user name and password to run this test.
    // you can calculate it fast in python:
    // import base64
    // base64.b64encode("someuser:somepassword")
    @Ignore
    @Test
    public void testGitHubGetUserAuth() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<MyHttp.GitHub.User> call = service.getUserAuth("Basic some_bad_base64_pass");
        Response<MyHttp.GitHub.User> response = call.execute();
        MyHttp.GitHub.User body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }

    // WARNING: see test above, and use correct OTP code as a second parameter
    @Ignore
    @Test
    public void testGitHubGetUserTFA() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<MyHttp.GitHub.User> call = service.getUserTFA("Basic some_bad_base64_pass", "421164");
        Response<MyHttp.GitHub.User> response = call.execute();
        MyHttp.GitHub.User body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }

    @Ignore
    @Test
    public void testGitHubGetUserRepos() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<List<MyHttp.GitHub.Repository>> call = service.getUserRepos("langara");
        Response<List<MyHttp.GitHub.Repository>> response = call.execute();
        List<MyHttp.GitHub.Repository> body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
        call = service.getUserRepos("JakeWharton");
        response = call.execute();
        body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }


    @Ignore
    @Test
    public void testGitHubGetUserReposAuth() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<List<MyHttp.GitHub.Repository>> call = service.getUserReposAuth("Basic some_bad_base64");
        Response<List<MyHttp.GitHub.Repository>> response = call.execute();
        List<MyHttp.GitHub.Repository> body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }


    @Ignore
    @Test
    public void testGitHubGetUserReposTFA() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<List<MyHttp.GitHub.Repository>> call = service.getUserReposTFA("Basic some_bad_base64", "197187");
        Response<List<MyHttp.GitHub.Repository>> response = call.execute();
        List<MyHttp.GitHub.Repository> body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }

}
