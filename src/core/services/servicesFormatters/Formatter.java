package core.services.servicesFormatters;

/**
 * Generic interface for formatting an object of type T into an array of Strings.
 * This is useful for displaying object data in tabular formats (e.g., JTables).
 *
 * @param <T> The type of object to be formatted.
 */
public interface Formatter<T> {
    /**
     * Formats the given object into an array of Strings.
     * Each element in the array represents a formatted attribute of the object.
     * @param object The object to format.
     * @return An array of Strings representing the formatted object data.
     */
    String[] format(T object);
}