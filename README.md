# DoIt2Day-Service

Project: Server-side application
Generated according to the [DoIt2Day API OpenAPI specification](src/main/resources/META-INF/openapi.json).
After code generation it's better to update libraries for the latest versions to prevent possible security issues.

[[_TOC_]]

# Options

The code was generated with the following options:

|Option|Value|
|---|---|
|Language|Java 17|
|Framework|Spring Boot 3.1.6|
|Build tool|Gradle (Groovy) 8.1.1|

# Pipeline <a name="pipeline"></a>

The pipeline contains the following stages for continuous integration and deployment of the application:

- **[build](#build)**: build, test, package the sources
- **[check](#check)**: perform project source code checks
- **[publish](#container)**: create a docker image and publish to a docker registry

> To view the visual representation of pipeline stages and jobs, click "CI-CD" -> "Editor" -> "Visualize" in your project repo

Subsequent sections describe each of these stages in details

Make sure the following environment variables are set to proper values:

|Name|Place|Description|
|---|---|---|
|DOCKER_AUTH_CONFIG|Credentials for the CoF image registry. Provides base images, GitLab runner images, and as well allows to push application images. Please find tokens here: [Access CoF Images](https://wiki.telekom.de/pages/viewpage.action?pageId=1409264063)|GitLab CI-CD variables|

## Build <a name="build"></a>

The initial source code will be supplemented with automatically generated code for API models, controllers and interfaces.

### Build the application

> At the build stage of the pipeline this task is run automatically by build.yml job, but you can also run it manually

Run `gradlew build`

When completed, the built JAR can be found in `build/libs`

### Maven repositories

In build.gradle 'asf-libs-maven-local' artifactory repository is added additionally to mavenCentral().
In settings.gradle 'asf-libs-maven-local' artifactory repository is added additionally to gradlePluginPortal().

Artifactory repository 'asf-libs-maven-local' is accessible from the intranet Telekom network without credentials.
To access libraries in artifactory from the internet, you should extend the configs with the access token as described in [the guide](https://docs.gradle.org/current/userguide/declaring_repositories.html):

```yml
maven {
  url https://artifactory.devops.telekom.de/artifactory/asf-libs-maven-local/
  credentials {
    username "$System.env.ARTIFACTORY_USER"
    password "$System.env.ARTIFACTORY_TOKEN"
  }
}
```

ARTIFACTORY_USER and ARTIFACTORY_TOKEN should be added as environment variables on your computer.
To generate the token, you can go to the [artifactory UI](https://artifactory.devops.telekom.de/ui/packages), login with GitLab account (using SAML) and in user setting generate API Key.

### OpenAPI based code generation <a name="openApiGeneration"></a>

Models and API classes are generated by OpenAPI Generator plugin.
This project uses [OpenAPITools release](https://github.com/OpenAPITools/openapi-generator) of the plugin.

> OpenAPI Generator plugin provides a task that executes code generation.
> The task runs automatically when the build/test task is triggered. You can also run it separately.

> Due to a bug in OpenAPI Generator's Spring related generator, in some cases if there is inheritance between objects where the superclass has _**x-nullable: true**_ properties, the OpenAPI generation will produce a compilation error at the model package. To prevent this, we have modified the original templates, which can be found in the following folder in your project: _**/src/main/resources/custom-openapi-templates**_. If you do not want to use these custom templates you can delete them, in which case you should also delete the following line of code from the openapi generator plugin: _**templateDir &#x3D; &quot;$projectDir/src/main/resources/custom-openapi-templates&quot;**_

Run `gradlew openApiGenerate`

The following sources will be generated in corresponding directories:

|Path|Description|
|---|---|
|build/generated-sources/src/main/java/onsite/academy/api|API|
|build/generated-sources/src/main/java/onsite/academy/model|Model|

### Run the application locally <a name="runLocally"></a>

To start the application locally, execute the following task from your IDE or command line.
Run `gradlew bootRun`

*By default, authentication will be deactivated, when you run the application [locally](#runLocallyWithoutAuth). Refer to the [Authentication](#authentication) section for further information.*

### Endpoints

The generated application exposes the API with default generated controllers.
The default context root is "/".

The API could be browsed in the swagger-ui dashboard:
* localhost: http://localhost:8080/swagger-ui/index.html

## Check <a name="check"></a>

### Code Quality <a name="codeQuality"></a>

At the `check` stage `code_quality` job inspects your code for compliance with conventions and standards.
Code Quality uses the open source Code Climate tool to analyze your source code.

At the end of `code_quality` job you will see an additional `Code Quality`
tab in your merge request and pipeline page.

> In the current Code Quality version merge requests are not blocked if violations were detected.

You can disable the `code_quality` job using the following environment variable:

|Variable|Example|Description|Location|
|---|---|---|---|
|ENABLE_CODE_QUALITY|true|flag for enabling the `code_quality` job in a pipeline|.gitlab-ci.yml or GitLab CI-CD variables|

[Read more about GitLab Code Quality](https://docs.gitlab.com/ee/user/project/merge_requests/code_quality.html)

# Security <a name="security"></a>

The application is pre-configured for OAuth2.0 based authentication flow, that uses StarGate Gateway as identity provider.

According to the [StarGate handbook](https://developer.telekom.de/docs/src/tardis_customer_handbook/StarGate/#enhanced-last-mile-security-gateway-token),
the Access Token is generated by the StarGate and forwarded to the service as HTTP-header field `Authorization`.
It allows the called service to check if the originating call was coming from a trusted StarGate.

The validation of Access Token is handled by the Spring security using the values defined in:
- [Helm charts](#charts) for remote deployment
- application.yaml file for local run

## How to change identity provider

If your service is not published on the StarGate, you can change the identity provider.

Please adjust `issuer-uri` and `jwk-set-uri` according to your identity provider.

Example for [TARDIS Identity Provider](https://developer.telekom.de/docs/src/tardis_customer_handbook/Iris/) in application.yaml file:

```
spring:
 security:
  oauth2:
   resourceserver:
    jwt:
     issuer-uri: https://iris-playground.live.dhei.telekom.de/auth/realms/default
     jwk-set-uri: https://iris-playground.live.dhei.telekom.de/auth/realms/default/protocol/openid-connect/certs
```

## Local run without authentication <a name="runLocallyWithoutAuth"></a>

For local run security disabled as the default Spring profile set to `default`.
When you run application locally, and the profile is not set or set to `default` the security is disabled.

In order to enable security for local run, set the profile to any non-default value.
You can do this via CLI parameter

`-Dspring.profiles.active=dev`

> When you deploy your application to the `dev`, `uat`, `prod`, environment name
> is passed to `SPRING_PROFILES_ACTIVE` variable via `Deployment.yaml` chart. So that, security is enabled.

You can read more about profiles [here](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)

# Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.18/gradle-plugin/reference/htmlsingle/)
* [OpenApi Generator Gradle Plugin](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.1.6/reference/htmlsingle/#web)

# More information

For more information regarding the Code Factory features, please refer to the [documentation](https://docs.devops.telekom.de/documentation/code-factory/)
or [contact us](https://docs.devops.telekom.de/support/cof/) in case of questions.