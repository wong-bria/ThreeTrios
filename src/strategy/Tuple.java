package strategy;

/**
 * Represents a Tuple data type.
 * Stores two objects of arbitrary type K and V.
 * This data type is immutable, but the objects may not be.
 * @param <K> A generic class.
 * @param <V> A generic class, may be the same as type K.
 */
public class Tuple<K, V> {
  private final K key;
  private final V value;

  /**
   * Constructor for Tuple.
   * @param key the key for this tuple.
   * @param value the value for this tuple.
   */
  public Tuple(K key, V value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Returns the key of this tuple.
   * @return the key of this tuple.
   */
  public K getKey() {
    return this.key;
  }

  /**
   * Returns the value of this tuple.
   * @return the value of this tuple.
   */
  public V getValue() {
    return this.value;
  }
}
