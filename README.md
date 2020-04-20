# WeatherPikt
Internet Weather Station

Replacement for [Weather Pike built using Python and Kivy](https://www.github.com/maroc81/WeatherPike)


## Overview

Weather Pikt is a multi-platform internet weather station but originally designed for a Raspberry Pi and a 3.5" LCD. Weather Pikt is built using Kotlin and TornadoFX with forecast data provided by DarkSky (soon to be replaced since Dark Sky will no longer be providing data to third parties).

Weather Pikt includes features such as

- Current time
- Current temperature
- Current conditions
- Multi-day forecast with lo/high temperatures and precipitation percentage
- Dynamic scaling UI for different resolutions

Planned features include

- Hourly forecast
- Detailed forecast
- Configuration UI
- Web based configuration

## TornadoFX

Weather Pikt is built using [TornadoFX](https://tornadofx.io/) the JavaFX framework for Kotlin. It takes JavaFX, the cross platform GUI toolkit, and adds kotlin specific bindings and features to simplify creating GUI apps. TornadoFX includes features such as type-safe builders, dependency injection, delegated properties, etc. which all contribute to reducing the amount of code needed to produce a working application.

Weather Pikt is less than 600 lines, including comments and import statements, for all source files combined in the initial working version.

## Usage

1. Register for an API key from [Dark Sky](https://darksky.net/dev)
2. Install JDK 11+.  For Raspberry Pi, download from https://bell-sw.com/java11
3. Clone this repository
4. Edit `app.properties` and enter your API key, desired GPS coordinates, etc
5. Build and run
```bash
$ ./gradlew build run
```

The above directions should work for your desired platform.  For detailed steps, see DEVELOP.md

TODO: Provide package for Raspbery Pi

## Acknowledgements
 - [Weather Pike](https://www.github.com/maroc81/WeatherPike): The original version written in python and kivy
 - [PiWeatherRock](https://github.com/genebean/PiWeatherRock): The UI for Weather Pikt was (heavily) inspired by this project.  Which in turn was based on the original (as far as I know) Weather Pi written by Jim Kemp.  While Weather Pikt's UI is heavily influenced by these projects, all code is original.
 - [Weather Underground Icons](https://github.com/manifestinteractive/weather-underground-icons)
