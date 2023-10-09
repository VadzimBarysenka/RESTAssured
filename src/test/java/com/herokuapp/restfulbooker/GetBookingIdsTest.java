package com.herokuapp.restfulbooker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class GetBookingIdsTest extends BaseTest {
  public GetBookingIdsTest() {
  }


  @Test
  private void getBookingIdsWithoutFiltersTest() {
    Response response = RestAssured.given(spec).get("/booking");

    Assert.assertEquals(response.getStatusCode(), 200, "Should be 200");

    List<Integer> bookingIds = response.jsonPath().getList("bookingid");
    Assert.assertFalse(bookingIds.isEmpty(), "Empty ids list");
  }

  @Test
  private void getBookingIdsWithFiltersTest() {
    spec.queryParam("firstname", "Susan");
    Response response = RestAssured.given(spec).get("/booking");

    Assert.assertEquals(response.getStatusCode(), 200, "Should be 200");

    List<Integer> bookingIds = response.jsonPath().getList("bookingid");
    Assert.assertFalse(bookingIds.isEmpty(), "Empty ids list");
  }
}
