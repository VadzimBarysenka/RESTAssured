package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class HealthCheckTest extends BaseTest {
  private final static String URL = "/ping";
  private static final int EXPECTED_STATUS_CODE = 201;

  @Test
  public void healthCheckTest() {
    given(spec)
        .when()
        .get(URL)
        .then()
        .assertThat().statusCode(EXPECTED_STATUS_CODE);
  }

  @Test
  public void headersAndCookiesTest() {
    Header additionalHeader = new Header("Additional header", "Its value");
    spec.header(additionalHeader);

    Cookie additionalCookie = new Cookie.Builder("Additional cookie", "Its value").build();
    spec.cookie(additionalCookie);

    Response response = RestAssured.given(spec)
        .cookie("Test cookie name", "Test cookie value")
        .header("Test header name", "Test header value")
        .log().all()
        .get(URL);

    Headers headers = response.getHeaders();
    System.out.println("Headers: " + headers);
    System.out.println(response.getHeader("Server"));
    Cookies cookies = response.getDetailedCookies();
    System.out.println("Cookies: " + cookies);
  }
}
