package boss.test

import boss.model.Content
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class ContentTests : BaseTest {

    @Test fun basicContentProperties() {
        val source = loadStream("/testsite/content/post/first.md")
        val content = Content(source)

        assertEquals("This is the content of our first post", content.text.trim())
        assertEquals("My First Post", content.frontMatter.title)
        assertEquals(true, content.frontMatter.draft)
        assertEquals(ZonedDateTime.of(2016, 12, 29, 19, 41, 5, 0, ZoneId.of("Europe/Oslo")).withFixedOffsetZone(), content.frontMatter.date)
    }

}