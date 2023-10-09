package com.herokuapp.restfulbooker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class DeleteBookingTest extends BaseTest {
  private final Booking booking;

  public DeleteBookingTest() {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      booking = objectMapper.readValue(new File("src/main/resources/Booking.json"), Booking.class);
    } catch (IOException e) {
      throw new RuntimeException("No file");
    }
  }

  @Test
  private void deleteBooking() {
    Response response = createBooking(booking);

    Assert.assertEquals(response.getStatusCode(), 200, "Incorrect status code returned");

    Response deleteResponse = RestAssured.given(spec).auth().preemptive().basic("admin", "password123")
        .delete(String.format("/booking/%s", response.jsonPath().getInt("bookingid")));

    Assert.assertEquals(deleteResponse.getStatusCode(), 201, "Incorrect status code returned");
  }
}