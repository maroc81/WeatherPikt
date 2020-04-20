package weatherpikt.view

import javafx.geometry.Pos
import javafx.scene.image.ImageView
import tornadofx.*
import weatherpikt.Style
import weatherpikt.model.ForecastDataModel
import kotlin.text.Typography.degree

/**
 * Class to show a forecast
 */
class ForecastView(private val model: ForecastDataModel) : Fragment() {

    /**
     * String binding on the low and high temperature properties of the forecast data model
     * that will update when either changes and return a string in the form:
     *    low / high
     */
    private val lowHigh = stringBinding(model.temperatureLowProperty(), model.temperatureHighProperty()) {
        "${model.temperatureLow.toInt()}$degree / ${model.temperatureHigh.toInt()}$degree"
    }

    /**
     * Reference to the forecast image assigned when the GUI is constructed
     */
    private var imgForecast: ImageView by singleAssign()

    /**
     * Construct the GUI
     */
    override val root = vbox {
        alignment = Pos.CENTER
        label(model.dateTimeAsString).addClass(Style.forecastDateTimeCSS)
        imgForecast = imageview(model.iconImage) {
            fitWidth = 100.0
            isPreserveRatio = true
        }
        label(lowHigh).addClass(Style.curSummaryCSS)
        label(model.precipitationPercent).addClass(Style.forecastValuesCSS)
    }

    /**
     * Resize the image on init based on the scale defined in the application style class.
     *
     * There is no style way to change the image using a CSS style so it must be done here.
     */
    init {
        imgForecast.fitWidth = imgForecast.fitWidth * Style.scale
    }
}