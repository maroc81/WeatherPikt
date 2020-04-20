package weatherpikt

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*
import weatherpikt.view.MainView

/**
 * Class for the tornadofx app to show weather and forecast from internet source
 */
class WeatherPiktApp : App() {
    /**
     * The primary view is built using the MainView class
     */
    override val primaryView = MainView::class

    /**
     * Initialize App
     */
    init {
        // Load the scale from the config
        Style.scale = config.double("scale", 1.0)
        // Initialize and import the style
        importStylesheet(Style::class)
    }

    /**
     * Override the start function so we can set the main window to be
     * "undecorated" with no title bar or border
     */
    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UNDECORATED)
        super.start(stage)
    }

    /**
     * Override create primary scene function so we can set background
     *
     * TODO: remove this
     */
    override fun createPrimaryScene(view: UIComponent): Scene {
        val scene = Scene(view.root)
        //scene.fill = javafx.scene.paint.Color.TRANSPARENT
        return scene
    }
}

/**
 * Launch the main application
 */
fun main(args: Array<String>) {
    Application.launch(WeatherPiktApp::class.java, *args)
}

/**
 * Define the styles used by the views
 */
class Style : Stylesheet() {

    /**
     * Set styles for the application and initialize the static CSS classes
     */
    init {
        root {
            fill = Color.BLACK
            backgroundColor += Color.BLACK

            prefWidth = 480.px * scale
            prefHeight = 320.px * scale
        }
        text {
            fill = Color.WHITE
        }

        curDateTimeCSS {
            fontSize = 28.px * scale
        }
        curTempCSS {
            fontSize = 24.px * scale
        }
        curSummaryCSS {
            fontSize = 16.px * scale
        }
        curImageCSS {
            maxWidth = 125.px
            maxHeight = 125.px
        }

        forecastDateTimeCSS {
            fontSize = 16.px * scale
        }
        forecastValuesCSS {
            fontSize = 14.px * scale
        }
    }

    /**
     * Static CSS classes used in the views
     */
    companion object {
        // Current conditions CSS
        val curDateTimeCSS by cssclass()
        val curTempCSS by cssclass()
        val curSummaryCSS by cssclass()
        val curImageCSS by cssclass()

        // Forecast conditions CSS
        val forecastDateTimeCSS by cssclass()
        val forecastValuesCSS by cssclass()

        // Change the scale to resize the window
        var scale = 1.0
    }
}
