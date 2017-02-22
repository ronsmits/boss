package boss.app

import boss.model.Site
import boss.model.SiteModel
import boss.view.ThemeBrowser
import tornadofx.App
import tornadofx.setInScope
import java.nio.file.Paths

class BossApp : App(ThemeBrowser::class, Styles::class) {
    init {
        val model = SiteModel().apply { item = Site(Paths.get("/Users/es/Projects/boss/src/test/resources/testsite")) }
        setInScope(model)
    }
}