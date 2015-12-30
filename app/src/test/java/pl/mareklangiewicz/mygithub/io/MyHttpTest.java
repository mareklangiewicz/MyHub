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
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

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

    // Tests below are manual tests for user to launch by hand (one by one)
    // and to inspect results on console or to set breakpoints and analyse step by step.


    @Ignore
    @Test
    public void testGitHubGetUserCall() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<MyHttp.GitHub.User> call = service.getUserCall("langara");
        Response<MyHttp.GitHub.User> response = call.execute();
        MyHttp.GitHub.User body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
        call = service.getUserCall("JakeWharton");
        response = call.execute();
        body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }


    // WARNING: You need base64 encoded user name and password to run this test.
    // you can calculate it easily in python:
    // import base64
    // base64.b64encode("someuser:somepassword")
    @Ignore
    @Test
    public void testGitHubGetUserAuthCall() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<MyHttp.GitHub.User> call = service.getUserAuthCall("Basic some_bad_base64_pass");
        Response<MyHttp.GitHub.User> response = call.execute();
        MyHttp.GitHub.User body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }

    // WARNING: see test above, and use correct OTP code as a second parameter
    @Ignore
    @Test
    public void testGitHubGetUserTFACall() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<MyHttp.GitHub.User> call = service.getUserTFACall("Basic some_bad_base64_pass", "421164");
        Response<MyHttp.GitHub.User> response = call.execute();
        MyHttp.GitHub.User body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }

    @Ignore
    @Test
    public void testGitHubGetUserReposCall() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<List<MyHttp.GitHub.Repository>> call = service.getUserReposCall("langara");
        Response<List<MyHttp.GitHub.Repository>> response = call.execute();
        List<MyHttp.GitHub.Repository> body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
        call = service.getUserReposCall("JakeWharton");
        response = call.execute();
        body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }


    // WARNING: You need base64 encoded user name and password to run this test.
    // you can calculate it easily in python:
    // import base64
    // base64.b64encode("someuser:somepassword")
    @Ignore
    @Test
    public void testGitHubGetUserReposAuthCall() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<List<MyHttp.GitHub.Repository>> call = service.getUserReposAuthCall("Basic some_bad_base64");
        Response<List<MyHttp.GitHub.Repository>> response = call.execute();
        List<MyHttp.GitHub.Repository> body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }


    // WARNING: see test above, and use correct OTP code as a second parameter
    @Ignore
    @Test
    public void testGitHubGetUserReposTFACall() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Call<List<MyHttp.GitHub.Repository>> call = service.getUserReposTFACall("Basic some_bad_base64", "197187");
        Response<List<MyHttp.GitHub.Repository>> response = call.execute();
        List<MyHttp.GitHub.Repository> body = response.body();
        log.w(str(body)); // set breakpoint here to see properties
    }




    <T> void subscribeAndLogObservable(Observable<T> observable) {
        observable.subscribe( // synchronously
                new Action1<T>() {
                    @Override public void call(T t) {
                        log.i(str(t)); // set breakpoint here to see properties
                    }
                },
                new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        log.e(throwable);
                    }
                },
                new Action0() {
                    @Override public void call() {
                        log.i("completed.");
                    }
                }
        );
    }


    @Ignore
    @Test
    public void testGitHubGetUserObservable() throws Exception {

        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Observable<MyHttp.GitHub.User> observable = service.getUserObservable("langara");
        subscribeAndLogObservable(observable);

        observable = service.getUserObservable("JakeWharton");
        subscribeAndLogObservable(observable);
    }

    // WARNING: You need base64 encoded user name and password to run this test.
    // you can calculate it easily in python:
    // import base64
    // base64.b64encode("someuser:somepassword")
    @Ignore
    @Test
    public void testGitHubGetUserAuthObservable() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Observable<MyHttp.GitHub.User> observable = service.getUserAuthObservable("Basic some_bad_base64_pass");
        subscribeAndLogObservable(observable);
    }

    // WARNING: see test above, and use correct OTP code as a second parameter
    @Ignore
    @Test
    public void testGitHubGetUserTFAObservable() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Observable<MyHttp.GitHub.User> observable = service.getUserTFAObservable("Basic some_bad_base64_pass", "421164");
        subscribeAndLogObservable(observable);
    }

    @Ignore
    @Test
    public void testGitHubGetUserReposObservable() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Observable<List<MyHttp.GitHub.Repository>> observable = service.getUserReposObservable("langara");
        subscribeAndLogObservable(observable);
        observable = service.getUserReposObservable("JakeWharton");
        subscribeAndLogObservable(observable);
    }


    // WARNING: You need base64 encoded user name and password to run this test.
    // you can calculate it easily in python:
    // import base64
    // base64.b64encode("someuser:somepassword")
    @Ignore
    @Test
    public void testGitHubGetUserReposAuthObservable() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Observable<List<MyHttp.GitHub.Repository>> observable = service.getUserReposAuthObservable("Basic some_bad_base64");
        subscribeAndLogObservable(observable);
    }


    // WARNING: see test above, and use correct OTP code as a second parameter
    @Ignore
    @Test
    public void testGitHubGetUserReposTFAObservable() throws Exception {
        MyHttp.GitHub.Service service = MyHttp.GitHub.create();
        Observable<List<MyHttp.GitHub.Repository>> observable = service.getUserReposTFAObservable("Basic some_bad_base64", "197187");
        subscribeAndLogObservable(observable);
    }

}
