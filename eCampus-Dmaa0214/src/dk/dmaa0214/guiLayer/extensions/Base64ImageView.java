package dk.dmaa0214.guiLayer.extensions;
/**
 * Found at https://github.com/dscho/angiotool/blob/master/src/main/java/imagejan/angiotool/gui/MyImageView.java
 * Thanks to Enrique Zudaire for sharing the code.
 * 
 * Edited to load Base64 Images. 
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.event.*;
import javax.xml.bind.DatatypeConverter;
import javax.imageio.ImageIO;

public class Base64ImageView extends View implements ImageObserver{


	public static final String
	TOP = "top",
	TEXTTOP = "texttop",
	MIDDLE = "middle",
	ABSMIDDLE = "absmiddle",
	CENTER = "center",
	BOTTOM = "bottom";

	public Base64ImageView(Element elem) {
		super(elem);
		initialize(elem);
		StyleSheet sheet = getStyleSheet();
		attr = sheet.getViewAttributes(this);
	}

	private void initialize( Element elem ) {
		synchronized(this) {
			loading = true;
			fWidth = fHeight = 0;
		}
		int width = 0;
		int height = 0;
		boolean customWidth = false;
		boolean customHeight = false;
		try {
			fElement = elem;

			loadImage();

			height = getIntAttr(HTML.Attribute.HEIGHT,-1);
			customHeight = (height>0);
			if( !customHeight && fImage != null )
				height = fImage.getHeight(this);
			if( height <= 0 )
				height = DEFAULT_HEIGHT;

			width = getIntAttr(HTML.Attribute.WIDTH,-1);
			customWidth = (width>0);
			if( !customWidth && fImage != null )
				width = fImage.getWidth(this);
			if( width <= 0 )
				width = DEFAULT_WIDTH;

			if( fImage != null )
				if( customWidth && customHeight )
					Toolkit.getDefaultToolkit().prepareImage(fImage,height,
							width,this);
				else
					Toolkit.getDefaultToolkit().prepareImage(fImage,-1,-1,
							this);
		} finally {
			synchronized(this) {
				loading = false;
				if (customWidth || fWidth == 0) {
					fWidth = width;
				}
				if (customHeight || fHeight == 0) {
					fHeight = height;
				}
			}
		}
	}

	private void loadImage() {
		String src =
				(String) fElement.getAttributes().getAttribute
				(HTML.Attribute.SRC);
		String imageDataBytes = src.substring(src.indexOf(",")+1);
		byte[] imageByte;
		try
		{
			imageByte = DatatypeConverter.parseBase64Binary(imageDataBytes);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			fImage = ImageIO.read(bis);

			bis.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public AttributeSet getAttributes() {
		return attr;
	}

	boolean isLink( ) {
		AttributeSet anchorAttr = (AttributeSet)
				fElement.getAttributes().getAttribute(HTML.Tag.A);
		if (anchorAttr != null) {
			return anchorAttr.isDefined(HTML.Attribute.HREF);
		}
		return false;
	}

	int getBorder( ) {
		return getIntAttr(HTML.Attribute.BORDER, isLink() ?DEFAULT_BORDER :0);
	}

	int getSpace( int axis ) {
		return getIntAttr( axis==X_AXIS ?HTML.Attribute.HSPACE :HTML.Attribute.VSPACE,
				0 );
	}

	Color getBorderColor( ) {
		StyledDocument doc = (StyledDocument) getDocument();
		return doc.getForeground(getAttributes());
	}

	float getVerticalAlignment( ) {
		String align = (String) fElement.getAttributes().getAttribute(HTML.Attribute.ALIGN);
		if( align != null ) {
			align = align.toLowerCase();
			if( align.equals(TOP) || align.equals(TEXTTOP) )
				return 0.0f;
			else if( align.equals(Base64ImageView.CENTER) || align.equals(MIDDLE)
					|| align.equals(ABSMIDDLE) )
				return 0.5f;
		}
		return 1.0f;
	}

	boolean hasPixels( ImageObserver obs ) {
		return fImage != null && fImage.getHeight(obs)>0
				&& fImage.getWidth(obs)>0;
	}


	private int getIntAttr(HTML.Attribute name, int deflt ) {
		AttributeSet attr = fElement.getAttributes();
		if( attr.isDefined(name) ) {
			int i;
			String val = (String) attr.getAttribute(name);
			if( val == null )
				i = deflt;
			else
				try{
					i = Math.max(0, Integer.parseInt(val));
				}catch( NumberFormatException x ) {
					i = deflt;
				}
			return i;
		} else
			return deflt;
	}

	public void setParent(View parent) {
		super.setParent(parent);
		fContainer = parent!=null ?getContainer() :null;
		if( parent==null && fComponent!=null ) {
			fComponent.getParent().remove(fComponent);
			fComponent = null;
		}
	}

	public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f) {
		super.changedUpdate(e,a,f);
		float align = getVerticalAlignment();

		int height = fHeight;
		int width  = fWidth;

		initialize(getElement());

		boolean hChanged = fHeight!=height;
		boolean wChanged = fWidth!=width;
		if( hChanged || wChanged || getVerticalAlignment()!=align ) {
			getParent().preferenceChanged(this,hChanged,wChanged);
		}
	}

	public void paint(Graphics g, Shape a) {
		Color oldColor = g.getColor();
		fBounds = a.getBounds();
		int border = getBorder();
		int x = fBounds.x + border + getSpace(X_AXIS);
		int y = fBounds.y + border + getSpace(Y_AXIS);
		int width = fWidth;
		int height = fHeight;
		int sel = getSelectionState();

		if( ! hasPixels(this) ) {
			g.setColor(Color.lightGray);
			g.drawRect(x,y,width-1,height-1);
			g.setColor(oldColor);
			loadIcons();
			Icon icon = fImage==null ?sMissingImageIcon :sPendingImageIcon;
			if( icon != null )
				icon.paintIcon(getContainer(), g, x, y);
		}

		if( fImage != null ) {
			g.drawImage(fImage,x, y,width,height,this);
		}

		Color bc = getBorderColor();
		if( sel == 2 ) {
			int delta = 2-border;
			if( delta > 0 ) {
				x += delta;
				y += delta;
				width -= delta<<1;
				height -= delta<<1;
				border = 2;
			}
			bc = null;
			g.setColor(Color.black);
			g.fillRect(x+width-5,y+height-5,5,5);
		}

		if( border > 0 ) {
			if( bc != null ) g.setColor(bc);
			for( int i=1; i<=border; i++ )
				g.drawRect(x-i, y-i, width-1+i+i, height-1+i+i);
			g.setColor(oldColor);
		}
	}

	protected void repaint( long delay ) {
		if( fContainer != null && fBounds!=null ) {
			fContainer.repaint(delay,
					fBounds.x,fBounds.y,fBounds.width,fBounds.height);
		}
	}

	protected int getSelectionState( ) {
		int p0 = fElement.getStartOffset();
		int p1 = fElement.getEndOffset();
		if (fContainer instanceof JTextComponent) {
			JTextComponent textComp = (JTextComponent)fContainer;
			int start = textComp.getSelectionStart();
			int end = textComp.getSelectionEnd();
			if( start<=p0 && end>=p1 ) {
				if( start==p0 && end==p1 && isEditable() )
					return 2;
				else
					return 1;
			}
		}
		return 0;
	}

	protected boolean isEditable( ) {
		return fContainer instanceof JEditorPane
				&& ((JEditorPane)fContainer).isEditable();
	}

	/** Returns the text editor's highlight color. */
	protected Color getHighlightColor( ) {
		JTextComponent textComp = (JTextComponent)fContainer;
		return textComp.getSelectionColor();
	}

	public boolean imageUpdate( Image img, int flags, int x, int y,
			int width, int height ) {
		if( fImage==null || fImage != img )
			return false;

		if( (flags & (ABORT|ERROR)) != 0 ) {
			fImage = null;
			repaint(0);
			return false;
		}

		short changed = 0;
		if( (flags & ImageObserver.HEIGHT) != 0 )
			if( ! getElement().getAttributes().isDefined(HTML.Attribute.HEIGHT) ) {
				changed |= 1;
			}
		if( (flags & ImageObserver.WIDTH) != 0 )
			if( ! getElement().getAttributes().isDefined(HTML.Attribute.WIDTH) ) {
				changed |= 2;
			}
		synchronized(this) {
			if ((changed & 1) == 1) {
				fWidth = width;
			}
			if ((changed & 2) == 2) {
				fHeight = height;
			}
			if (loading) {
				return true;
			}
		}
		if( changed != 0 ) {
			if( DEBUG ) System.out.println("ImageView: resized to "+fWidth+"x"+fHeight);

			Document doc = getDocument();
			try {
				if (doc instanceof AbstractDocument) {
					((AbstractDocument)doc).readLock();
				}
				preferenceChanged(this,true,true);
			} finally {
				if (doc instanceof AbstractDocument) {
					((AbstractDocument)doc).readUnlock();
				}
			}			

			return true;
		}

		if( (flags & (FRAMEBITS|ALLBITS)) != 0 )
			repaint(0);
		else if( (flags & SOMEBITS) != 0 )
			if( sIsInc )
				repaint(sIncRate);

		return ((flags & ALLBITS) == 0);
	}

	private static boolean sIsInc = true;
	private static int sIncRate = 100;

	public float getPreferredSpan(int axis) {
		int extra = 2*(getBorder()+getSpace(axis));
		switch (axis) {
		case View.X_AXIS:
			return fWidth+extra;
		case View.Y_AXIS:
			return fHeight+extra;
		default:
			throw new IllegalArgumentException("Invalid axis: " + axis);
		}
	}

	public float getAlignment(int axis) {
		switch (axis) {
		case View.Y_AXIS:
			return getVerticalAlignment();
		default:
			return super.getAlignment(axis);
		}
	}

	public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
		int p0 = getStartOffset();
		int p1 = getEndOffset();
		if ((pos >= p0) && (pos <= p1)) {
			Rectangle r = a.getBounds();
			if (pos == p1) {
				r.x += r.width;
			}
			r.width = 0;
			return r;
		}
		return null;
	}

	public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
		Rectangle alloc = (Rectangle) a;
		if (x < alloc.x + alloc.width) {
			bias[0] = Position.Bias.Forward;
			return getStartOffset();
		}
		bias[0] = Position.Bias.Backward;
		return getEndOffset();
	}

	public void setSize(float width, float height) {
	}

	protected void resize( int width, int height ) {
		if( width==fWidth && height==fHeight )
			return;

		fWidth = width;
		fHeight= height;

		MutableAttributeSet attr = new SimpleAttributeSet();
		attr.addAttribute(HTML.Attribute.WIDTH ,Integer.toString(width));
		attr.addAttribute(HTML.Attribute.HEIGHT,Integer.toString(height));
		((StyledDocument)getDocument()).setCharacterAttributes(
				fElement.getStartOffset(),
				fElement.getEndOffset(),
				attr, false);
	}

	public void mousePressed(MouseEvent e){
		Dimension size = fComponent.getSize();
		if( e.getX() >= size.width-7 && e.getY() >= size.height-7
				&& getSelectionState()==2 ) {
			if(DEBUG)System.out.println("ImageView: grow!!! Size="+fWidth+"x"+fHeight);
			Point loc = fComponent.getLocationOnScreen();
			fGrowBase = new Point(loc.x+e.getX() - fWidth,
					loc.y+e.getY() - fHeight);
		} else {
			fGrowBase = null;
			JTextComponent comp = (JTextComponent)fContainer;
			int start = fElement.getStartOffset();
			int end = fElement.getEndOffset();
			int mark = comp.getCaret().getMark();
			int dot  = comp.getCaret().getDot();
			if( e.isShiftDown() ) {
				if( mark <= start )
					comp.moveCaretPosition(end);
				else
					comp.moveCaretPosition(start);
			} else {
				if( mark!=start )
					comp.setCaretPosition(start);
				if( dot!=end )
					comp.moveCaretPosition(end);
			}
		}
	}

	public void mouseDragged(MouseEvent e ) {
		if( fGrowBase != null ) {
			Point loc = fComponent.getLocationOnScreen();
			int width = Math.max(2, loc.x+e.getX() - fGrowBase.x);
			int height= Math.max(2, loc.y+e.getY() - fGrowBase.y);

			if( e.isShiftDown() && fImage!=null ) {
				float imgWidth = fImage.getWidth(this);
				float imgHeight = fImage.getHeight(this);
				if( imgWidth>0 && imgHeight>0 ) {
					float prop = imgHeight / imgWidth;
					float pwidth = height / prop;
					float pheight= width * prop;
					if( pwidth > width )
						width = (int) pwidth;
					else
						height = (int) pheight;
				}
			}

			resize(width,height);
		}
	}

	public void mouseReleased(MouseEvent e){
		fGrowBase = null;
	}

	public void mouseClicked(MouseEvent e){
		if( e.getClickCount() == 2 ) {
		}
	}

	public void mouseEntered(MouseEvent e){
	}
	public void mouseMoved(MouseEvent e ) {
	}
	public void mouseExited(MouseEvent e){
	}

	private void loadIcons( ) {
		try{
			if( sPendingImageIcon == null )
				sPendingImageIcon = getLoadingImageIcon();
			if( sMissingImageIcon == null )
				sMissingImageIcon = getNoImageIcon();
		}catch( Exception x ) {
			System.err.println("ImageView: Couldn't load image icons");
		}
	}

	protected StyleSheet getStyleSheet() {
		HTMLDocument doc = (HTMLDocument) getDocument();
		return doc.getStyleSheet();
	}

	/**
	 * Returns the icon to use if the image couldn't be found.
	 */
	public Icon getNoImageIcon() {
		return (Icon) UIManager.getLookAndFeelDefaults().get("html.missingImage");
	}

	/**
	 * Returns the icon to use while in the process of loading the image.
	 */
	public Icon getLoadingImageIcon() {
		return (Icon) UIManager.getLookAndFeelDefaults().get("html.pendingImage");
	}

	private AttributeSet attr;
	private Element   fElement;
	private Image     fImage;
	private int       fHeight,fWidth;
	private Container fContainer;
	private Rectangle fBounds;
	private Component fComponent;
	private Point     fGrowBase;
	private boolean   loading;


	private static Icon sPendingImageIcon,
	sMissingImageIcon;

	private static final boolean DEBUG = false;

	static final String IMAGE_CACHE_PROPERTY = "imageCache";

	private static final int
	DEFAULT_WIDTH = 32,
	DEFAULT_HEIGHT= 32,
	DEFAULT_BORDER=  2;

}