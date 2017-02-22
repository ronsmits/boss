package boss.test

import boss.model.Site
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.nio.file.Paths

class SiteTests : BaseTest {

    @Test fun basicSiteProperties() {
        val site = Site(Paths.get("."))
        site.load(loadStream("/testsite/config.toml"))

        assertEquals("My New Hugo Site", site.title)
        assertEquals("en-us", site.languageCode)
        assertEquals("http://example.org/", site.baseURL)
        assertEquals(2, site.taxonomies.size)
        assertEquals("tags", site.taxonomies["tag"])
        assertEquals("Nikola Tesla", site.params["author"])
        site.params["author"] = "Edvin Syse"

        val output = ByteArrayOutputStream()
        site.save(output)
        val saved = output.toString("UTF-8")
        println(saved)
    }

}
