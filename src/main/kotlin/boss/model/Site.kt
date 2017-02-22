@file:Suppress("UNCHECKED_CAST")

package boss.model

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableMap
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path

class Site(val path: Path) {
    val rawProperty = SimpleObjectProperty<Toml>(Toml())
    var raw by rawProperty

    val languageCodeProperty = SimpleStringProperty()
    var languageCode by languageCodeProperty

    val titleProperty = SimpleStringProperty()
    var title by titleProperty

    val themeProperty = SimpleStringProperty()
    var theme by themeProperty

    val baseURLProperty = SimpleStringProperty()
    var baseURL by baseURLProperty

    val taxonomies = FXCollections.observableHashMap<String, String>()

    val params = FXCollections.observableHashMap<String, String>()

    val buildDraftsProperty = SimpleBooleanProperty(false)
    var buildDrafts by buildDraftsProperty

    val buildFutureProperty = SimpleBooleanProperty(false)
    var buildFuture by buildFutureProperty

    val buildExpiredProperty = SimpleBooleanProperty(false)
    var buildExpired by buildExpiredProperty

    val relativeURLsProperty = SimpleBooleanProperty(false)
    var relativeURLs by relativeURLsProperty

    val canonifyURLsProperty = SimpleBooleanProperty(false)
    var canonifyURLs by canonifyURLsProperty

    val defaultLayoutProperty = SimpleStringProperty("post")
    var defaultLayout by defaultLayoutProperty

    val defaultContentLanguageProperty = SimpleStringProperty("en")
    var defaultContentLanguage by defaultContentLanguageProperty

    val defaultContentLanguageInSubdirProperty = SimpleBooleanProperty(false)
    var defaultContentLanguageInSubdir by defaultContentLanguageInSubdirProperty

    val disableLiveReloadProperty = SimpleBooleanProperty(false)
    var disableLiveReload by disableLiveReloadProperty

    val disableRSSProperty = SimpleBooleanProperty(false)
    var disableRSS by disableRSSProperty

    val disableSitemapProperty = SimpleBooleanProperty(false)
    var disableSitemap by disableSitemapProperty

    val enableGitInfoProperty = SimpleBooleanProperty(false)
    var enableGitInfo by enableGitInfoProperty

    val enableRobotsTXTProperty = SimpleBooleanProperty(false)
    var enableRobotsTXT by enableRobotsTXTProperty

    val disable404Property = SimpleBooleanProperty(false)
    var disable404 by disable404Property

    val enableEmojiProperty = SimpleBooleanProperty(false)
    var enableEmoji by enableEmojiProperty

    val enableMissingTranslationPlaceholdersProperty = SimpleBooleanProperty(false)
    var enableMissingTranslationPlaceholders by enableMissingTranslationPlaceholdersProperty

    val googleAnalyticsProperty = SimpleStringProperty("")
    var googleAnalytics by googleAnalyticsProperty

    val noChmodProperty = SimpleBooleanProperty(false)
    var noChmod by noChmodProperty

    val themesDirProperty = SimpleStringProperty("themes")
    var themesDir by themesDirProperty

    val noTimesProperty = SimpleBooleanProperty(false)
    var noTimes by noTimesProperty

    val paginateProperty = SimpleIntegerProperty(10)
    var paginate by paginateProperty

    val pygmentsCodeFencesGuessSyntaxProperty = SimpleBooleanProperty(false)
    var pygmentsCodeFencesGuessSyntax by pygmentsCodeFencesGuessSyntaxProperty

    val pygmentsStyleProperty = SimpleStringProperty("monokai")
    var pygmentsStyle by pygmentsStyleProperty

    val pygmentsUseClassesProperty = SimpleBooleanProperty(false)
    var pygmentsUseClasses by pygmentsUseClassesProperty

    val disablePathToLowerProperty = SimpleBooleanProperty(false)
    var disablePathToLower by disablePathToLowerProperty

    val verboseProperty = SimpleBooleanProperty(false)
    var verbose by verboseProperty

    val verboseLogProperty = SimpleBooleanProperty(false)
    var verboseLog by verboseLogProperty

    val watchProperty = SimpleBooleanProperty(true)
    var watch by watchProperty

    val configProperty = SimpleStringProperty("config.toml")
    var config by configProperty

    fun load() {
        Files.newInputStream(path.resolve(config)).use { load(it) }
    }

    fun load(source: InputStream) {
        raw = raw.read(source)
        extractNamedProperties()
    }

    fun save() {
        Files.newOutputStream(path.resolve(config)).use { save(it) }
    }

    fun save(output: OutputStream) {
        output.use { TomlWriter().write(copyPropertiesToMap(), it) }
    }

    private fun copyPropertiesToMap(): MutableMap<String, Any>? {
        val map = raw.toMap()

        getPropertyFields().forEach { field ->
            val name = field.name.substringBefore("Property")
            val property = field.get(this)

            when (property) {
                is Property<*> -> map[name] = property.value
                is ObservableMap<*, *> -> map[name] = property
            }
        }

        return map
    }

    private fun extractNamedProperties() {
        getPropertyFields().forEach { field ->
            val name = field.name.substringBefore("Property")
            if (raw.contains(name)) {
                field.isAccessible = true
                val property = field.get(this)

                when (property) {
                    is StringProperty -> property.value = raw.getString(name)
                    is BooleanProperty -> property.value = raw.getBoolean(name)
                    is ObservableMap<*, *> -> (property as ObservableMap<String, Any>).putAll(raw.getTable(name).toMap())
                }

            }
        }
    }

    private fun getPropertyFields() = Site::class.java.declaredFields.filterNot { it.name.startsWith("raw") || it.name.contains("\$") }

}

class SiteModel : ItemViewModel<Site>() {
    val raw = bind { item?.rawProperty }
    val languageCode = bind { item?.languageCodeProperty }
    val title = bind { item?.titleProperty }
    val theme = bind { item?.themeProperty }
    val baseURL = bind { item?.baseURLProperty }
    val buildDrafts = bind { item?.buildDraftsProperty }
    val buildFuture = bind { item?.buildFutureProperty }
    val buildExpired = bind { item?.buildExpiredProperty }
    val relativeURLs = bind { item?.relativeURLsProperty }
    val canonifyURLs = bind { item?.canonifyURLsProperty }
    val defaultLayout = bind { item?.defaultLayoutProperty }
    val defaultContentLanguage = bind { item?.defaultContentLanguageProperty }
    val defaultContentLanguageInSubdir = bind { item?.defaultContentLanguageInSubdirProperty }
    val disableLiveReload = bind { item?.disableLiveReloadProperty }
    val disableRSS = bind { item?.disableRSSProperty }
    val disableSitemap = bind { item?.disableSitemapProperty }
    val enableGitInfo = bind { item?.enableGitInfoProperty }
    val enableRobotsTXT = bind { item?.enableRobotsTXTProperty }
    val disable404 = bind { item?.disable404Property }
    val enableEmoji = bind { item?.enableEmojiProperty }
    val enableMissingTranslationPlaceholders = bind { item?.enableMissingTranslationPlaceholdersProperty }
    val googleAnalytics = bind { item?.googleAnalyticsProperty }
    val noChmod = bind { item?.noChmodProperty }
    val themesDir = bind { item?.themesDirProperty }
    val noTimes = bind { item?.noTimesProperty }
    val paginate = bind { item?.paginateProperty }
    val pygmentsCodeFencesGuessSyntax = bind { item?.pygmentsCodeFencesGuessSyntaxProperty }
    val pygmentsStyle = bind { item?.pygmentsStyleProperty }
    val pygmentsUseClasses = bind { item?.pygmentsUseClassesProperty }
    val disablePathToLower = bind { item?.disablePathToLowerProperty }
    val verbose = bind { item?.verboseProperty }
    val verboseLog = bind { item?.verboseLogProperty }
    val watch = bind { item?.watchProperty }
}
