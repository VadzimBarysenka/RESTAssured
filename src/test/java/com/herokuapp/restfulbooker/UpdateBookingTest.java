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

public class UpdateBookingTest extends BaseTest {
  private final Booking booking;
  public UpdateBookingTest() {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      booking = objectMapper.readValue(new File("src/main/resources/Booking.json"), Booking.class);
    } catch (IOException e) {
      throw new RuntimeException("No file");
    }
  }

  @Test
  private void updateBookingPOJOTest() {
    Response response = createBooking(booking);
    Bookingdates bookingdates = new Bookingdates("2023-09-11", "2023-10-30");
    Booking newBooking = new Booking("Frodo", "Baggins", 1000, true, bookingdates, "Ring of Power");

    Response updatedResponse = RestAssured.given().auth().preemptive().basic("admin", "password123")
        .contentType(ContentType.JSON).body(newBooking).put(String.format("https://restful-booker.herokuapp.com/booking/%s", response.jsonPath().getInt("bookingid")));
    updatedResponse.print();

    Assert.assertEquals(updatedResponse.getStatusCode(), 200, String.format("Status code %s returned instead of %s", updatedResponse.getStatusCode(), 200));

    Assert.assertEquals(updatedResponse.as(Booking.class).getFirstname(), "Frodo");
  }

  @Test
  private void updateBookingTest() {
    Response response = createBooking(booking);
    response.print();

    int bookingId = response.jsonPath().getInt("bookingid");

    JSONObject body = new JSONObject();
    body.put("firstname", "Jane");
    body.put("lastname", "Doe");
    body.put("totalprice", 340);
    body.put("depositpaid", false);

    JSONObject bookingdates = new JSONObject();
    bookingdates.put("checkin", "2023-07-15");
    bookingdates.put("checkout", "2023-08-15");

    body.put("bookingdates", bookingdates);
    body.put("additionalneeds", "Diner");

    Response updatedResponse = RestAssured.given().auth().preemptive().basic("admin", "password123")
        .contentType(ContentType.JSON).body(body.toString()).put(String.format("https://restful-booker.herokuapp.com/booking/%s", bookingId));
    updatedResponse.print();

    Assert.assertEquals(updatedResponse.getStatusCode(), 200, String.format("Status code %s returned instead of %s", updatedResponse.getStatusCode(), 200));

    SoftAssert softAssert = new SoftAssert();

    softAssert.assertEquals(updatedResponse.jsonPath().getString("firstname"), "Jane", "Wrong firstname returned");
    softAssert.assertEquals(updatedResponse.jsonPath().getString("lastname"), "Doe", "Wrong lastname returned");
    softAssert.assertEquals(updatedResponse.jsonPath().getInt("totalprice"), 340, "Wrong total price returned");
    softAssert.assertFalse(updatedResponse.jsonPath().getBoolean("depositpaid"));
    softAssert.assertEquals(updatedResponse.jsonPath().getString("bookingdates.checkin"), "2023-07-15", "Wrong checkin date returned");
    softAssert.assertEquals(updatedResponse.jsonPath().getString("bookingdates.checkout"), "2023-08-15", "Wrong checkout date returned");
    softAssert.assertEquals(updatedResponse.jsonPath().getString("additionalneeds"), "Diner");

    softAssert.assertAll();
  }
}
