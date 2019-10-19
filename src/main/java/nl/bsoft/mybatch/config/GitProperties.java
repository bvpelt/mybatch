package nl.bsoft.mybatch.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Slf4j
@Component
@PropertySource("classpath:git.properties")
public @Data
class GitProperties {

    @Value("${git.branch}")
    private String branch;

    @Value("${git.build.host}")
    private String host;

    @Value("${git.build.time}")
    private String buildTime;

    @Value("${git.build.user.email}")
    private String buildUserEmail;

    @Value("${git.build.user.name}")
    private String buildUserName;

    @Value("${git.build.version}")
    private String buildVersion;

    /*
    git.closest.tag.commit.count=2
    git.closest.tag.name=V0.8
    git.commit.id=d431b45775a93c2cbe0ed31bc4d13f8811130104
    git.commit.id.abbrev=d431b45
    git.commit.id.describe=V0.8-2-gd431b45-dirty
    git.commit.id.describe-short=V0.8-2-dirty
    git.commit.message.full=Added profiling link
    git.commit.message.short=Added profiling link
    git.commit.time=2019-10-18T22\:04\:08+0200
    git.commit.user.email=bart.vanpelt@gmail.com
    git.commit.user.name=bart.vanpelt@gmail.com
    git.dirty=true
    git.local.branch.ahead=0
    git.local.branch.behind=0
    git.remote.origin.url=git@github.com\:bvpelt/mybatch.git
    git.tags=
    git.total.commit.count=23

     */

}
