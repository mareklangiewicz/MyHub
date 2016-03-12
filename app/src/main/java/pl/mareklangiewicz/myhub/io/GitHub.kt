package pl.mareklangiewicz.myhub.io

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import rx.Observable

//    private static final boolean VERY_VERBOSE = false;

const val URL = "https://api.github.com"

const val ACCEPT = "Accept: application/vnd.github.v3+json"
const val AGENT = "User-Agent: MyHub"



class Plan {
    var name: String? = null // e.g.: "Medium"
    var space: Long? = null
    var private_repos: Long? = null
    var collaborators: Long? = null
}

class User {
    var login: String? = null
    var id: Long = 0
    var avatar_url: String? = null
    var gravatar_id: String? = null // e.g.: ""
    var url: String? = null // e.g.: "https://api.github.com/users/octocat"
    var html_url: String? = null // e.g.: "https://github.com/octocat"
    var followers_url: String? = null // e.g.: "https://api.github.com/users/octocat/followers"
    var following_url: String? = null // e.g.: "https://api.github.com/users/octocat/following{/other_user}"
    var gists_url: String? = null // e.g.: "https://api.github.com/users/octocat/gists{/gist_id}"
    var starred_url: String? = null // e.g.: "https://api.github.com/users/octocat/starred{/owner}{/repo}"
    var subscriptions_url: String? = null // e.g.: "https://api.github.com/users/octocat/subscriptions"
    var organizations_url: String? = null // e.g.: "https://api.github.com/users/octocat/orgs"
    var repos_url: String? = null // e.g.: "https://api.github.com/users/octocat/repos"
    var events_url: String? = null // e.g.: "https://api.github.com/users/octocat/events{/privacy}"
    var received_events_url: String? = null // e.g.: "https://api.github.com/users/octocat/received_events"
    var type: String? = null // e.g.: "User"
    var site_admin: Boolean? = null
    var name: String? = null // e.g.: "monalisa octocat"
    var company: String? = null // e.g.: "GitHub"
    var blog: String? = null // e.g.: "https://github.com/blog"
    var location: String? = null // e.g.: "San Francisco"
    var email: String? = null // e.g.: "octocat@github.com"
    var hireable: Boolean? = null
    var bio: String? = null // e.g.: "There once was..."
    var public_repos: Long? = null
    var public_gists: Long? = null
    var followers: Long? = null
    var following: Long? = null
    var created_at: String? = null // e.g.: "2008-01-14T04:33:35Z"
    var updated_at: String? = null // e.g.: "2008-01-14T04:33:35Z"
    var total_private_repos: Long? = null
    var owned_private_repos: Long? = null
    var private_gists: Long? = null
    var disk_usage: Long? = null
    var collaborators: Long? = null
    var plan: Plan? = null
}

class Owner {
    var login: String? = null // e.g.: "octocat"
    var id: Long? = null
    var avatar_url: String? = null // e.g.: "https://github.com/images/error/octocat_happy.gif"
    var gravatar_id: String? = null // e.g.: ""
    var url: String? = null // e.g.: "https://api.github.com/users/octocat"
    var html_url: String? = null // e.g.: "https://github.com/octocat"
    var followers_url: String? = null // e.g.: "https://api.github.com/users/octocat/followers"
    var following_url: String? = null // e.g.: "https://api.github.com/users/octocat/following{/other_user}"
    var gists_url: String? = null // e.g.: "https://api.github.com/users/octocat/gists{/gist_id}"
    var starred_url: String? = null // e.g.: "https://api.github.com/users/octocat/starred{/owner}{/repo}"
    var subscriptions_url: String? = null // e.g.: "https://api.github.com/users/octocat/subscriptions"
    var organizations_url: String? = null // e.g.: "https://api.github.com/users/octocat/orgs"
    var repos_url: String? = null // e.g.: "https://api.github.com/users/octocat/repos"
    var events_url: String? = null // e.g.: "https://api.github.com/users/octocat/events{/privacy}"
    var received_events_url: String? = null // e.g.: "https://api.github.com/users/octocat/received_events"
    var type: String? = null // e.g.: "User"
    var site_admin: Boolean? = null

}

class Permissions {
    var admin: Boolean? = null
    var push: Boolean? = null
    var pull: Boolean? = null
}

class Repository {
    var id: Long? = null
    internal var owner: Owner? = null
    var name: String? = null // e.g.: "Hello-World"
    var full_name: String? = null // e.g.: "octocat/Hello-World"
    var description: String? = null // e.g.: "This your first repo!"
    // public Boolean private; FIXME SOMEDAY: private is a java keyword too.. check in moshi what can we do..
    var fork: Boolean? = null
    var url: String? = null // e.g.: "https://api.github.com/repos/octocat/Hello-World"
    var html_url: String? = null // e.g.: "https://github.com/octocat/Hello-World"
    var archive_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/{archive_format}{/ref}"
    var assignees_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/assignees{/user}"
    var blobs_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/git/blobs{/sha}"
    var branches_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/branches{/branch}"
    var clone_url: String? = null // e.g.: "https://github.com/octocat/Hello-World.git"
    var collaborators_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/collaborators{/collaborator}"
    var comments_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/comments{/number}"
    var commits_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/commits{/sha}"
    var compare_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/compare/{base}...{head}"
    var contents_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/contents/{+path}"
    var contributors_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/contributors"
    var downloads_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/downloads"
    var events_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/events"
    var forks_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/forks"
    var git_commits_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/git/commits{/sha}"
    var git_refs_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/git/refs{/sha}"
    var git_tags_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/git/tags{/sha}"
    var git_url: String? = null // e.g.: "git:github.com/octocat/Hello-World.git"
    var hooks_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/hooks"
    var issue_comment_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/issues/comments{/number}"
    var issue_events_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/issues/events{/number}"
    var issues_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/issues{/number}"
    var keys_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/keys{/key_id}"
    var labels_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/labels{/name}"
    var languages_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/languages"
    var merges_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/merges"
    var milestones_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/milestones{/number}"
    var mirror_url: String? = null // e.g.: "git:git.example.com/octocat/Hello-World"
    var notifications_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/notifications{?since, all, participating}"
    var pulls_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/pulls{/number}"
    var releases_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/releases{/id}"
    var ssh_url: String? = null // e.g.: "git@github.com:octocat/Hello-World.git"
    var stargazers_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/stargazers"
    var statuses_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/statuses/{sha}"
    var subscribers_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/subscribers"
    var subscription_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/subscription"
    var svn_url: String? = null // e.g.: "https://svn.github.com/octocat/Hello-World"
    var tags_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/tags"
    var teams_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/teams"
    var trees_url: String? = null // e.g.: "http://api.github.com/repos/octocat/Hello-World/git/trees{/sha}"
    var homepage: String? = null // e.g.: "https://github.com"
    var language: String? = null // e.g.: null
    var forks_count: Long? = null
    var stargazers_count: Long? = null
    var watchers_count: Long? = null
    var size: Long? = null
    var default_branch: String? = null // e.g.: "master"
    var open_issues_count: Long? = null
    var has_issues: Boolean? = null
    var has_wiki: Boolean? = null
    var has_pages: Boolean? = null
    var has_downloads: Boolean? = null
    var pushed_at: String? = null // e.g.: "2011-01-26T19:06:43Z"
    var created_at: String? = null // e.g.: "2011-01-26T19:01:12Z"
    var updated_at: String? = null // e.g.: "2011-01-26T19:14:43Z"
    internal var permissions: Permissions? = null
}

interface GithubService {

    @Headers(ACCEPT, AGENT)
    @GET("/users/{user}")
    fun getUserCall(@Path("user") user: String): Call<User>

    @Headers(ACCEPT, AGENT)
    @GET("/user")
    fun getUserAuthCall(@Header("Authorization") auth: String): Call<User>

    @Headers(ACCEPT, AGENT)
    @GET("/user")
    fun getUserTFACall(@Header("Authorization") auth: String, @Header("X-GitHub-OTP") code: String): Call<User>

    @Headers(ACCEPT, AGENT)
    @GET("/users/{user}/repos?per_page=100")
    fun getUserReposCall(@Path("user") user: String): Call<List<Repository>>

    @Headers(ACCEPT, AGENT)
    @GET("/user/repos?per_page=100")
    fun getUserReposAuthCall(@Header("Authorization") auth: String): Call<List<Repository>>

    @Headers(ACCEPT, AGENT)
    @GET("/user/repos?per_page=100")
    fun getUserReposTFACall(@Header("Authorization") auth: String, @Header("X-GitHub-OTP") code: String): Call<List<Repository>>


    @Headers(ACCEPT, AGENT)
    @GET("/users/{user}")
    fun getUserObservable(@Path("user") user: String): Observable<User>

    @Headers(ACCEPT, AGENT)
    @GET("/user")
    fun getUserAuthObservable(@Header("Authorization") auth: String): Observable<User>

    @Headers(ACCEPT, AGENT)
    @GET("/user")
    fun getUserTFAObservable(@Header("Authorization") auth: String, @Header("X-GitHub-OTP") code: String): Observable<User>

    @Headers(ACCEPT, AGENT)
    @GET("/users/{user}/repos?per_page=100")
    fun getUserReposObservable(@Path("user") user: String): Observable<List<Repository>>

    @Headers(ACCEPT, AGENT)
    @GET("/user/repos?per_page=100")
    fun getUserReposAuthObservable(@Header("Authorization") auth: String): Observable<List<Repository>>

    @Headers(ACCEPT, AGENT)
    @GET("/user/repos?per_page=100")
    fun getUserReposTFAObservable(@Header("Authorization") auth: String, @Header("X-GitHub-OTP") code: String): Observable<List<Repository>>
}


private val retrofit = Retrofit.Builder().baseUrl(URL).addConverterFactory(MoshiConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()

fun createGithubService(): GithubService {


        // THIS IS OLD JAVA WAY TO INJECT SOME REQUEST/RESPONSE LOGGING
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


    return retrofit.create(GithubService::class.java)
}

