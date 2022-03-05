# SpeakTime

A simple app that speaks the time, when the screen turns on. The app listens system broadcast for screen on, and tells the *date* and *time*. You can set start and stop time as desired. Or use the default set time, which is set to night hours.

The main purpose of the app is to listen to the time at night without having to open your eyes.

<img src="/speakTimeScreenShot.png" alt="SpeakTime" width="200"/>

## Features used

1. WorkManager to chain unique work to read schedule time and update if needed

2. foreground service to register broadcast receiver to listen to protected intent sent by the system

3. broadcast receiver to listen to screen on event, stop service when schedule time is reached, and update the schedule when device is booted