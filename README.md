# smsfwd

## SMS Forwarder Android Application

SMS Forwarder is a simple Android application that forwards incoming SMS messages to a specified phone number via SMS. The app is designed for users who want to forward messages from one device to another automatically.

Example use-case: Getting TOTP tokens directly to your accountant performing payments via some bank that has extremely poor RBAC and MFA management. 
Note: It's generally not recommended to forward any SMS messages, nor use SMS as 2FA. At all.

### Features

Automatically forwards incoming SMS messages to a predefined phone number.
Runs in the background and doesn't require user interaction once set up.

### Permissions

The application requires the following permissions:

`RECEIVE_SMS`: To receive incoming SMS messages.
`SEND_SMS`: To send SMS messages to the specified phone number.

### Installation

* Clone or download the repository containing the source code.
* Open the project in Android Studio.
* Connect your Android device to your computer with USB debugging enabled.
* Press the "Run" button (green play icon) in the toolbar or press Shift + F10.
* Select your connected device in the "Select Deployment Target" window and click "OK".
* The app will be installed on your device.

### Usage

* Launch the SMS Forwarder app on your device.
* Grant the necessary SMS permissions when prompted.
* Specify the destination SMS number in the app.
* App  will continue running in the background and forward incoming SMS messages to the specified phone number automatically.
* Stop the app to stop forwarding the SMS messages.
