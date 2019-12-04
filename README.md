# JSC Google Drive

Java based Google Drive data synchronization.

## Initialization

From the root directory run the command

```
gradle wrapper
```

This will create the gradle wrapper files. It is not mandatory but depending on your environment it might be needed.

Then build the project:
```
gradle clean
gradle build
```

Go to the Google Developer Console and create a new API. Download the associated client Id and secret and save it to replace the `src/main/resources/credentials.json` file.

Create the folder `drive` then run the application:

```
gradle run
```

This is a work in progress, default folders and details about how to create the API settings in Google Developer Console will come soon.