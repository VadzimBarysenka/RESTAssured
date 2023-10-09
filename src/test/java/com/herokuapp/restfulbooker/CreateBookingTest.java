package com.herokuapp.restfulbooker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;

public class CreateBookingTest extends BaseTest {
  private final Booking booking;
  public CreateBookingTest() {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      booking = objectMapper.readValue(new File("src/main/resources/Booking.json"), Booking.class);
    } catch (IOException e) {
      throw new RuntimeException("No file");
    }
  }

  @Test
  private void createBookingPOJOTest() {
    Response response = createBooking(booking);
    BookingId bookingId = response.as(BookingId.class);

    Assert.assertEquals(response.getStatusCode(), 200, "Incorrect status code returned");
    Assert.assertEquals(bookingId.getBooking().toString(), booking.toString());
  }

  @Test
  private void createBookingTest() {
    Response response = createBooking(booking);

    Assert.assertEquals(response.getStatusCode(), 200, "Incorrect status code returned");

    SoftAssert softAssert = new SoftAssert();

    softAssert.assertEquals(response.jsonPath().getString("booking.firstname"), "John", "Wrong firstname returned");
    softAssert.assertEquals(response.jsonPath().getString("booking.lastname"), "Doe", "Wrong lastname returned");
    softAssert.assertEquals(response.jsonPath().getInt("booking.totalprice"), 150, "Wrong total price returned");
    softAssert.assertTrue(response.jsonPath().getBoolean("booking.depositpaid"));
    softAssert.assertEquals(response.jsonPath().getString("booking.bookingdates.checkin"), "2023-07-15", "Wrong checkin date returned");
    softAssert.assertEquals(response.jsonPath().getString("booking.bookingdates.checkout"), "2023-08-15", "Wrong checkout date returned");
    softAssert.assertEquals(response.jsonPath().getString("booking.additionalneeds"), "Breakfast");

    softAssert.assertAll();
  }
}