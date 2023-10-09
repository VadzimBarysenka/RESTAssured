package com.herokuapp.restfulbooker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;

public class GetBookingTest extends BaseTest {
  private final Booking booking;
  public GetBookingTest() {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      booking = objectMapper.readValue(new File("src/main/resources/Booking.json"), Booking.class);
    } catch (IOException e) {
      throw new RuntimeException("No file");
    }
  }

  @Test
  private void getBookingPOJOTest() {
    Response responseCreate = createBooking(booking);
    spec.pathParam("bookingId", responseCreate.jsonPath().getInt("bookingid"));
    Response response = RestAssured.given(spec).get("/booking/{bookingId}");

    Assert.assertEquals(response.getStatusCode(), 200, "Incorrect status code returned");

    Assert.assertEquals(response.as(Booking.class).toString(), booking.toString());
  }
/*
  @Test
  private void getBookingTest() {
    Response responseCreate = createBooking(booking);
    responseCreate.print();

    spec.pathParam("bookingId", responseCreate.jsonPath().getInt("bookingid"));
    Response response = RestAssured.given(spec).get("/booking/{bookingId}");
    response.print();

    Assert.assertEquals(response.getStatusCode(), 200, "Incorrect status code returned");

    SoftAssert softAssert = new SoftAssert();

    softAssert.assertEquals(response.jsonPath().getString("firstname"), "John", "Wrong firstname returned");
    softAssert.assertEquals(response.jsonPath().getString("lastname"), "Doe", "Wrong lastname returned");
    softAssert.assertEquals(response.jsonPath().getInt("totalprice"), 150, "Wrong total price returned");
    softAssert.assertTrue(response.jsonPath().getBoolean("depositpaid"));
    softAssert.assertEquals(response.jsonPath().getString("bookingdates.checkin"), "2023-07-15", "Wrong checkin date returned");
    softAssert.assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2023-08-15", "Wrong checkout date returned");

    softAssert.assertAll();
  }

  @Test
  private void getBookingXMLTest() {
    Response responseCreate = createBooking(booking);
    responseCreate.print();

    spec.pathParam("bookingId", responseCreate.jsonPath().getInt("bookingid"));

    Header xmlHeader = new Header("Accept", "application/xml");
    spec.header(xmlHeader);

    Response response = RestAssured.given(spec).get("/booking/{bookingId}");
    response.print();

    Assert.assertEquals(response.getStatusCode(), 200, "Incorrect status code returned");

    SoftAssert softAssert = new SoftAssert();

    softAssert.assertEquals(response.xmlPath().getString("booking.firstname"), "John", "Wrong firstname returned");
    softAssert.assertEquals(response.xmlPath().getString("booking.lastname"), "Doe", "Wrong lastname returned");
    softAssert.assertEquals(response.xmlPath().getInt("booking.totalprice"), 150, "Wrong total price returned");
    softAssert.assertTrue(response.xmlPath().getBoolean("booking.depositpaid"));
    softAssert.assertEquals(response.xmlPath().getString("booking.bookingdates.checkin"), "2023-07-15", "Wrong checkin date returned");
    softAssert.assertEquals(response.xmlPath().getString("booking.bookingdates.checkout"), "2023-08-15", "Wrong checkout date returned");

    softAssert.assertAll();
  }

 */
}
