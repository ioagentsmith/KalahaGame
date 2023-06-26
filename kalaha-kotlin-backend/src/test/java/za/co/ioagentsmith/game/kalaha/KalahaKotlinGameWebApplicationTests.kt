package za.co.ioagentsmith.game.kalaha

//import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
class KalahaKotlinGameWebApplicationTests {

    //Hamcrest matchers `is`

    @Autowired
    val restTemplate: TestRestTemplate? = null

    @Test
    @Throws(Exception::class)
    fun testJspWithEl() {
        val entity = this.restTemplate!!.getForEntity("/", String::class.java)
        //assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.statusCode, `is`(equalTo(HttpStatus.OK)))
    }

}
