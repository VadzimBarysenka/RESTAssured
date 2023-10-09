package com.herokuapp.restfulbooker;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
  protected RequestSpecification spec;

  @BeforeMethod
  public void setUp() {
    spec = new RequestSpecBuilder()
        .addFilter(new AllureRestAssured())
        .setBaseUri("https://restful-booker.herokuapp.com")
        .build();
  }

  protected Response createBooking(Booking booking) {
    return RestAssured.given(spec)
        .contentType(ContentType.JSON).body(booking).post("/booking");
  }
}