package com.InventoryManagementSystem.Util;

import java.util.List;
import java.util.function.Function;
import com.InventoryManagementSystem.Model.Generate;
import com.InventoryManagementSystem.Repository.CodeNameRepository;

public class NumericCodeGeneratorUtil {

    /**
     * Generates a 6-digit numeric code of form BBBSSS where:
     * - BBB is a 3-digit base segment (padded with zeros)
     * - SSS is a 3-digit sequence starting at 001 and incremented per base
     *
     * Example: baseThreeDigits="1" -> base "001". If existing codes for that base
     * are
     * "001001","001002" the next returned value will be "001003".
     *
     * @param baseThreeDigits base segment (can be "1", "001", etc.). If null/empty
     *                        treated as "000".
     * @param items           list of domain objects to scan for existing codes
     * @param codeExtractor   function extracting the code string from each item
     * @param <T>             item type
     * @return new unique 6-digit code like "001001"
     */
    public static <T> String generateSixDigitCode(String baseThreeDigits, List<T> items,
            Function<T, String> codeExtractor) {
        String base = baseThreeDigits == null ? "" : baseThreeDigits.replaceAll("\\D", "");
        if (base.length() > 3)
            base = base.substring(0, 3);
        while (base.length() < 3)
            base = "0" + base;

        int maxSeq = 0;
        for (T item : items) {
            String code = codeExtractor.apply(item);
            if (code == null || code.length() < 6)
                continue;
            if (!code.startsWith(base))
                continue;
            String seqPart = code.substring(3, 6);
            try {
                int seq = Integer.parseInt(seqPart);
                if (seq > maxSeq)
                    maxSeq = seq;
            } catch (NumberFormatException ignored) {
            }
        }

        int next = maxSeq + 1;
        if (next > 999)
            throw new IllegalStateException("Sequence overflow for base " + base);
        return base + String.format("%03d", next);
    }

    /**
     * Generates the next 3-digit code (001..999) based on existing items' codes.
     * It extracts digits from each item's code using the provided extractor and
     * uses the first up-to-3 digits as the code value to compare.
     */
    public static <T> String generateNextThreeDigits(List<T> items, Function<T, String> codeExtractor) {
        int max = 0;
        for (T item : items) {
            String code = codeExtractor.apply(item);
            if (code == null)
                continue;
            String digits = code.replaceAll("\\D", "");
            if (digits.isEmpty())
                continue;
            String three = digits.length() >= 3 ? digits.substring(0, 3)
                    : String.format("%03d", Integer.parseInt(digits));
            try {
                int val = Integer.parseInt(three);
                if (val > max)
                    max = val;
            } catch (NumberFormatException ignored) {
            }
        }

        int next = max + 1;
        if (next > 999)
            throw new IllegalStateException("Branch code overflow");
        return String.format("%03d", next);
    }

    /**
     * Delegate to CodeGeneratorUtil to generate unique alphanumeric codes (e.g.,
     * two-letter department codes).
     */
    public static String generateUniqueCode(String categoryType, Generate generate,
            CodeNameRepository codeNameRepository, String type) {
        return CodeGeneratorUtil.generateUniqueCode(categoryType, generate, codeNameRepository, type);
    }
}
