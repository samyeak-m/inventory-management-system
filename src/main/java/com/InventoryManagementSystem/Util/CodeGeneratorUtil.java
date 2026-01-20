package com.InventoryManagementSystem.Util;

import com.InventoryManagementSystem.Model.Generate;
import com.InventoryManagementSystem.Model.CodeName;
import com.InventoryManagementSystem.Repository.CodeNameRepository;

import java.util.List;
import java.util.function.Function;

public class CodeGeneratorUtil {

    public static String generateUniqueCode(String categoryType, Generate generate,
            CodeNameRepository codeNameRepository, String type) {
        if (categoryType == null || categoryType.trim().isEmpty()) {
            return "";
        }
        String word = categoryType.trim().toUpperCase().replaceAll("[^A-Z]", "");
        if (word.length() < 2) {
            word = word.length() == 1 ? word + "X" : "XX";
        }

        // For SubCategory, Category, or Branch, always use two-letter codes
        if ("SubCategory".equalsIgnoreCase(type)
                || "Category".equalsIgnoreCase(type)
                || "Branch".equalsIgnoreCase(type)
                || "Department".equalsIgnoreCase(type)
                || "Country".equalsIgnoreCase(type)
                || "City".equalsIgnoreCase(type)
                || "State".equalsIgnoreCase(type)
                || "Address".equalsIgnoreCase(type)
                || "Item".equalsIgnoreCase(type)) {
            // Try all unique two-letter combinations from the word
            for (int i = 0; i < word.length() - 1; i++) {
                for (int j = i + 1; j < word.length(); j++) {
                    String code = "" + word.charAt(i) + word.charAt(j);
                    if (!codeNameRepository.existsByCode(code)) {
                        return code;
                    }
                }
            }
            throw new IllegalStateException("No unique two-letter code available for: " + word);
        } else {
            // Original logic for other types
            String[] words = categoryType.trim().toUpperCase().split("\\s+");
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                String w = words[i].replaceAll("[^A-Z]", "");
                if (w.length() >= 2) {
                    code.append(w.substring(0, 2));
                } else if (w.length() == 1) {
                    code.append(w).append("X");
                } else {
                    code.append("XX");
                }
            }
            String baseCode = code.toString();
            String uniqueCode = baseCode;
            int suffix = 0;
            while (codeNameRepository.existsByCode(uniqueCode)) {
                String letters = "" + (char) ('A' + (suffix / 26)) + (char) ('A' + (suffix % 26));
                uniqueCode = baseCode + "-" + letters;
                suffix++;
                if (suffix >= 26 * 26) {
                    throw new IllegalStateException("Exhausted all unique two-letter codes.");
                }
            }
            return uniqueCode;
        }
    }

    public static <T> String generateNextCode(String baseCode, List<T> items, Function<T, String> codeExtractor) {
        int max = 0;
        String prefix = baseCode + "-";
        for (T item : items) {
            String code = codeExtractor.apply(item);
            if (code != null && code.startsWith(prefix)) {
                String numberPart = code.substring(prefix.length());
                try {
                    int num = Integer.parseInt(numberPart);
                    if (num > max)
                        max = num;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return prefix + String.format("%03d", max + 1);
    }

    // Numeric: next 3-digit for Category (001, 002, ...)
    public static String generateNextCategoryNumericCode(CodeNameRepository codeNameRepository) {
        List<CodeName> items = codeNameRepository.findByTypeIgnoreCase("Category");
        int max = items.stream()
                .filter(cn -> Boolean.TRUE.equals(cn.getDisplay()))
                .map(CodeName::getCode)
                .filter(code -> code != null && code.matches("\\d{3}"))
                .mapToInt(Integer::parseInt)
                .max().orElse(0);
        String next = String.format("%03d", max + 1);
        while (codeNameRepository.existsByCode(next)) {
            max++;
            next = String.format("%03d", max + 1);
        }
        return next;
    }

    // Numeric: next sub-code under a parent prefix (e.g., 001 + 001 => 001001)
    public static String generateNextSubCategoryNumericCode(String categoryCode,
            CodeNameRepository codeNameRepository) {
        String prefix = categoryCode;
        List<CodeName> items = codeNameRepository.findByTypeIgnoreCase("SubCategory");
        int max = items.stream()
                .filter(cn -> Boolean.TRUE.equals(cn.getDisplay()))
                .map(CodeName::getCode)
                .filter(code -> code != null
                        && code.startsWith(prefix)
                        && code.length() == prefix.length() + 3
                        && code.substring(prefix.length()).matches("\\d{3}"))
                .mapToInt(code -> Integer.parseInt(code.substring(prefix.length())))
                .max().orElse(0);

        String nextSuffix = String.format("%03d", max + 1);
        String next = prefix + nextSuffix;
        while (codeNameRepository.existsByCode(next)) {
            max++;
            nextSuffix = String.format("%03d", max + 1);
            next = prefix + nextSuffix;
        }
        return next;
    }
}