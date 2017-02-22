package boss.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue
import java.io.BufferedReader
import java.io.InputStream
import java.util.regex.Pattern

class Content() {
    val frontMatterProperty = SimpleObjectProperty<FrontMatter>()
    var frontMatter by frontMatterProperty

    val textProperty = SimpleStringProperty()
    var text by textProperty

    companion object {
        val FrontMatterContentPattern = Pattern.compile("\\+\\+\\+(.*?)\\+\\+\\+(.*)", Pattern.DOTALL)
    }

    constructor(source: InputStream) : this() {
        val all = source.bufferedReader().use(BufferedReader::readText)
        val matcher = FrontMatterContentPattern.matcher(all)
        if (matcher.find()) {
            frontMatter = FrontMatter(matcher.group(1))
            text = matcher.group(2)
        } else {
            throw IllegalArgumentException("Source did not contain front matter and content")
        }
    }
}