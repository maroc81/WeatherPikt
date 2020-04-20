package weatherpikt.view

import javafx.geometry.Pos
import javafx.scene.image.ImageView
import javafx.scene.text.TextAlignment
import tornadofx.*
import weatherpikt.Style
import weatherpikt.model.ForecastDataModel

/**
 * Class to show the current conditions
 */
class CurrentConditionsView(val model: ForecastDataModel) : Fragment() {

    /**
     * Reference to the current conditions image assigned when the GUI is constructed
     */
    private var imgCurCondition: ImageView by singleAssign()

    /**
     * Construct the GUI
     */
    override val root = hbox {
        alignment = Pos.CENTER
        vbox {
            alignment = Pos.CENTER
            label(model.temperatureHighDegrees).addClass(Style.curTempCSS)
            label(model.summaryProperty()) {
                isWrapText = true
                textAlignment = TextAlignment.CENTER
            }.addClass(Style.curSummaryCSS)
        }
        imgCurCondition = imageview(model.iconImage) {
            fitHeight = 125.0
            isPreserveRatio = true
        }
        gridpane {
            hgap = 20.0
            vgap = 10.0
            alignment = Pos.CENTER
            row {
                label("Feels Like:").addClass(Style.curSummaryCSS)
                label(model.temperatureLowDegrees).addClass(Style.curSummaryCSS)
            }
            row {
                label("Humidity: ").addClass(Style.curSummaryCSS)
                label(model.humidityPercent).addClass(Style.curSummaryCSS)
            }
            row {
                label("Pressure: ").addClass(Style.curSummaryCSS)
                label(model.pressureProperty()).addClass(Style.curSummaryCSS)
            }
        }
    }

    /**
     * Resize the image on init based on the scale defined in the application style class.
     *
     * There is no style way to change the image using a CSS style so it must be done here.
     */
    init {
        imgCurCondition.fitHeight = imgCurCondition.fitHeight * Style.scale
    }
}