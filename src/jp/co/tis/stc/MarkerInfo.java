package jp.co.tis.stc;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Pattern REGEX = Pattern.compile("^id=(\\d+):name=(.+):pos\\[0\\]=([\\d.]+):pos\\[1\\]=([\\d.]+)$");
	private Integer id;
	private String fileName;
	private Float[] pos = new Float[2];
	
	public MarkerInfo(String markerStr) {
		Matcher m = REGEX.matcher(markerStr);
		if (!m.find()) throw new RuntimeException("not markerInfo string : " + markerStr);
		id = Integer.parseInt(m.group(1));
		fileName = m.group(2);
		pos[0] = Float.parseFloat(m.group(3));
		pos[1] = Float.parseFloat(m.group(4));
	}

	public Integer getId() {
		return id;
	}

	public String getFileName() {
		return fileName;
	}

	public Float[] getPos() {
		return pos;
	}
}
