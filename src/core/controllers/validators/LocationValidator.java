package core.controllers.validators;

public class LocationValidator {

    public static void validateLocationId(String id) {
        if (id == null || id.length() != 3) {
            throw new IllegalArgumentException("El ID debe tener exactamente 3 caracteres.");
        }
        if (!id.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("El ID solo puede contener letras mayúsculas.");
        }
    }

    public static void validateStringField(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
    }

    public static double parseCoordinate(String coordStr, String coordName, double min, double max) {
        try {
            double coord = Double.parseDouble(coordStr);
            if (coord < min || coord > max) {
                throw new IllegalArgumentException("La " + coordName + " debe estar entre " + min + " y " + max + ".");
            }
            return coord;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La " + coordName + " debe ser un número válido.");
        }
    }
}