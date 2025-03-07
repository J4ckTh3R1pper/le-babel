package dayp308.chatroom.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

public class MarkdownUtil {
    public static String stripMarkdown(String markdown){
        if (markdown == null) return "";
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        TextContentRenderer renderer = TextContentRenderer.builder().build();
        return renderer.render(document);
    }
}
