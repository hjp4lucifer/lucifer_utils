package cn.lucifer.http;

/**
 * <p>
 * A simple class encapsulating a name/value pair.
 * </p>
 * 
 * @author lucifer.yun
 */
public class NameValuePair {

	protected String name;

	protected Object value;

	public NameValuePair() {
		// TODO Auto-generated constructor stub
	}

	public NameValuePair(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the {@link #name} to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the {@link #value}
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the {@link #value} to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{name=");
		builder.append(name);
		builder.append(", value=");
		builder.append(value);
		builder.append("}");
		return builder.toString();
	}

}
