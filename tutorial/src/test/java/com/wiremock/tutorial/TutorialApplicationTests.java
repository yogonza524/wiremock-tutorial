package com.wiremock.tutorial;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TutorialApplicationTests {

    private int port = 8089;
    private String realServer = "https://jsonplaceholder.typicode.com";
    private String fakeServer = "http://localhost:" + port;
    private String body = "{\n" +
        "  \"id\": 1,\n" +
        "  \"name\": \"Leanne Graham\",\n" +
        "  \"username\": \"Bret\",\n" +
        "  \"email\": \"Sincere@april.biz\",\n" +
        "  \"address\": {\n" +
        "    \"street\": \"Kulas Light\",\n" +
        "    \"suite\": \"Apt. 556\",\n" +
        "    \"city\": \"Gwenborough\",\n" +
        "    \"zipcode\": \"92998-3874\",\n" +
        "    \"geo\": {\n" +
        "      \"lat\": \"-37.3159\",\n" +
        "      \"lng\": \"81.1496\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"phone\": \"1-770-736-8031 x56442\",\n" +
        "  \"website\": \"hildegard.org\",\n" +
        "  \"company\": {\n" +
        "    \"name\": \"Romaguera-Crona\",\n" +
        "    \"catchPhrase\": \"Multi-layered client-server neural-net\",\n" +
        "    \"bs\": \"harness real-time e-markets\"\n" +
        "  }\n" +
        "}";
    
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port);
    
    @Test
    public void contextLoads() {
    }
    
    @Test
    @Description("Test an User from external service")
    public void testUser() {
//        1. Stub the endpoint
        stubFor(get(urlPathMatching("/users/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)
                )
        );
        
        //2. Make a request
        RestTemplate restTemplate = new RestTemplate();
        String resourceURL = fakeServer;
        ResponseEntity<String> response = restTemplate
                .getForEntity(resourceURL + "/users/1", String.class);
        
        //3. Verify
        assertNotNull(response);
        assertTrue("Status code not equals to 200",response.getStatusCode().equals(HttpStatus.OK));
        assertTrue("Contains fail", response.getBody().contains("\"name\": \"Leanne Graham\""));
    }

}
