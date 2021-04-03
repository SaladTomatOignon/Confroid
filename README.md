<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/SaladTomatOignon/Confroid">
    <img src="Confroid/assets/images/confroid_logo.png" alt="Logo" width="100" height="100">
  </a>

  <h3 align="center">Confroid</h3>

  <p align="center">
    Android storage application and centralized configuration management library
    <br />
    <a href="https://github.com/SaladTomatOignon/Confroid/Documentation"><strong>Go to the documentation »</strong></a>
    <br />
    <br />
    <a href="https://github.com/SaladTomatOignon/Confroid/tree/main/Confroid%20storage%20service">Web service</a>
    ·
    <a href="https://github.com/SaladTomatOignon/Confroid/tree/main/Confroid">Confroid application</a>
    ·
    <a href="https://github.com/SaladTomatOignon/Confroid/tree/main/ConfroidDemo">Demo application</a>
    ·
    <a href="https://github.com/othneildrew/Best-README-Template/issues">Report bug</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#authors">Authors</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

Confroid is an Android application allowing centralized configurations management. Any application installed on the device will have the option of entrusting the storage of its configuration to Confroid rather than performing this task itself.
The user will be able to edit the configurations of an application using Confroid.
The configurations will be stored in a transactional mode with a version history. 

In order to make the Confroid usage easier, an Android library is provided to interact with the application. So Confroid users won't have to worry about managing intents, authentication and more. 


### Built With

The Confroid application is fully developed in Java and uses the following dependencies :

* [Google Gson](https://github.com/google/gson)
* [Android Volley](https://developer.android.com/training/volley)
* [Various AndroidX packages](https://developer.android.com/jetpack/androidx)

The Confroid Web service is also developed in Java, using Spring framework with Hibernate.



<!-- GETTING STARTED -->
## Getting Started

This section will show you how to install the Confroid application and the web server.

### Prerequisites

Before getting started, you need to download a copy of the latest version of the repository :

```sh
git clone https://github.com/SaladTomatOignon/Confroid.git
```

If you want to use the provided web service, you will have to install [Maven](https://mkyong.com/maven/how-to-install-maven-in-windows/) in order to build the application.

### Installation

#### Confroid application

First you need to build the Confroid application. Go to the root application and type this command :
* Unix
```sh
gradlew assembleDebug
```

* Windows
```sh
gradlew.bat assembleDebug
```
This will generate the resulting APK which can be found at _Confroid/app/build/outputs/apk/app-debug.apk_

You can also open an IDE like Android Studio to build and run from a (virtual) Android device.

#### Confroid web storage service

Go to the application root, you will find the file at _src/main/resources/application.properties_. From the file you can configure you database access. Provide your database driver and yours logins informations.
By default, a postgreSQL driver is used with username *postgres* and password *postgres*.

When edited, you can start the server with maven command :

```sh
mvn spring-boot:run
```

Or build and run it from an IDE like IntelliJ.


<!-- USAGE EXAMPLES -->
## Usage

When the Confroid application and the web service are started, you can link the both applications from Confroid in the settings view.
On the URL field, type your IP address, in the username and password fields, type informations that are stored in the web service database, you will have to create a user if none exist (a demo dump database is provided).

_For more details, please refer to the [Documentation](https://github.com/SaladTomatOignon/Confroid/Documentation)_.

The web service connection is optional and it does not prevent you to use Confroid if you don't want to use it.


<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/othneildrew/Best-README-Template/issues) for a list of proposed features (and known issues).


<!-- CONTRIBUTING -->
## Contributing

This project is open source and you can contribute by submiting new features.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- AUTHORS -->
## Authors

* [CISSE Mamadou](https://github.com/mciissee)
* [SANCHES FERNANDES Stéphane](https://github.com/steph-sanches)
* [TRAN Éric](https://github.com/etran2907)
* [WADAN Samy](https://github.com/SaladTomatOignon)
* [ZEMMOUR Axel](https://github.com/axel-zemmour)
* [ZULAL Volkan](https://github.com/volkanzulal)