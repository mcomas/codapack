package test;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

public class URLEditorPane {

    public static String HTML="<html>\n" +
            "<body>\n" +
            "Click on the link in the editale JEditorPane <br>\n" +
            "<a href=\"http://java.sun.com\">\nlink</a>" +
            "</body>\n" +
            "</html>";

    boolean isNeedCursorChange=true;
    JTextPane edit=new JTextPane() {
        public void setCursor(Cursor cursor) {
            if (isNeedCursorChange) {
                super.setCursor(cursor);
            }
        }
    };

    public URLEditorPane() {
        JFrame frame=new JFrame("Click on Links in editable JEditorPane");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(edit);
        MyHTMLEditorKit kit=new MyHTMLEditorKit();
//        HTMLEditorKit kit=new HTMLEditorKit();

        edit.setEditorKit(kit);
//        edit.setEditable(false);

        edit.setText(HTML);
        edit.addHyperlinkListener(new HTMLListener());
        frame.setSize(500,300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new URLEditorPane();
    }

    private class HTMLListener implements HyperlinkListener {
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                edit.setPage(e.getURL());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
      }
    }

    public class MyHTMLEditorKit extends HTMLEditorKit {

        MyLinkController handler=new MyLinkController();
        public void install(JEditorPane c) {
            MouseListener[] oldMouseListeners=c.getMouseListeners();
            MouseMotionListener[] oldMouseMotionListeners=c.getMouseMotionListeners();
            super.install(c);
            //the following code removes link handler added by original
            //HTMLEditorKit

            for (MouseListener l: c.getMouseListeners()) {
                c.removeMouseListener(l);
            }
            for (MouseListener l: oldMouseListeners) {
                c.addMouseListener(l);
            }

            for (MouseMotionListener l: c.getMouseMotionListeners()) {
                c.removeMouseMotionListener(l);
            }
            for (MouseMotionListener l: oldMouseMotionListeners) {
                c.addMouseMotionListener(l);
            }

            //add out link handler instead of removed one
            c.addMouseListener(handler);
            c.addMouseMotionListener(handler);
        }

        public class MyLinkController extends LinkController {

            public void mouseClicked(MouseEvent e) {
                JEditorPane editor = (JEditorPane) e.getSource();

                if (editor.isEditable() && SwingUtilities.isLeftMouseButton(e)) {
                    if (e.getClickCount()==2) {
                        editor.setEditable(false);
                        super.mouseClicked(e);
                        editor.setEditable(true);
                    }
                }

            }
            public void mouseMoved(MouseEvent e) {
                JEditorPane editor = (JEditorPane) e.getSource();

                if (editor.isEditable()) {
                    isNeedCursorChange=false;
                    editor.setEditable(false);
                    isNeedCursorChange=true;
                    super.mouseMoved(e);
                    isNeedCursorChange=false;
                    editor.setEditable(true);
                    isNeedCursorChange=true;
                }
            }

        }
    }
}
