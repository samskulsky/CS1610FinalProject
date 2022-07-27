public class Tile {

  public final int x, y;
	public final int r, g, b;
	public final int length;
  // public final String hexCode;

	public Tile(int x, int y, int r, int g, int b, int lengthInPixels) {
		this.x = x;
    this.y = y;
    this.r = r;
		this.g = g;
		this.b = b;
    // hexCode = "#" + new RGBColor(this.r, this.g, this.b).toHex();
		length = lengthInPixels;
	}

  /* I'm just going to disable all these getters in favor of making the fields public
	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public int getLength() {
		return length;
	}
  */
	
}