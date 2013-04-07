package com.lomoussw.androidstarter;

import com.lomoussw.androidstarter.Enums.CountryCode;
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
                       @FormParam("customApp") boolean customApp,//

                       // Application
                       @FormParam("packageName") String packageName,//
                       @FormParam("name") String name,//
                       @FormParam("activity") String activity,//
                       @FormParam("activityLayout") String activityLayout,//
                       @FormParam("sdkTarget") int sdkTarget,//
                       @FormParam("sdkMinTarget") int sdkMinTarget,//
                       @FormParam("sdkMaxTarget") int sdkMaxTarget,//

                        // Languages
                        @FormParam("ar_EG") boolean ar_EG,//
                        @FormParam("ar_IL") boolean ar_IL,//
                        @FormParam("bg_BG") boolean bg_BG,//
                        @FormParam("ca_ES") boolean ca_ES,//
                        @FormParam("da_DK") boolean da_DK,//
                        @FormParam("de_AT") boolean de_AT,//
                        @FormParam("de_CH") boolean de_CH,//
                        @FormParam("de_DE") boolean de_DE,//
                        @FormParam("de_LI") boolean de_LI,//
                        @FormParam("el_GR") boolean el_GR,//
                        @FormParam("en_AU") boolean en_AU,//
                        @FormParam("en_CA") boolean en_CA,//
                        @FormParam("en_GB") boolean en_GB,//
                        @FormParam("en_IE") boolean en_IE,//
                        @FormParam("en_IN") boolean en_IN,//
                        @FormParam("en_NZ") boolean en_NZ,//
                        @FormParam("en_SG") boolean en_SG,//
                        @FormParam("en_US") boolean en_US,//
                        @FormParam("en_ZA") boolean en_ZA,//
                        @FormParam("es_ES") boolean es_ES,//
                        @FormParam("es_US") boolean es_US,//
                        @FormParam("fi_FI") boolean fi_FI,//
                        @FormParam("fr_BE") boolean fr_BE,//
                        @FormParam("fr_CA") boolean fr_CA,//
                        @FormParam("fr_CH") boolean fr_CH,//
                        @FormParam("fr_FR") boolean fr_FR,//
                        @FormParam("hr_HR") boolean hr_HR,//
                        @FormParam("hu_HU") boolean hu_HU,//
                        @FormParam("in_ID") boolean in_ID,//
                        @FormParam("it_CH") boolean it_CH,//
                        @FormParam("it_IT") boolean it_IT,//
                        @FormParam("iw_IL") boolean iw_IL,//
                        @FormParam("ja_JP") boolean ja_JP,//
                        @FormParam("ko_KR") boolean ko_KR,//
                        @FormParam("it_LT") boolean it_LT,//
                        @FormParam("iv_LV") boolean iv_LV,//
                        @FormParam("nb_NO") boolean nb_NO,//
                        @FormParam("nl_BE") boolean nl_BE,//
                        @FormParam("lt_LT") boolean lt_LT,//
                        @FormParam("nl_NL") boolean nl_NL,//
                        @FormParam("pl_PL") boolean pl_PL,//
                        @FormParam("pt_BR") boolean pt_BR,//
                        @FormParam("pt_PT") boolean pt_PT,//
                        @FormParam("ro_RO") boolean ro_RO,//
                        @FormParam("sk_SK") boolean sk_SK,//
                        @FormParam("sl_SI") boolean sl_SI,//
                        @FormParam("sr_RS") boolean sr_RS,//
                        @FormParam("sv_SE") boolean sv_SE,//
                        @FormParam("th_TH") boolean th_TH,//
                        @FormParam("tl_PH") boolean tl_PH,//
                        @FormParam("tr_TH") boolean tr_TH,//
                        @FormParam("uk_UA") boolean uk_UA,//
                        @FormParam("vi_VN") boolean vi_VN,//
                        @FormParam("zh_CN") boolean zh_CN,//
                        @FormParam("zh_TW") boolean zh_TW,//
                        @FormParam("cs_CZ") boolean cs_CZ,//
                        @FormParam("ru_RU") boolean ru_RU,//
                        @FormParam("hi_IN") boolean hi_IN,//
                        @FormParam("lv_LV") boolean lv_LV,//
                        @FormParam("tr_TR") boolean tr_TR,//
                        // Github access token
                       @FormParam("accessToken") String accessToken//
    ) {

        boolean listNavigation = false;
        boolean tabNavigation = false;
        boolean git = false;
        List<String> langs = new ArrayList<String>();

        if(ar_EG) langs.add(CountryCode.ar_EG.toString());
        if(ar_IL) langs.add(CountryCode.ar_IL.toString());
        if(bg_BG) langs.add(CountryCode.bg_BG.toString());
        if(ca_ES) langs.add(CountryCode.ca_ES.toString());
        if(da_DK) langs.add(CountryCode.da_DK.toString());
        if(de_AT) langs.add(CountryCode.de_AT.toString());
        if(de_CH) langs.add(CountryCode.de_CH.toString());
        if(de_DE) langs.add(CountryCode.de_DE.toString());
        if(de_LI) langs.add(CountryCode.de_LI.toString());
        if(el_GR) langs.add(CountryCode.el_GR.toString());

        if(en_AU) langs.add(CountryCode.en_AU.toString());
        if(en_CA) langs.add(CountryCode.en_CA.toString());
        if(en_GB) langs.add(CountryCode.en_GB.toString());
        if(en_IE) langs.add(CountryCode.en_IE.toString());
        if(en_IN) langs.add(CountryCode.en_IN.toString());
        if(en_NZ) langs.add(CountryCode.en_NZ.toString());
        if(en_SG) langs.add(CountryCode.en_SG.toString());
        if(en_US) langs.add(CountryCode.en_US.toString());
        if(en_ZA) langs.add(CountryCode.en_ZA.toString());
        if(es_ES) langs.add(CountryCode.es_ES.toString());

        if(es_US) langs.add(CountryCode.es_US.toString());
        if(fi_FI) langs.add(CountryCode.fi_FI.toString());
        if(fr_BE) langs.add(CountryCode.fr_BE.toString());
        if(fr_CA) langs.add(CountryCode.fr_CA.toString());
        if(fr_CH) langs.add(CountryCode.fr_CH.toString());
        if(fr_FR) langs.add(CountryCode.fr_FR.toString());
        if(hr_HR) langs.add(CountryCode.hr_HR.toString());
        if(hu_HU) langs.add(CountryCode.hu_HU.toString());
        if(in_ID) langs.add(CountryCode.in_ID.toString());
        if(it_CH) langs.add(CountryCode.it_CH.toString());

        if(it_IT) langs.add(CountryCode.it_IT.toString());
        if(iw_IL) langs.add(CountryCode.iw_IL.toString());
        if(ja_JP) langs.add(CountryCode.ja_JP.toString());
        if(ko_KR) langs.add(CountryCode.ko_KR.toString());
        if(lt_LT) langs.add(CountryCode.lt_LT.toString());
        if(it_LT) langs.add(CountryCode.it_LT.toString());
        if(iv_LV) langs.add(CountryCode.iv_LV.toString());
        if(nb_NO) langs.add(CountryCode.nb_NO.toString());
        if(nl_BE) langs.add(CountryCode.nl_BE.toString());
        if(nl_NL) langs.add(CountryCode.nl_NL.toString());

        if(pl_PL) langs.add(CountryCode.pl_PL.toString());
        if(pt_BR) langs.add(CountryCode.pt_BR.toString());
        if(pt_PT) langs.add(CountryCode.pt_PT.toString());
        if(ro_RO) langs.add(CountryCode.ro_RO.toString());
        if(sk_SK) langs.add(CountryCode.sk_SK.toString());
        if(sl_SI) langs.add(CountryCode.sl_SI.toString());
        if(sr_RS) langs.add(CountryCode.sr_RS.toString());
        if(sv_SE) langs.add(CountryCode.sv_SE.toString());
        if(th_TH) langs.add(CountryCode.th_TH.toString());
        if(tl_PH) langs.add(CountryCode.tl_PH.toString());

        if(tr_TH) langs.add(CountryCode.tr_TH.toString());
        if(uk_UA) langs.add(CountryCode.uk_UA.toString());
        if(vi_VN) langs.add(CountryCode.vi_VN.toString());
        if(zh_CN) langs.add(CountryCode.zh_CN.toString());
        if(zh_TW) langs.add(CountryCode.zh_TW.toString());

        if(cs_CZ) langs.add(CountryCode.cs_CZ.toString());
        if(ru_RU) langs.add(CountryCode.ru_RU.toString());
        if(hi_IN) langs.add(CountryCode.hi_IN.toString());
        if(lv_LV) langs.add(CountryCode.lv_LV.toString());
        if(tr_TR) langs.add(CountryCode.tr_TR.toString());

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
                customApp(customApp).
                permissions(new ArrayList<String>()).//

                // Languages
                languages(langs).//

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
                customApp(customApp). //
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
