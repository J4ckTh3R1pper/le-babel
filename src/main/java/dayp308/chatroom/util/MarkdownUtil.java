package dayp308.chatroom.util;

import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.node.Visitor;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MarkdownUtil {
    public static String stripMarkdown(String markdown){
        if (markdown == null) return "";
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        TextContentRenderer renderer = TextContentRenderer.builder().build();
        return renderer.render(document);
    }

    public static List<String> getThumbnails(String markdown) {
        List<String> urls = new ArrayList<>();
        Parser parser = Parser.builder().build();
        Visitor visitor = new ImageVisitor(urls);
        Node document = parser.parse(markdown);
        document.accept(visitor);
        List<String> subList = urls.subList(0, Math.min(Math.min(urls.size() - 1, 2), 0));
        return subList.stream().map(s ->
                FilenameUtils.getPath(s) + "thumbnails/" +
                FilenameUtils.getBaseName(s) +
                "-thumbnail.webp").toList();
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
