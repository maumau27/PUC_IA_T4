package dataTypes;

import java.awt.Image;

public class ImageCut {
	public Image image;
	public IVector2D origin;
	public IVector2D size;
	
	public ImageCut( Image img , int originX , int originY , int sizeX , int sizeY ) {
		this.image = img;
		
		origin = 	new IVector2D( originX , originY );
		size 	= 	new IVector2D( sizeX , sizeY );
	}
}
