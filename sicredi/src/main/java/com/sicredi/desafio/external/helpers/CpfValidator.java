package com.sicredi.desafio.external.helpers;

public class CpfValidator {

    private CpfValidator() {
    }

    public static boolean isValid(String raw) {
        String cpf = normalize(raw);
        if (!hasValidLength(cpf)) return false;
        if (allDigitsEqual(cpf)) return false;

        int dv1 = calculateDigit(cpf, 9, 10);
        int dv2 = calculateDigit(cpf, 10, 11);

        return dv1 == charToInt(cpf.charAt(9))
                && dv2 == charToInt(cpf.charAt(10));
    }

    private static String normalize(String raw) {
        if (raw == null) return "";
        return raw.replaceAll("\\D", "");
    }

    private static boolean hasValidLength(String cpf) {
        return cpf.length() == 11;
    }

    private static boolean allDigitsEqual(String cpf) {
        return cpf.chars().distinct().count() == 1;
    }

    private static int calculateDigit(String cpf, int length, int weightStart) {
        int sum = 0;
        for (int i = 0, w = weightStart; i < length; i++, w--) {
            sum += charToInt(cpf.charAt(i)) * w;
        }
        int dv = 11 - (sum % 11);
        return (dv >= 10) ? 0 : dv;
    }

    private static int charToInt(char c) {
        return c - '0';
    }
}
