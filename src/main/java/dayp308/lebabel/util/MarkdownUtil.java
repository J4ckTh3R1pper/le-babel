package dayp308.lebabel.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.HtmlInline;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.node.Visitor;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

public class MarkdownUtil {
    public static String stripMarkdown(String markdown){
        if (markdown == null) return "";
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        removeImageNodes(document);
        TextContentRenderer renderer = TextContentRenderer.builder().build();
        return renderer.render(document);
    }

    private static void removeImageNodes(Node node) {
        Node next;
        for (Node child = node.getFirstChild(); child != null; child = next) {
            next = child.getNext();
            
            if (child instanceof Image) {
                child.unlink();
            } else if (child instanceof HtmlInline) {
                HtmlInline html = (HtmlInline) child;
                if (html.getLiteral().toLowerCase().startsWith("<p><img")) {
                    child.unlink();
                }
            } else {
                removeImageNodes(child);
            }
    }
}

    public static List<String> getThumbnails(String markdown, int count) {
        List<String> urls = new ArrayList<>();
        Parser parser = Parser.builder().build();
        Visitor visitor = new ImageVisitor(urls);
        Node document = parser.parse(markdown);
        document.accept(visitor);
        List<String> subList = urls.isEmpty() ? urls : urls.subList(0, Math.min(urls.size(), count));
        return subList.stream().map(s ->
                "/" + FilenameUtils.getPath(s) + "thumbnails/" +
                FilenameUtils.getBaseName(s) +
                ".jpeg").toList();
    }

    public static class ImageVisitor extends AbstractVisitor {
        protected final List<String> urls;

        public ImageVisitor(List<String> urls) {
            this.urls = urls;
        }

        @Override
        public void visit(Image image) {
            // 提取图片的 URL
            String url = image.getDestination();
            urls.add(url);
            super.visit(image);
        }
    }

}
