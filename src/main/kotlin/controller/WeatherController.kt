package weatherpikt.controller

import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient
import tk.plogitech.darksky.forecast.APIKey
import tk.plogitech.darksky.forecast.ForecastRequest
import tk.plogitech.darksky.forecast.ForecastRequestBuilder
import tk.plogitech.darksky.forecast.GeoCoordinates
import tk.plogitech.darksky.forecast.model.Forecast
import tk.plogitech.darksky.forecast.model.Latitude
import tk.plogitech.darksky.forecast.model.Longitude
import tornadofx.*
import weatherpikt.model.ForecastDataModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.fixedRateTimer

/**
 * The weather controller class that loads forecast information from Dark Sky
 *
 * TODO: switch to different forecast source
 */
class WeatherController : Controller() {

    /**
     * JavaFX property for
     */
    var curDateTime:String by property("")
    fun curDateTimeProperty() = getProperty(WeatherController::curDateTime)

    /**
     * Dark Sky forecast request object
     */
    var request: ForecastRequest? = null

    /**
     * Instance of the current conditions stored in the forecast model
     */
    val currently = ForecastDataModel(-1)

    /**
     * List of forecasts returned from forecast request
     */
    val forecastModels = mutableListOf<ForecastDataModel>()

    /**
     * Timer that updates the current date time property twice every second
     */
    private val updateCurDateTimer = fixedRateTimer(name = "CurrentDateTimer", initialDelay = 100, period = 500, daemon = true) {
        runAsync {
            formatCurrentDateTime()
        } ui { curDateTimeString ->
            curDateTime = curDateTimeString
        }
    }

    /**
     * Timer that updates the forecast every 5 minutes
     */
    private val updateForecastTimer = fixedRateTimer(name = "UpdateForecastTimer", initialDelay = 100, period = 5 * 60 * 1000, daemon = true) {
        runAsync {
            fetchForecast()
        } ui { forecast ->
            updateForecastModels(forecast)
        }
    }

    /**
     * Formats the current date time to a pattern in the form
     *      Three letter day, three letter month, day #, hour:minute:second am/pm
     *      Sun, Apr 19 2:23:38 PM
     */
    fun formatCurrentDateTime(): String {
        val dt = LocalDateTime.now()
        // date time in form Sun, Jun 30 10:55:29 PM
        val formatter = DateTimeFormatter.ofPattern("EEE, LLL d  h:mm:ss a");
        return dt.format(formatter);
    }

    /**
     * Fetches the forecast
     */
    private fun fetchForecast(): Forecast? {
        return try {
            val apiKey = app.config.string("apiKey", "")
            val lon = app.config.double("longitude", 0.0)
            val lat = app.config.double("latitude", 0.0)
            val units = app.config.string("units", "us")
            val lang = app.config.string("lang", "en")
            request = ForecastRequestBuilder()
                    .key(APIKey(apiKey))
                    .location(GeoCoordinates(Longitude(lon), Latitude(lat)))
                    .units(ForecastRequestBuilder.Units.valueOf(units))
                    .language(ForecastRequestBuilder.Language.valueOf(lang))
                    .exclude(ForecastRequestBuilder.Block.minutely)
                    .extendHourly()
                    .build()
            val client = DarkSkyJacksonClient()
            client.forecast(request)
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    /**
     * Takes the specified forecast result and updates the forecast models used
     * by the GUI.
     */
    private fun updateForecastModels(forecastResult: Forecast?) {
        // If forecast result is null, do nothing
        // TODO: may want to clear forecast after X number of failures
        forecastResult ?: return

        // Update each forecast model with forecast result
        for (fm in forecastModels) {
            if (forecastResult.daily.data.size > fm.index) {
                val daily = forecastResult.daily.data[fm.index]
                fm.timeZone = forecastResult.timezone
                fm.dateTime = daily.time
                fm.icon = daily.icon
                fm.temperatureLow = daily.temperatureLow
                fm.temperatureHigh = daily.temperatureHigh
                fm.precipitation = daily.precipProbability
            }
        }

        // Update the current weather conditions model
        currently.dateTime = forecastResult.currently.time
        currently.icon = forecastResult.currently.icon
        currently.summary = forecastResult.currently.summary
        // Use high temperature for current, and low temperature for apparent
        // TODO: need a different class to hold current weather conditions
        currently.temperatureHigh = forecastResult.currently.temperature
        currently.temperatureLow = forecastResult.currently.apparentTemperature
        currently.humidity = forecastResult.currently.humidity
        currently.pressure = forecastResult.currently.pressure
    }

    /**
     * Creates a default forecast model at the given index
     */
    fun createForecastModel(index: Int): ForecastDataModel {
        forecastModels.add(ForecastDataModel(index))
        return forecastModels.last()
    }
}