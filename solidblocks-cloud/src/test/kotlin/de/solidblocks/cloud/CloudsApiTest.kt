package de.solidblocks.cloud

import de.solidblocks.cloud.api.CloudApiHttpServer
import de.solidblocks.cloud.auth.api.AuthApi
import de.solidblocks.cloud.clouds.api.CloudsApi
import de.solidblocks.cloud.model.generateRsaKeyPair
import de.solidblocks.test.TestEnvironment
import de.solidblocks.test.TestEnvironmentExtension
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestEnvironmentExtension::class)
class CloudsApiTest {

    val keyPair = generateRsaKeyPair()
    val httpServer = CloudApiHttpServer(privateKey = keyPair.first, publicKey = keyPair.second, port = -1)

    @Test
    fun testCloudsApi(testEnvironment: TestEnvironment) {
        AuthApi(
            httpServer,
            testEnvironment.repositories.clouds,
            testEnvironment.repositories.environments,
            testEnvironment.managers.users
        )
        CloudsApi(httpServer, testEnvironment.managers.clouds)

        val token = given().port(httpServer.port).login()

        // access without token is denied
        given().port(httpServer.port).get("/api/v1/clouds").then().assertThat()
            .statusCode(401)

        // get list of clouds
        given().port(httpServer.port).withAuthToken(token).get("/api/v1/clouds").then().assertThat()
            .statusCode(200).assertThat().body("clouds.size()", `is`(0))

        testEnvironment.testContext.createCloud("cloud1")

        val cloudId = given().port(httpServer.port).with().withAuthToken(token).get("/api/v1/clouds").then()
            .assertThat()
            .statusCode(200).assertThat()
            .body("clouds.size()", `is`(1))
            .assertThat().body("clouds[0].name", `is`("cloud1"))
            .assertThat().body("clouds[0].id", CoreMatchers.notNullValue())
            .extract().body().jsonPath().get<String>("clouds[0].id")

        // get existing cloud
        given().port(httpServer.port).with().withAuthToken(token).get("/api/v1/clouds/{id}", cloudId).then()
            .assertThat()
            .statusCode(200).assertThat()
            .assertThat().body("cloud.name", `is`("cloud1"))
            .assertThat().body("cloud.id", `is`(cloudId))

        // get non existing cloud
        given().port(httpServer.port).with().withAuthToken(token).get("/api/v1/clouds/f4b21644-ba00-46ff-a445-e7ac2e8762f3").then()
            .assertThat()
            .statusCode(404).assertThat()

        // get invalid cloud id
        given().port(httpServer.port).with().withAuthToken(token).get("/api/v1/clouds/xxx").then()
            .assertThat()
            .statusCode(400).assertThat()
    }

    /*
    @Test
    fun testLoadsCloudFromHostHeader(testEnvironment: TestEnvironment) {
        testEnvironment.ensureCloud("localhost")

        CloudsApi(httpServer, testEnvironment.cloudsManager)

        given().port(httpServer.port).with().header("Host", "").get(" ").then()
            .assertThat()
            .statusCode(404)
            .body("messages[0].code", equalTo("cloud_unknown_domain"))

        given().port(httpServer.port).with().header("Host", "localhost").get("/api/v1/clouds").then()
            .assertThat()
            .statusCode(200)
            .body("cloud.name", equalTo("cloud1"))
    }
    */
}
