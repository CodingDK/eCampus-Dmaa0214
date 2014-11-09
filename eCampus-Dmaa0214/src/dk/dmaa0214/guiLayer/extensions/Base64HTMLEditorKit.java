package dk.dmaa0214.guiLayer.extensions;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class Base64HTMLEditorKit extends HTMLEditorKit {

	private static final long serialVersionUID = 1L;
	private static HTMLFactory factory = null;

	public Base64HTMLEditorKit() {
		super();
		//StyleSheet styleSheet = getStyleSheet();
		StyleSheet styleSheet = new StyleSheet();
	    styleSheet.addRule("body {background-color: #f0f0f0; margin:4px; font-family:verdana, sans-serif;}");
	    styleSheet.addRule("a:link {color:#000099}");
	    styleSheet.addRule("a:visited {color:#000099}");
	    styleSheet.addRule("a:hover {color:#C0C0C0}");
	    styleSheet.addRule("a:active {color:#000099}");
	    setStyleSheet(styleSheet);
	    //System.out.println(styleSheet.toString());
	}
	
    @Override
    public ViewFactory getViewFactory() {
        if (factory == null) {
            factory = new HTMLFactory() {
            	
            	@Override
                public View create(Element elem) {
                    AttributeSet attrs = elem.getAttributes();
                    Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
                    Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
                    if (o instanceof HTML.Tag) {
                        HTML.Tag kind = (HTML.Tag) o;
                        if (kind == HTML.Tag.IMG) {
                            return new Base64ImageView(elem);
                        }
                    }
                    return super.create(elem);
                }
            };
        }
        return factory;
    }
    
}
