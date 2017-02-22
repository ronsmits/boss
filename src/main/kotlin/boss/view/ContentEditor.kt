package boss.view

import boss.util.markdown.MarkdownSyntaxHighlighter
import com.vladsch.flexmark.parser.Parser
import javafx.beans.property.SimpleStringProperty
import org.fxmisc.flowless.VirtualizedScrollPane
import org.fxmisc.richtext.CodeArea
import tornadofx.*


class ContentEditor : View("Boss - The Hugo Editor") {
    val content = SimpleStringProperty()

    override val root = stackpane {
        val codeArea = CodeArea(resources.stream("/test.md")!!.bufferedReader().readText())
        this += VirtualizedScrollPane(codeArea)
        codeArea.apply {
            setPrefSize(1024.0, 700.0)
            addClass("markdown-editor")
            stylesheets.add("/css/MarkdownEditor.css")
            stylesheets.add("/css/prism.css")
            isWrapText = true
            this@ContentEditor.content.bind(textProperty())
            this@ContentEditor.content.onChange {
                computeHighlighting(this, text)
            }
            computeHighlighting(this, text)
        }
    }

    fun computeHighlighting(codearea: CodeArea, text: String) {
        val astRoot = Parser.builder().build().parse(text)
        /*
        val hl = SyntaxHighlighter(text)
        hl.visit(parsed)
        val spans = hl.getSpans()
        */
        MarkdownSyntaxHighlighter.highlight(codearea, astRoot, mutableListOf())

    }

/*
class SyntaxHighlighter(val text: String) : NodeVisitor(arrayOf()) {
    var spans = HashMap<Int, Pair<List<String>, Int>>()

    private fun span(node: Node, vararg classes: String) {
        spans[node.startOffset] = classes.toList() to (node.endOffset - node.startOffset)
    }

    override fun visit(node: Node) {
        if (node !is Document) {
            println("$node (${node.chars.normalizeEOL()})")
            when (node) {
                is Heading -> span(node, "h${node.level}")
                is Code -> span(node, "code")
                is FencedCodeBlock -> span(node, "code")
                is Paragraph -> span(node, "p")
                is Text -> {}
                else -> span(node)
            }
        }
        super.visit(node)
    }

    fun getSpans(): StyleSpans<Collection<String>> {
        val spansBuilder = StyleSpansBuilder<Collection<String>>()
        val sortedSpans = spans.toSortedMap()
        val lastSpanStart = sortedSpans.lastKey()
        val lastSpanLength = spans[lastSpanStart]!!.second

        sortedSpans.forEach { entry ->
            val (classes, length) = entry.value
            spansBuilder.add(classes, length)
        }

        //spansBuilder.add(emptyList(), text.length - (lastSpanStart + lastSpanLength))
        return spansBuilder.create()
    }
}
*/
}
