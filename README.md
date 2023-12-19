# Insightoid

Insightoid is an Android crash-reporting library. It is designed to help developers monitor and fix issues in their applications by providing detailed crash reports.

## Features

- Crash Reporting: Insightoid captures unhandled exceptions and sends them to a backend server for further analysis.
- Logging: Insightoid provides a logging mechanism that can be enabled or disabled as per your needs.

## Installation

To use Insightoid in your Android project, add the following dependency to your `build.gradle` file:

```groovy
dependencies {
    implementation("io.github.kl3jvi:insightoid:1.0.0")
} 
```

## Usage

To initialize Insightoid in your application, use the following code in your `Application` class:
    
```kotlin
Insightoid.Builder()
    .withContext(this)
    .setApiKey("your-api-key")
    .setEnableCrashReporting(true)
    .setEnableLogging(true)
    .initialize()
```
Replace `"your-api-key"` with your actual API key.

## License

Insightoid is licensed under the Apache Software License, Version 2.0. You can check out the full license [here](https://www.apache.org/licenses/LICENSE-2.0.txt).

For more information, please visit the [project page](https://github.com/kl3jvi/Insightoid/).