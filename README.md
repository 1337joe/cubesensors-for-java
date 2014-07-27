#Cubesensors for Java

A Java library for reading data from the [CubeSensors API](http://my.cubesensors.com/docs)

##Authorization Usage

This library extends [Scribe](https://github.com/fernandezpablo85/scribe-java) to use the CubeSensors OAuth authentication.  That means that creating an OAuthService object is as simple as putting your app API key in cubesensors.properties and calling:

```java
OAuthService service = new ServiceBuilder()
		.provider(CubeSensorsAuthApi.class)
		.apiKey(CubeSensorsProperties.getAppKey())
		.apiSecret(CubeSensorsProperties.getAppSecret())
		.signatureType(SignatureType.QueryString)
		.build();
```

That defaults to using 'oob' for the callback, meaning the api will provide the user with the verifier string in the web browser instead of calling your app to pass it directly.  Specifying a callback should be as simple as adding `.callback("your url here")` to the builder.

For usage examples and instructions on getting an access token see `TestAuth.java`.

##API Usage

Once you've got an access token you can start querying the API for data.  See `TestApi.java` for full examples, but all it takes to get the current state of a cube is:

```java
// create the API instance for a given accessToken
CubeSensorsApiV1 api = new CubeSensorsApiV1(accessToken);
// get the list of devices
List<Device> devices = api.getDevices();
// print current state information for the first device
System.out.print(api.getCurrent(devices.get(0).getUid()));
```

##Notes

I'm just starting work on my app that will use this library.  My tests indicate that the library works, but I may make changes for usability as I start using it more if I come across any major issues.

The CubeSensors API is still in development.  As far as I can tell everything in this library currently works, but updates may be needed if the API itself changes.

I'm using the Java 8 java.time package for dates/times.  If there's demand it shoudn't be hard to put together a branch that either uses the old time classes or Joda-Time.