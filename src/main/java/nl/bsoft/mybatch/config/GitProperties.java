package nl.bsoft.mybatch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


@Slf4j
@Component
public
class GitProperties implements ResourceLoaderAware {

    public static String BRANCH = "git.branch";
    public static String BUILD_TIME = "git.build.time";
    public static String BUILD_USER_EMAIL = "git.build.user.email";
    public static String BUILD_USER_NAME = "git.build.user.name";
    public static String BUILD_VERSION = "git.build.version";
    public static String CLOSEST_TAG_COMMIT_COUNT = "git.closest.tag.commit.count";
    public static String CLOSEST_TAG_NAME = "git.closest.tag.name";
    public static String COMMIT_ID = "git.commit.id";
    public static String COMMIT_ID_ABBREV = "git.commit.id.abbrev";
    public static String COMMIT_ID_DESCRIBE = "git.commit.id.describe";
    public static String COMMIT_ID_DESCRIBE_SHORT = "git.commit.id.describe-short";
    public static String COMMIT_MESSAGE_FULL = "git.commit.message.full";
    public static String COMMIT_MESSAGE_SHORT = "git.commit.message.short";
    public static String COMMIT_TIME = "git.commit.time";
    public static String COMMIT_USER_EMAIL = "git.commit.user.email";
    public static String COMMIT_USER_NAME = "git.commit.user.name";
    public static String DIRTY = "git.dirty";
    public static String HOST = "git.build.host";
    public static String LOCAL_BRANCH_AHEAD = "git.local.branch.ahead";
    public static String LOCAL_BRANCH_BEHIND = "git.local.branch.behind";
    public static String REMOTE_ORIGIN_URL = "git.remote.origin.url";
    public static String TAGS = "git.tags";
    public static String TOTAL_COMMIT_COUNT = "git.total.commit.count";

    public static List<String> GIT_PROPERTIES = Arrays.asList(
            BRANCH,
            BUILD_TIME,
            BUILD_USER_EMAIL,
            BUILD_USER_NAME,
            BUILD_VERSION,
            CLOSEST_TAG_COMMIT_COUNT,
            CLOSEST_TAG_NAME,
            COMMIT_ID,
            COMMIT_ID_ABBREV,
            COMMIT_ID_DESCRIBE,
            COMMIT_ID_DESCRIBE_SHORT,
            COMMIT_MESSAGE_FULL,
            COMMIT_MESSAGE_SHORT,
            COMMIT_TIME,
            COMMIT_USER_EMAIL,
            COMMIT_USER_NAME,
            DIRTY,
            HOST,
            LOCAL_BRANCH_AHEAD,
            LOCAL_BRANCH_BEHIND,
            REMOTE_ORIGIN_URL,
            TAGS,
            TOTAL_COMMIT_COUNT
    );

    private ResourceLoader resourceLoader = new DefaultResourceLoader(getClass().getClassLoader());
    private Properties properties = null;

    public GitProperties() {
        this.properties = getGitProperties();
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Properties getGitProperties() {
        InputStream inputStream = null;
        Properties properties = null;
        Resource resource = resourceLoader.getResource("classpath:git.properties");
        try {
            inputStream = resource.getInputStream();
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("Cannot read properties: {}", e);
        }
        return properties;
    }

    public String getPropertie(final String propertyName) {
        String result = "";
        result = (String) this.properties.get(propertyName);
        return result;
    }

    // git.branch=skippolicy
    public String getBranch() {
        return (String) this.properties.get(BRANCH);
    }

    //git.build.host=pluto
    public String getHost() {
        return (String) this.properties.get(HOST);
    }

    //git.build.time=2019-10-19T12\:11\:40+0200
    public String getBuildTime() {
        return (String) this.properties.get(BUILD_TIME);
    }

    //git.build.user.email=bart.vanpelt@gmail.com
    public String getBuildUserEmail() {
        return (String) this.properties.get(BUILD_USER_EMAIL);
    }

    //git.build.user.name=bart.vanpelt@gmail.com
    public String getBuildUserName() {
        return (String) this.properties.get(BUILD_USER_NAME);
    }

    //git.build.version=0.0.1-SNAPSHOT
    public String getBuildVersion() {
        return (String) this.properties.get(BUILD_VERSION);
    }

    //git.closest.tag.commit.count=3
    public String getClosestTagCommitCount() {
        return (String) this.properties.get(CLOSEST_TAG_COMMIT_COUNT);
    }

    //git.closest.tag.name=V0.8
    public String getClosestTagName() {
        return (String) this.properties.get(CLOSEST_TAG_NAME);
    }

    //git.commit.id=cbcf929aac1250ad0e2b276b14ff08f1a535e0f1
    public String getCommitId() {
        return (String) this.properties.get(COMMIT_ID);
    }

    //git.commit.id.abbrev=cbcf929
    public String getCommitIdAbbrev() {
        return (String) this.properties.get(COMMIT_ID_ABBREV);
    }

    //git.commit.id.describe=V0.8-3-gcbcf929a-dirty
    public String getCommitIdDescribe() {
        return (String) this.properties.get(COMMIT_ID_DESCRIBE);
    }

    //git.commit.id.describe-short=V0.8-3-dirty
    public String getCommitIdDescribeShort() {
        return (String) this.properties.get(COMMIT_ID_DESCRIBE_SHORT);
    }

    //git.commit.message.full=Trying to insert gitinfo
    public String getCommitMessageFull() {
        return (String) this.properties.get(COMMIT_MESSAGE_FULL);
    }

    //git.commit.message.short=Trying to insert gitinfo
    public String getCommitMessageShort() {
        return (String) this.properties.get(COMMIT_MESSAGE_SHORT);
    }

    //git.commit.time=2019-10-19T11\:45\:49+0200
    public String getCommitTime() {
        return (String) this.properties.get(COMMIT_TIME);
    }

    //git.commit.user.email=bart.vanpelt@gmail.com
    public String getCommitUserEmail() {
        return (String) this.properties.get(COMMIT_USER_EMAIL);
    }

    //git.commit.user.name=bart.vanpelt@gmail.com
    public String getCommitUserName() {
        return (String) this.properties.get(COMMIT_USER_NAME);
    }

    //git.dirty=true
    public String getDirty() {
        return (String) this.properties.get(DIRTY);
    }

    //git.local.branch.ahead=1
    public String getLocalBranchAhead() {
        return (String) this.properties.get(LOCAL_BRANCH_AHEAD);
    }

    //git.local.branch.behind=0
    public String getLocalBranchBehind() {
        return (String) this.properties.get(LOCAL_BRANCH_BEHIND);
    }

    //git.remote.origin.url=git@github.com\:bvpelt/mybatch.git
    public String getRemoteOriginUrl() {
        return (String) this.properties.get(REMOTE_ORIGIN_URL);
    }

    //git.tags=
    public String getTags() {
        return (String) this.properties.get(TAGS);
    }

    //git.total.commit.count=24
    public String getTotalCommitCount() {
        return (String) this.properties.get(TOTAL_COMMIT_COUNT);
    }
}
