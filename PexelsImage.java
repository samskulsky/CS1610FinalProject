public class PexelsImage {
	
	private int id;
	private int width, height;
	private String avg_color;
	private String tiny;
	
	public PexelsImage(int id, int width, int height, String avg_color, String tiny) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.avg_color = avg_color;
		this.tiny = tiny;
	}

	public int getID() {
		return id;
	}

	public String getAvgColor() {
		return avg_color;
	}

	public String getTiny() {
		return tiny;
	}
	
}