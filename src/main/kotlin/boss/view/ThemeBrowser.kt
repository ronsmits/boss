package boss.view

import boss.ThemeController
import boss.app.Styles
import boss.model.SiteModel
import boss.model.Theme
import javafx.geometry.Pos.BOTTOM_LEFT
import javafx.scene.control.Alert.AlertType.CONFIRMATION
import javafx.scene.control.ButtonType
import javafx.scene.control.ButtonType.NO
import tornadofx.*

class ThemeBrowser : View("Theme Browser") {
    val themeController: ThemeController by inject()
    val site: SiteModel by inject()

    override val root = datagrid<Theme> {
        addClass(Styles.themeBrowser)
        asyncItems { themeController.listThemes() }
        onUserSelect(clickCount = 1) { confirmInstall(it) }
        cellCache { theme ->
            stackpane {
                addClass(Styles.thumbnail)
                imageview {
                    runAsyncWithProgress {
                        themeController.getThumbnail(theme)
                    } ui { image = it }
                    isPreserveRatio = true
                    fitHeight = cellHeight
                    fitWidth = cellWidth
                }
                label(theme.toString()) {
                    addClass(Styles.themeName)
                    paddingAll = 5.0
                    prefWidth = cellWidth
                    stackpaneConstraints { alignment = BOTTOM_LEFT }
                    parent.onHover {
                        paddingVerticalProperty.animate(if (it) 20.0 else 10.0, 200.millis)
                    }
                }
            }
        }
    }

    private fun confirmInstall(theme: Theme) {
        val yesAndMakeDefault = ButtonType("Set as default")
        val installOnly = ButtonType("Install only")

        alert(CONFIRMATION, "Confirm theme install", "Confirm installation of the ${theme.name} theme", installOnly, yesAndMakeDefault, NO) { button ->
            if (button != NO) {
                themeController.install(theme, site.item, yesAndMakeDefault == button)
            }
        }
    }

}