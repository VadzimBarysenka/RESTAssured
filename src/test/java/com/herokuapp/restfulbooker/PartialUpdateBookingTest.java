package com.herokuapp.restfulbooker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;

public class PartialUpdateBookingTest extends BaseTest {

  private final Booking booking;
  public PartialUpdateBookingTest() {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      booking = objectMapper.readValue(new File("src/main/resources/Booking.json"), Booking.class);
    } catch (IOException e) {
      throw new RuntimeException("No file");
    }
  }

  @Test
  private void partialUpdateBookingPOJOTest() {
    Response response = createBooking(booking);
    int bookingId = response.jsonPath().getInt("bookingid");
    booking.setLastname("Bilbo");
    booking.setTotalprice(404);

    Response updatedResponse = RestAssured.given(spec).auth().preemptive().basic("admin", "password123")
        .contentType(ContentType.JSON).body(booking).patch(String.format("/booking/%s", bookingId));
    updatedResponse.print();
    Booking updatedBooking = updatedResponse.as(Booking.class);

    Assert.assertEquals(response.getStatusCode(), 200, "Incorrect status code returned");

    Assert.assertEquals(updatedBooking.toString(), booking.toString());
  }
/*
  @Test
  private void partialUpdateBookingTest() {
    Response response = createBooking(booking);
    response.print();

    int bookingId = response.jsonPath().getInt("bookingid");

    JSONObject partialUpdateBody = new JSONObject();
    partialUpdateBody.put("firstname", "Bormaley");

    JSONObject bookingdates = new JSONObject();
    bookingdates.put("checkin", "2023-07-13");
    bookingdates.put("checkout", "2023-08-15");
    partialUpdateBody.put("bookingdates", bookingdates);

    Response updatedResponse = RestAssured.given(spec).auth().preemptive().basic("admin", "password123")
        .contentType(ContentType.JSON).body(partialUpdateBody.toString()).patch(String.format("/booking/%s", bookingId));
    updatedResponse.print();

    Assert.assertEquals(updatedResponse.getStatusCode(), 200, String.format("Status code %s returned instead of %s", updatedResponse.getStatusCode(), 200));

    SoftAssert softAssert = new SoftAssert();

    softAssert.assertEquals(updatedResponse.jsonPath().getString("firstname"), "Jane", "Wrong firstname returned");
    softAssert.assertEquals(updatedResponse.jsonPath().getString("lastname"), "Doe", "Wrong lastname returned");
    softAssert.assertEquals(updatedResponse.jsonPath().getInt("totalprice"), 150, "Wrong total price returned");
    softAssert.assertTrue(updatedResponse.jsonPath().getBoolean("depositpaid"));
    softAssert.assertEquals(updatedResponse.jsonPath().getString("bookingdates.checkin"), "2023-07-15", "Wrong checkin date returned");
    softAssert.assertEquals(updatedResponse.jsonPath().getString("bookingdates.checkout"), "2023-08-15", "Wrong checkout date returned");
    softAssert.assertEquals(updatedResponse.jsonPath().getString("additionalneeds"), "Breakfast");

    softAssert.assertAll();
  }

 */
}
