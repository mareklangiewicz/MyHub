package pl.mareklangiewicz.myhub.io;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

public final class GitHub {

//    private static final boolean VERY_VERBOSE = false;

    private GitHub() {
        throw new AssertionError("GitHub class is noninstantiable.");
    }


    public static final String URL = "https://api.github.com";

    @SuppressWarnings("unused")
    public static class Plan {
        public String name; // e.g.: "Medium"
        public Long space;
        public Long private_repos;
        public Long collaborators;
    }

    @SuppressWarnings("unused")
    public static class User {
        public String login;
        public long id;
        public String avatar_url;
        public String gravatar_id; // e.g.: ""
        public String url; // e.g.: "https://api.github.com/users/octocat"
        public String html_url; // e.g.: "https://github.com/octocat"
        public String followers_url; // e.g.: "https://api.github.com/users/octocat/followers"
        public String following_url; // e.g.: "https://api.github.com/users/octocat/following{/other_user}"
        public String gists_url; // e.g.: "https://api.github.com/users/octocat/gists{/gist_id}"
        public String starred_url; // e.g.: "https://api.github.com/users/octocat/starred{/owner}{/repo}"
        public String subscriptions_url; // e.g.: "https://api.github.com/users/octocat/subscriptions"
        public String organizations_url; // e.g.: "https://api.github.com/users/octocat/orgs"
        public String repos_url; // e.g.: "https://api.github.com/users/octocat/repos"
        public String events_url; // e.g.: "https://api.github.com/users/octocat/events{/privacy}"
        public String received_events_url; // e.g.: "https://api.github.com/users/octocat/received_events"
        public String type; // e.g.: "User"
        public Boolean site_admin;
        public String name; // e.g.: "monalisa octocat"
        public String company; // e.g.: "GitHub"
        public String blog; // e.g.: "https://github.com/blog"
        public String location; // e.g.: "San Francisco"
        public String email; // e.g.: "octocat@github.com"
        public Boolean hireable;
        public String bio; // e.g.: "There once was..."
        public Long public_repos;
        public Long public_gists;
        public Long followers;
        public Long following;
        public String created_at; // e.g.: "2008-01-14T04:33:35Z"
        public String updated_at; // e.g.: "2008-01-14T04:33:35Z"
        public Long total_private_repos;
        public Long owned_private_repos;
        public Long private_gists;
        public Long disk_usage;
        public Long collaborators;
        public Plan plan;
    }

    @SuppressWarnings("unused")
    public static class Owner {
        public String login; // e.g.: "octocat"
        public Long id;
        public String avatar_url; // e.g.: "https://github.com/images/error/octocat_happy.gif"
        public String gravatar_id; // e.g.: ""
        public String url; // e.g.: "https://api.github.com/users/octocat"
        public String html_url; // e.g.: "https://github.com/octocat"
        public String followers_url; // e.g.: "https://api.github.com/users/octocat/followers"
        public String following_url; // e.g.: "https://api.github.com/users/octocat/following{/other_user}"
        public String gists_url; // e.g.: "https://api.github.com/users/octocat/gists{/gist_id}"
        public String starred_url; // e.g.: "https://api.github.com/users/octocat/starred{/owner}{/repo}"
        public String subscriptions_url; // e.g.: "https://api.github.com/users/octocat/subscriptions"
        public String organizations_url; // e.g.: "https://api.github.com/users/octocat/orgs"
        public String repos_url; // e.g.: "https://api.github.com/users/octocat/repos"
        public String events_url; // e.g.: "https://api.github.com/users/octocat/events{/privacy}"
        public String received_events_url; // e.g.: "https://api.github.com/users/octocat/received_events"
        public String type; // e.g.: "User"
        public Boolean site_admin;

    }

    @SuppressWarnings("unused")
    public static class Permissions {
        public Boolean admin;
        public Boolean push;
        public Boolean pull;
    }

    @SuppressWarnings("unused")
    public static class Repository {
        public Long id;
        Owner owner;
        public String name; // e.g.: "Hello-World"
        public String full_name; // e.g.: "octocat/Hello-World"
        public String description; // e.g.: "This your first repo!"
        // public Boolean private; FIXME SOMEDAY: private is a java keyword too.. check in moshi what can we do..
        public Boolean fork;
        public String url; // e.g.: "https://api.github.com/repos/octocat/Hello-World"
        public String html_url; // e.g.: "https://github.com/octocat/Hello-World"
        public String archive_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/{archive_format}{/ref}"
        public String assignees_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/assignees{/user}"
        public String blobs_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/git/blobs{/sha}"
        public String branches_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/branches{/branch}"
        public String clone_url; // e.g.: "https://github.com/octocat/Hello-World.git"
        public String collaborators_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/collaborators{/collaborator}"
        public String comments_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/comments{/number}"
        public String commits_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/commits{/sha}"
        public String compare_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/compare/{base}...{head}"
        public String contents_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/contents/{+path}"
        public String contributors_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/contributors"
        public String downloads_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/downloads"
        public String events_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/events"
        public String forks_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/forks"
        public String git_commits_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/git/commits{/sha}"
        public String git_refs_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/git/refs{/sha}"
        public String git_tags_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/git/tags{/sha}"
        public String git_url; // e.g.: "git:github.com/octocat/Hello-World.git"
        public String hooks_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/hooks"
        public String issue_comment_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/issues/comments{/number}"
        public String issue_events_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/issues/events{/number}"
        public String issues_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/issues{/number}"
        public String keys_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/keys{/key_id}"
        public String labels_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/labels{/name}"
        public String languages_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/languages"
        public String merges_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/merges"
        public String milestones_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/milestones{/number}"
        public String mirror_url; // e.g.: "git:git.example.com/octocat/Hello-World"
        public String notifications_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/notifications{?since, all, participating}"
        public String pulls_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/pulls{/number}"
        public String releases_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/releases{/id}"
        public String ssh_url; // e.g.: "git@github.com:octocat/Hello-World.git"
        public String stargazers_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/stargazers"
        public String statuses_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/statuses/{sha}"
        public String subscribers_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/subscribers"
        public String subscription_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/subscription"
        public String svn_url; // e.g.: "https://svn.github.com/octocat/Hello-World"
        public String tags_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/tags"
        public String teams_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/teams"
        public String trees_url; // e.g.: "http://api.github.com/repos/octocat/Hello-World/git/trees{/sha}"
        public String homepage; // e.g.: "https://github.com"
        public String language; // e.g.: null
        public Long forks_count;
        public Long stargazers_count;
        public Long watchers_count;
        public Long size;
        public String default_branch; // e.g.: "master"
        public Long open_issues_count;
        public Boolean has_issues;
        public Boolean has_wiki;
        public Boolean has_pages;
        public Boolean has_downloads;
        public String pushed_at; // e.g.: "2011-01-26T19:06:43Z"
        public String created_at; // e.g.: "2011-01-26T19:01:12Z"
        public String updated_at; // e.g.: "2011-01-26T19:14:43Z"
        Permissions permissions;
    }

    private static final String ACCEPT = "Accept: application/vnd.github.v3+json";
    private static final String AGENT = "User-Agent: MyHub";

    public interface Service {

        @Headers({ACCEPT, AGENT})
        @GET("/users/{user}")
        Call<User> getUserCall(@Path("user") String user);

        @Headers({ACCEPT, AGENT})
        @GET("/user")
        Call<User> getUserAuthCall(@Header("Authorization") String auth);

        @Headers({ACCEPT, AGENT})
        @GET("/user")
        Call<User> getUserTFACall(@Header("Authorization") String auth, @Header("X-GitHub-OTP") String code);

        @Headers({ACCEPT, AGENT})
        @GET("/users/{user}/repos?per_page=100")
        Call<List<Repository>> getUserReposCall(@Path("user") String user);

        @Headers({ACCEPT, AGENT})
        @GET("/user/repos?per_page=100")
        Call<List<Repository>> getUserReposAuthCall(@Header("Authorization") String auth);

        @Headers({ACCEPT, AGENT})
        @GET("/user/repos?per_page=100")
        Call<List<Repository>> getUserReposTFACall(@Header("Authorization") String auth, @Header("X-GitHub-OTP") String code);


        @Headers({ACCEPT, AGENT})
        @GET("/users/{user}")
        Observable<User> getUserObservable(@Path("user") String user);

        @Headers({ACCEPT, AGENT})
        @GET("/user")
        Observable<User> getUserAuthObservable(@Header("Authorization") String auth);

        @Headers({ACCEPT, AGENT})
        @GET("/user")
        Observable<User> getUserTFAObservable(@Header("Authorization") String auth, @Header("X-GitHub-OTP") String code);

        @Headers({ACCEPT, AGENT})
        @GET("/users/{user}/repos?per_page=100")
        Observable<List<Repository>> getUserReposObservable(@Path("user") String user);

        @Headers({ACCEPT, AGENT})
        @GET("/user/repos?per_page=100")
        Observable<List<Repository>> getUserReposAuthObservable(@Header("Authorization") String auth);

        @Headers({ACCEPT, AGENT})
        @GET("/user/repos?per_page=100")
        Observable<List<Repository>> getUserReposTFAObservable(@Header("Authorization") String auth, @Header("X-GitHub-OTP") String code);
    }

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    public static Service create() {


//        if(VERY_VERBOSE) {
//            OkHttpClient client = new OkHttpClient();
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            client.interceptors().add(interceptor);
//
//            Retrofit loggingretrofit = new Retrofit.Builder()
//                    .baseUrl(URL)
//                    .client(client)
//                    .addConverterFactory(MoshiConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .build();
//
//            return loggingretrofit.create(Service.class);
//        }


        return retrofit.create(Service.class);
    }

}
