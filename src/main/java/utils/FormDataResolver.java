package utils;

import com.github.javafaker.Faker;
import enums.AccountFields;          // make sure your package is lowercase `enums`
import context.ScenarioContext;

import java.util.HashMap;
import java.util.Map;

public final class FormDataResolver {

    private FormDataResolver() {}

    /**
     * Convert a DataTable map into form params with three simple rules:
     * - "faker"  -> generate via AccountFields.fromKey(key)
     * - "saved"  -> scenarioContext.get(key)
     * - other    -> literal value
     * Also: always remember email/password in ScenarioContext (generated or literal).
     */
    public static Map<String, String> resolve(
            Map<String, String> originalParams,
            Faker faker,
            ScenarioContext scenarioContext
    ) {
        Map<String, String> result = new HashMap<>();
        if (originalParams == null) return result;

        for (Map.Entry<String, String> entry : originalParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value != null && value.equalsIgnoreCase("faker")) {
                try {
                    AccountFields field = AccountFields.fromKey(key);
                    value = field.generate(faker);
                } catch (IllegalArgumentException ex) {
                    // no mapping, leave value unchanged
                }
            } else if (value != null && value.equalsIgnoreCase("saved")) {
                String saved = scenarioContext.get(key, String.class);
                if (saved == null || saved.trim().isEmpty()) {
                    throw new IllegalStateException("No saved value for key '" + key + "'");
                }
                value = saved;
            }

            // always remember credentials
            if ("email".equalsIgnoreCase(key) || "password".equalsIgnoreCase(key)) {
                scenarioContext.set(key, value);
            }

            result.put(key, value);
        }
        return result;
    }
}
