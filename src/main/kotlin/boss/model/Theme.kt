package boss.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class Theme {
    val nameProperty = SimpleStringProperty()
    var name by nameProperty

    val pathProperty = SimpleStringProperty()
    var path by pathProperty

    val thumbnailProperty = SimpleStringProperty()
    var thumbnail by thumbnailProperty

    val repoProperty = SimpleStringProperty()
    var repo by repoProperty

    val homepageProperty = SimpleStringProperty()
    var homepage by homepageProperty

    val descriptionProperty = SimpleStringProperty()
    var description by descriptionProperty

    override fun toString() = name ?: path

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Theme

        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        return pathProperty.hashCode()
    }


}