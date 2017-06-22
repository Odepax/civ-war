package civwar.view.interfacing;

/** Represents any entity that can be represented by an image. */
public interface Depictable {
	/**
	 * Provides a way to depict this object.
	 *
	 * @return It could be used to create a {@link java.net.URL} object...
	 */
	public String getDepictingIconPath();
}
