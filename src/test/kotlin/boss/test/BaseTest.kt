package boss.test

interface BaseTest {
    fun loadStream(path: String) = SiteTests::class.java.getResourceAsStream(path)
}
