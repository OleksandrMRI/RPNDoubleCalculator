package com.shpp.p2p.cs.ohololobov.assignment10.variableparser;

import java.util.List;

/**
 * this class contains logic of normalizing of variable equality to parsing
 */
public class VariableEqualityNormalizer {
    public static final String VALID_SEPARATOR_BETWEEN_CHARS = " ";
    public static final String REPLACEMENT = "";
    /**
     * instance of VariableEqualityNormalizer
     */
    private static VariableEqualityNormalizer instance;

    /**
     * /**
     * singleton for creating instance of class VariableEqualityNormalizer as Singleton
     *
     * @return instance of class VariableEqualityNormalizer
     */
    public static VariableEqualityNormalizer getInstance() {
        if (instance == null) {
            instance = new VariableEqualityNormalizer();
        }
        return instance;
    }

    /**
     * the method normalizes string of variable equality with replacing all spaces to empty strings
     *
     * @param variablesEqualities list of all variable equalities from ArgsDispatcher
     * @return list of variable equalities without spaces
     */
    public List<String> normalizeVariablesEqualities(List<String> variablesEqualities) {
        for (int i = 0; i < variablesEqualities.size(); i++) {
            variablesEqualities.set(i, variablesEqualities.get(i).replace(VALID_SEPARATOR_BETWEEN_CHARS, REPLACEMENT));
        }
        return variablesEqualities;
    }
}
