package weatherpikt.view

import javafx.geometry.Pos
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import tornadofx.*
import weatherpikt.Style
import weatherpikt.controller.WeatherController

/**
 * The main application view which does the following:
 *   - Adds a date time label to the top
 *   - Adds a current conditions view in the middle
 *   - Adds forecast views along the bottom
 *   - Handles drag events to move the window since we use an undecorated window
 */
class MainView : View() {

    /**
     * Inject the weather controller instance
     */
    private val controller: WeatherController by inject()

    /**
     * Construct the GUI
     */
    override val root = vbox {
        hbox {
            alignment = Pos.CENTER
            label(controller.curDateTimeProperty()).addClass(Style.curDateTimeCSS)
        }
        with(CurrentConditionsView(controller.currently)) {
            root.vgrow = Priority.ALWAYS
            this@vbox.add(this)
        }
        gridpane {
            row {
                for (fv in 0..3) {
                    with(ForecastView(controller.createForecastModel(fv))) {
                        root.gridpaneConstraints { hGrow = Priority.ALWAYS }
                        this@row.add(this)
                    }
                }
            }
        }

        // Add event filters to handle dragging the window
        addEventFilter(MouseEvent.MOUSE_PRESSED, ::startDrag)
        addEventFilter(MouseEvent.MOUSE_DRAGGED, ::doDrag)

        // Add event handler for closing application with escape and entering full screen with f
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED) {
            if( it.code == KeyCode.ESCAPE) close()
            else if (it.code == KeyCode.F) primaryStage.isFullScreen = true
        }

        // Enter full screen on start if set in config and use w key to exit
        primaryStage.isFullScreen = app.config.boolean("fullscreen", false)
        primaryStage.fullScreenExitKeyCombination = KeyCodeCombination(KeyCode.W)
    }

    /**
     * Variables to hold the starting x and y when dragging the window
     */
    private var xOffset = 0.0
    private var yOffset = 0.0

    /**
     * Save starting x and y when mouse drag starts
     */
    private fun startDrag(evt: MouseEvent) {
        xOffset = evt.sceneX
        yOffset = evt.sceneY
    }

    /**
     * Move window as mouse is dragged
     */
    private fun doDrag(evt: MouseEvent) {
        primaryStage.x = evt.screenX - xOffset
        primaryStage.y = evt.screenY - yOffset
    }
}

