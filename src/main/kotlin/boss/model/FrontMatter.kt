package boss.model

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue
import java.io.OutputStream
import java.time.ZonedDateTime

class FrontMatter() {
    val rawProperty = SimpleObjectProperty<Toml>(Toml())
    var raw by rawProperty

    val dateProperty = SimpleObjectProperty<ZonedDateTime>()
    var date by dateProperty

    val titleProperty = SimpleStringProperty()
    var title by titleProperty

    val draftProperty = SimpleBooleanProperty()
    var draft by draftProperty

    constructor(source: String): this() {
        load(source)
    }

    fun load(source: String) {
        raw = raw.read(source)
        extractNamedProperties()
    }

    fun store(target: OutputStream) {
        target.use {
            TomlWriter().write(raw, it)
        }
    }

    private fun extractNamedProperties() {
        date = raw.getString("date")?.let { ZonedDateTime.parse(it) }
        title = raw.getString("title")
        draft = raw.getBoolean("draft", false)
    }

}