package weatherpikt.model

import javafx.scene.image.Image
import tornadofx.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class ForecastType {
    HOURLY, DAILY
}

/**
 * Model class with JavaFX properties for holding forecast data
 */
class ForecastDataModel(val index: Int) {

    /**
     * The type of forecast, either daily or hourly
     */
    var type: ForecastType by property(ForecastType.DAILY)
    fun typeProperty() = getProperty(ForecastDataModel::type)

    /**
     * The date and time the forecast applies to
     */
    var dateTime: Instant by property(Instant.now())
    fun dateTimeProperty() = getProperty(ForecastDataModel::dateTime)

    var timeZone: String by property("")
    fun timeZoneProperty() = getProperty(ForecastDataModel::timeZone)

    /**
     * The icon string retrieved from the forecast
     */
    var icon: String by property("")
    fun iconProperty() = getProperty(ForecastDataModel::icon)

    /**
     * Binding to the icon property that returns an Image object whenever the icon property changes
     */
    val iconImage = iconProperty().objectBinding { iconAsImage() }

    /**
     * The low temperature in the forecast
     */
    var temperatureLow: Double by property(0.0)
    fun temperatureLowProperty() = getProperty(ForecastDataModel::temperatureLow)

    /**
     * String binding to the low temperature property that returns the temperature as an int with degree symbol
     */
    val temperatureLowDegrees = temperatureLowProperty().stringBinding { "${it?.toInt()}${Typography.degree}" }

    /**
     * The high temperature in the forecast
     */
    var temperatureHigh: Double by property(0.0)
    fun temperatureHighProperty() = getProperty(ForecastDataModel::temperatureHigh)

    /**
     *  String binding to the high temperature property that returns the temperature as an int with degree symbol
     */
    val temperatureHighDegrees = temperatureHighProperty().stringBinding { "${it?.toInt()}${Typography.degree}" }

    /**
     * The forecast precipitation chance
     */
    var precipitation: Double by property(0.0)
    fun precipitationProperty() = getProperty(ForecastDataModel::precipitation)

    /**
     * String binding to precipitation property that returns precipitation as an integer percent
     */
    val precipitationPercent = precipitationProperty().stringBinding { "${it?.times(100)?.toInt() ?: 0}%" }

    /**
     * The forecast humidity
     */
    var humidity: Double by property(0.0)
    fun humidityProperty() = getProperty(ForecastDataModel::humidity)

    /**
     * String binding to the humidity property that returns humidity as a integer percent
     */
    val humidityPercent = humidityProperty().stringBinding { "${it?.times(100)?.toInt() ?: 0}%" }

    /**
     * The forecast pressure
     */
    var pressure: Double by property(0.0)
    fun pressureProperty() = getProperty(ForecastDataModel::pressure)

    /**
     * The forecast summary
     */
    var summary: String by property("")
    fun summaryProperty() = getProperty(ForecastDataModel::summary)


    /**
     * Returns the forecast date time as a string that represents the forecast
     *
     */
    val dateTimeAsString = dateTimeProperty().stringBinding {
        // If hourly forecast, pattern is hour and am or pam
        // If daily forecast, just the day
        val pattern = if (type == ForecastType.HOURLY) "h a" else "EEEE"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        // Use zone id of forecast area so the hour or day refers
        // to the time at the destination
        val zoneId = try {
            ZoneId.of(timeZone)
        } catch (e: Exception) {
            ZoneId.systemDefault()
        }
        val dt = LocalDateTime.ofInstant(dateTime, zoneId)
        formatter.format(dt)
    }

    /**
     * Returns an Image object created from the icon string returned by the forecast
     */
    fun iconAsImage(): Image? {
        // icon strings are in the form "day-condition" for day and "night-condition" for night
        var icon = icon.replace("-", "").replace("day", "")
        if (icon.contains("night")) icon = "nt_${icon.replace("night", "")}"

        // try to load the image with the translated icon to file name
        return try {
            Image("icons/$icon.png")
        } catch (e: Exception) {
            // load failed, try using the unknown icon as a place holder
            try {
                Image("icons/unknown.png")
            } catch (e: Exception) {
                // all failed, return null
                println(e)
                null
            }
        }
    }
}