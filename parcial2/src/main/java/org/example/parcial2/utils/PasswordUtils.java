package org.example.parcial2.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase utilitaria para el manejo de contraseñas con encriptación SHA1
 */
public class PasswordUtils {

    /**
     * Encripta una contraseña usando SHA1
     * @param password Contraseña en texto plano
     * @return Contraseña encriptada en SHA1 (formato hexadecimal)
     */
    public static String encryptPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }

        try {
            // Crear instancia de MessageDigest con algoritmo SHA1
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            // Convertir la contraseña a bytes y aplicar hash
            byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));

            // Convertir bytes a representación hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Algoritmo SHA-1 no disponible", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }

    /**
     * Verifica si una contraseña en texto plano coincide con su versión encriptada
     * @param plainPassword Contraseña en texto plano
     * @param encryptedPassword Contraseña encriptada almacenada en BD
     * @return true si las contraseñas coinciden, false en caso contrario
     */
    public static boolean verifyPassword(String plainPassword, String encryptedPassword) {
        if (plainPassword == null || encryptedPassword == null) {
            return false;
        }

        try {
            String hashedInput = encryptPassword(plainPassword);
            return hashedInput.equals(encryptedPassword);
        } catch (Exception e) {
            System.err.println("Error al verificar contraseña: " + e.getMessage());
            return false;
        }
    }

    public static void testPassword(String password) {
        System.out.println("Contraseña original: " + password);
        System.out.println("SHA1 Hash: " + encryptPassword(password));
        System.out.println("Longitud del hash: " + encryptPassword(password).length());
    }

    public static void main(String[] args) {
        // Ejemplos de uso
        testPassword("password123");
        testPassword("password456");
        testPassword("password789");
        testPassword("password321");

        // Ejemplo de verificación
        String original = "password123";
        String encrypted = encryptPassword(original);
        System.out.println("\n--- Verificación ---");
        System.out.println("¿Coinciden? " + verifyPassword(original, encrypted));
        System.out.println("¿Coinciden con contraseña incorrecta? " + verifyPassword("wrongpassword", encrypted));
    }
}
