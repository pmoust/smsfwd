package com.pmoust.smsfwd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import java.util.Locale;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SHARED_PREFS = "SMSForwarderPrefs";
    private static final String PHONE_NUMBER_KEY = "destinationPhoneNumber";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            SmsMessage[] messages = extractSmsMessages(intent.getExtras());
            for (SmsMessage message : messages) {
                String senderPhoneNumber = message.getDisplayOriginatingAddress();
                String messageText = "From: " + senderPhoneNumber + " " + message.getDisplayMessageBody();

                // Retrieve the destination phone number from SharedPreferences
                String destinationPhoneNumber = getDestinationPhoneNumber(context);

                if (!destinationPhoneNumber.isEmpty() && !isSamePhoneNumber(senderPhoneNumber, destinationPhoneNumber)) {
                    // Forward the SMS
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(destinationPhoneNumber, null, messageText, null, null);
                } else {
                    // Phone number not set, don't forward the SMS
                }
            }
        }
    }

    private SmsMessage[] extractSmsMessages(Bundle bundle) {
        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus == null || pdus.length == 0) {
            return new SmsMessage[0];
        }

        SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }
        return messages;
    }

    private String getDestinationPhoneNumber(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PHONE_NUMBER_KEY, "");
    }

    boolean isSamePhoneNumber(String senderPhoneNumber, String destinationPhoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String defaultRegion = Locale.getDefault().getCountry();
        try {
            Phonenumber.PhoneNumber senderNumber = phoneNumberUtil.parse(senderPhoneNumber, defaultRegion);
            Phonenumber.PhoneNumber destinationNumber = phoneNumberUtil.parse(destinationPhoneNumber, defaultRegion);

            // Strip the country code from both numbers
            String senderNationalNumber = String.valueOf(senderNumber.getNationalNumber());
            String destinationNationalNumber = String.valueOf(destinationNumber.getNationalNumber());

            return senderNationalNumber.equals(destinationNationalNumber);
        } catch (NumberParseException e) {
            e.printStackTrace();
            return false;
        }
    }

}