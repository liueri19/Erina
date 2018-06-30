package erina;

/**
 * Simple immutable data class representing a pair of values.
 *
 * @version 1.0
 * @author Eric
 */
public class Pair<K, V> {
	private final K key;
	private final V value;

	/**
	 * Constructs a new Pair mapping the specified key to the specified value.
	 * @param key	the key in this Pair
	 * @param value	the value in this Pair
	 */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Gets the key in this Pair.
	 * @return	the key
	 */
	public final K getKey() { return key; }

	/**
	 * Gets the value in this Pair.
	 * @return	the value
	 */
	public final V getValue() { return value; }
}

