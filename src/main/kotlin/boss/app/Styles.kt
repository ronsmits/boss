package boss.app

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val themeBrowser by cssclass()
        val themeName by cssclass()
        val thumbnail by cssclass()
    }

    init {
        themeBrowser {
            backgroundColor += Color.WHITE
            cellWidth = 320.px
            cellHeight = 215.px
            prefWidth = 1050.px
            themeName {
                prefWidth = this@themeBrowser.cellWidth
                textFill = c("#ff4088")
                fontWeight = FontWeight.BOLD
                fontSize = 22.px
                backgroundColor += c("#fff", 0.8)
            }
            thumbnail and hover {
                themeName {
                    textFill = Color.WHITE
                    backgroundColor += c("#ff4088")
                }
            }
            datagridCell {
                backgroundColor += Color.WHITE
            }
        }
    }
}