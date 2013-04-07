package com.lomoussw.androidstarter;

import com.lomoussw.androidstarter.util.GithubUtils;
import com.lomoussw.androidstarter.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Path("/")
public class Main {// extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String ERROR = "error";
    private static final String SUCCESS = "success";
    private static String GITHUB_SECRET;
    private static String GITHUB_ID;

    // Initialize the
    static {
        Properties prop = new Properties();
        try {
            prop.load(Main.class.getClassLoader().getResourceAsStream("Github.properties"));
            GITHUB_ID = prop.getProperty("id");
            GITHUB_SECRET = prop.getProperty("secret");
        } catch (FileNotFoundException e) {
            LOGGER.error("problem during properties loading", e);
        } catch (IOException e) {
            LOGGER.error("problem during properties loading", e);
        }
    }

    @GET
    @Path("test")
    public Response test() {
        LOGGER.debug("GITHUB_ID = " + GITHUB_ID);
        LOGGER.debug("GITHUB_SECRET = " + GITHUB_SECRET);
        return Response.noContent().build();
    }

    @POST
    @Path("builder")
    @Produces("application/zip")
    public Response go(//

                       // State
                       @FormParam("actionBarSherlock") boolean actionBarSherlock,//
                       @FormParam("navigationType") String navigationType,//
                       @FormParam("androidAnnotations") boolean androidAnnotations,//
                       @FormParam("restTemplate") boolean restTemplate,//
                       @FormParam("maven") boolean maven,//
                       @FormParam("nineOldAndroids") boolean nineOldAndroids,//
                       @FormParam("supportV4") boolean supportV4,//
                       @FormParam("acra") boolean acra,//
                       @FormParam("eclipse") boolean eclipse,//
                       @FormParam("viewPager") boolean viewPager,//
                       @FormParam("viewPagerIndicator") boolean viewPagerIndicator,//
                       @FormParam("roboguice") boolean roboguice,//
                       @FormParam("proguard") boolean proguard,//

                       // Application
                       @FormParam("packageName") String packageName,//
                       @FormParam("name") String name,//
                       @FormParam("activity") String activity,//
                       @FormParam("activityLayout") String activityLayout,//
                       @FormParam("sdkTarget") int sdkTarget,//
                       @FormParam("sdkMinTarget") int sdkMinTarget,//
                       @FormParam("sdkMaxTarget") int sdkMaxTarget,//
                       // Github access token
                       @FormParam("accessToken") String accessToken//
    ) {

        boolean listNavigation = false;
        boolean tabNavigation = false;
        boolean git = false;

        if (navigationType != null) {
            tabNavigation = navigationType.equals("tabNavigation");
            listNavigation = navigationType.equals("listNavigation");
        }

        if (StringUtils.isEmpty(packageName)) {
            packageName = "com.androidstarter.app";
        }
        if (sdkMinTarget < 7 || sdkMinTarget > 17) {
            sdkMinTarget = 7;
        }

        if (sdkTarget < 7 || sdkTarget > 17) {
            sdkTarget = 17;
        }

        if (sdkMaxTarget < sdkMinTarget || sdkMaxTarget < sdkTarget || sdkMaxTarget > 17) {
            sdkMaxTarget = sdkTarget;
        }

        if (sdkMinTarget > sdkTarget) {
            sdkMinTarget = sdkTarget;
        }

        if (StringUtils.isEmpty(name)) {
            name = "MyApplication";
        }
        if (StringUtils.isEmpty(activity)) {
            activity = "MainActivity";
        }
        if (StringUtils.isEmpty(activityLayout)) {
            activityLayout = "activity_main";
        }

        if (!StringUtils.isEmpty(accessToken)) {
            git = true;
        }

        if (viewPager && !actionBarSherlock && !viewPagerIndicator && !supportV4) {
            supportV4 = true;
        }

        AppDetails appDetails = new AppDetails.Builder().//

                // Parameters
                packageName(packageName).//
                name(name).//
                activity(activity).//
                activityLayout(activityLayout).//
                minSdk(sdkMinTarget).//
                targetSdk(sdkTarget).//
                maxSdk(sdkMaxTarget).//
                permissions(new ArrayList<String>()).//

                // Libraries
                actionBarSherlock(actionBarSherlock).//
                listNavigation(listNavigation).//
                tabNavigation(tabNavigation).//
                viewPager(viewPager).//
                viewPagerIndicator(viewPagerIndicator).//
                roboguice(roboguice).//
                androidAnnotations(androidAnnotations).//
                restTemplate(restTemplate). //
                maven(maven). //
                nineOldAndroids(nineOldAndroids). //
                supportV4(supportV4). //
                acra(acra). //
                eclipse(eclipse). //
                proguard(proguard). //
                git(git). //
                build();

        final Kickstartr kickstarter = new Kickstartr(appDetails);

        if (!git) {
            LOGGER.debug("No github asked");
            final File file = kickstarter.zipify();

            if (file == null) {
                return Response.serverError().build();
            }

            StreamingOutput output = new StreamingOutput() {
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    try {
                        FileUtils.copyFile(file, output);
                        kickstarter.clean();
                    } catch (Exception e) {
                        throw new WebApplicationException(e);
                    }
                }
            };

            return Response //
                    .ok(output) //
                    .header("Content-Length", file.length()) //
                    .header("Content-Disposition", "attachment; filename=" + file.getName()) //
                    .build();
        } else {
            LOGGER.debug("Github output asked");
            Repository repository = null;
            try {
                repository = kickstarter.githubify(accessToken);
            } catch (IOException e) {
                e.printStackTrace();
                return Response.seeOther(GithubUtils.createAndroidKickstartRUriWithAccessToken(accessToken, ERROR, "Unable to create or access repository : " + e.getMessage())).build();
            } catch (GitAPIException e) {
                e.printStackTrace();
                return Response.seeOther(GithubUtils.createAndroidKickstartRUriWithAccessToken(accessToken, ERROR, "Unable to create or access repository : " + e.getMessage())).build();
            } finally {
                kickstarter.clean();
            }
            if (repository != null) {
                return Response.seeOther(GithubUtils.createAndroidKickstartRUriWithAccessToken(accessToken, SUCCESS, "Repository successfully created! You can access it now at the following address : &repositoryUrl=" + repository.getHtmlUrl())).build();
            } else {
                return Response.serverError().build();
            }
        }
    }

    @GET
    @Path("token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccessToken(@QueryParam("code") String code) throws ClientProtocolException, IOException, URISyntaxException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost("https://github.com/login/oauth/access_token");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("client_id", GITHUB_ID));
        nameValuePair.add(new BasicNameValuePair("client_secret", GITHUB_SECRET));
        nameValuePair.add(new BasicNameValuePair("code", code));

        postRequest.setEntity(new UrlEncodedFormEntity(nameValuePair));

        HttpResponse response = httpclient.execute(postRequest);
        if (response.getStatusLine().getStatusCode() != 200) {
            return Response.temporaryRedirect(GithubUtils.createAndroidKickstartRUri(ERROR, "Unexpected status code : " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase())).build();
        } else {
            HttpEntity entity = response.getEntity();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String accessToken = GithubUtils.findAccessTokenInString(line);
                if (accessToken != null) {
                    return Response.temporaryRedirect(GithubUtils.createAndroidKickstartRUriGit(GithubUtils.ACCESS_TOKEN, accessToken)).build();
                }
            }
        }
        return Response.temporaryRedirect(GithubUtils.createAndroidKickstartRUri(ERROR, "Couldn't retrieve access token")).build();
    }

}
