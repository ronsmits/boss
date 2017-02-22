package boss

import boss.model.Site
import boss.model.Theme
import javafx.scene.image.Image
import org.jsoup.Jsoup
import sun.misc.IOUtils
import sun.nio.ch.IOUtil
import tornadofx.Controller
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.util.*
import java.util.regex.Pattern
import java.util.zip.ZipInputStream

class ThemeController : Controller() {
    companion object {
        val UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
        val ThemeBase = "http://themes.gohugo.io/"
        val ThemeModulesUrl = "https://raw.githubusercontent.com/spf13/hugoThemes/master/.gitmodules"
        val ModulePattern = Pattern.compile("\\[submodule \".*\"\\]\\s*path = (.*?)\\s*url = (.*?)\\s", Pattern.MULTILINE)
    }

    fun listThemes(): List<Theme> {
        val submodules = URI.create(ThemeModulesUrl).toURL().openStream().bufferedReader().use { it.readText() }
        val matcher = ModulePattern.matcher(submodules)
        val themesByPath = HashMap<String, Theme>()
        while (matcher.find()) {
            val theme = Theme().apply {
                path = matcher.group(1)
                repo = matcher.group(2).replace(Regex("\\.git$"), "")
            }
            themesByPath[theme.path] = theme
        }

        val doc = Jsoup.connect(ThemeBase).header("User-Agent", UserAgent).get()

        doc.select(".thumbnail").forEach {
            val link = it.select("a")
            val homepageUrl = link.attr("href")
            val path = homepageUrl.substringAfter(ThemeBase).substringBefore("/")
            val img = link.select("img")
            val metaLink = it.select(".thumbnail-meta a")

            themesByPath[path]?.apply {
                description = img.attr("alt")
                thumbnail = img.attr("src")
                name = metaLink.text()
                homepage = homepageUrl
            }
        }

        return themesByPath.values.toList()
    }

    fun install(theme: Theme, site: Site, makeDefault: Boolean = true) {
        val archive = URI.create("${theme.repo}/archive/master.zip")
        val target = site.path.resolve(site.themesDir).resolve(theme.path)
        archive.toURL().openStream().use {
            val zip = ZipInputStream(it)
            var entry = zip.nextEntry
            val masterFolder = entry.name
            entry = zip.nextEntry
            while (entry != null) {
                val newFile = target.resolve(entry.name.substringAfter(masterFolder))
                if (entry.isDirectory) {
                    Files.createDirectories(newFile)
                } else {
                    Files.createDirectories(newFile.parent)
                    Files.copy(zip, newFile, REPLACE_EXISTING)
                }
                entry = zip.nextEntry
            }
        }
        if (makeDefault) {
            site.theme = theme.path
            site.save()
        }
    }

    fun getThumbnail(theme: Theme): Image {
        if (theme.thumbnail != null) {
            return URI.create(theme.thumbnail).toURL().openConnection().let {
                it.addRequestProperty("User-Agent", UserAgent)
                it.connect()
                Image(it.inputStream)
            }
        } else {
            return Image(resources.stream("/images/no-thumbnail.png"))
        }
    }
}

fun main(args: Array<String>) {
    val ctrl = ThemeController()

    val site = Site(Paths.get("/Users/es/Projects/boss/src/test/resources/testsite"))
    site.load()

    val theme = ThemeController().listThemes().find { it.path == "vienna" }!!
    ctrl.install(theme, site, makeDefault = false)
}

