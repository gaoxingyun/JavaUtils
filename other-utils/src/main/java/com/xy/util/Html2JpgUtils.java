package com.xy.util;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;

/**
 * html转jpg工具类
 *
 * @author xy
 *
 */
public class Html2JpgUtils {

    public static void html2Jpg(String htmlUrl, String fileName) {
        try {
            JEditorPane editorPane = new JEditorPane();
            editorPane.setEditable(false);
            SynchronousHTMLEditorKit kit = new SynchronousHTMLEditorKit();
            editorPane.setEditorKitForContentType("text/html", kit);
            editorPane.setContentType("text/html");
            editorPane.setPage(htmlUrl);

            BufferedImage img = new BufferedImage(editorPane.getPreferredSize().width,
                    editorPane.getPreferredSize().height, BufferedImage.TYPE_INT_RGB);

            Graphics graphics = img.getGraphics();
            editorPane.setSize(editorPane.getPreferredSize());
            editorPane.print(graphics);
            File file = new File(fileName);
            ImageIO.write(img, "jpg", file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

class SynchronousHTMLEditorKit extends HTMLEditorKit {

    private static final long serialVersionUID = 1L;

     SynchronousHTMLEditorKit() {
        super();
    }

    public Document createDefaultDocument() {
        HTMLDocument doc = (HTMLDocument) super.createDefaultDocument();
        doc.setAsynchronousLoadPriority(-1);
        return doc;
    }

    public ViewFactory getViewFactory() {
        return new HTMLFactory() {
            public View create(Element elem) {
                View view = super.create(elem);
                if (view instanceof ImageView) {
                    ((ImageView) view).setLoadsSynchronously(true);
                }
                return view;
            }
        };
    }
}
