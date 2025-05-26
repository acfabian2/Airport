package core.controllers.validators;

public class PlaneValidator {

    public static void validatePlaneId(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("El ID no puede estar vacío.");
        }
        if (id.length() != 7) {
            throw new IllegalArgumentException("ID inválido: debe tener exactamente 7 caracteres (2 letras seguidas de 5 números).");
        }

        String idLetters = id.substring(0, 2);
        String idNumbers = id.substring(2, 7);

        if (!idLetters.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("ID inválido: los primeros 2 caracteres deben ser letras mayúsculas.");
        }
        if (!idNumbers.matches("\\d{5}")) {
            throw new IllegalArgumentException("ID inválido: los últimos 5 caracteres deben ser números.");
        }
    }

    public static void validateStringField(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacía.");
        }
    }

    public static int parseMaxCapacity(String maxCapacityStr) {
        if (maxCapacityStr.isEmpty()) {
            throw new IllegalArgumentException("La capacidad máxima no puede estar vacía.");
        }
        try {
            return Integer.parseInt(maxCapacityStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La capacidad máxima debe ser numérica.");
        }
    }
}