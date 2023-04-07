package com.pmoust.smsfwd;

import org.junit.Test;

import static org.junit.Assert.*;

public class SmsReceiverTest {

    @Test
    public void isSamePhoneNumberTest() {
        SmsReceiver smsReceiver = new SmsReceiver();

        // Test with different phone numbers
        assertFalse(smsReceiver.isSamePhoneNumber("+12345678901", "+19876543210"));

        // Test with the same phone numbers (with and without country code)
        assertTrue(smsReceiver.isSamePhoneNumber("+12345678901", "2345678901"));
        assertTrue(smsReceiver.isSamePhoneNumber("2345678901", "+12345678901"));
        assertTrue(smsReceiver.isSamePhoneNumber("2345678901", "2345678901"));
    }
}