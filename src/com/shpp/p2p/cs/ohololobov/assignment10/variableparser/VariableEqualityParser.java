package com.shpp.p2p.cs.ohololobov.assignment10.variableparser;

import com.shpp.p2p.cs.ohololobov.assignment10.service.Validator;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.map.primitive.MutableCharIntMap;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * this class pars variable equalities, received from ArgsDispatcher as list of variable equalities
 */
public class VariableEqualityParser {
    Logger log = LoggerFactory.getLogger(VariableEqualityParser.class);

    /**
     * constant contains string that used as equality sign
     */
    private static final String EQUALITY_SIGN = "=";
    /**
     * instance of VariableEqualityParser
     */
    private static VariableEqualityParser variableParser;

    /**
     * singleton for creating instance of class VariableEqualityParser as Singleton
     *
     * @return instance of class VariableEqualityParser
     */
    public static VariableEqualityParser getInstance() {
        if (variableParser == null) {
            variableParser = new VariableEqualityParser();
        }

        return variableParser;
    }

    /**
     * The method pars only variables equalities the names of which appear in the expression
     *
     * @param equalities            list of all presented equalities from ArgsDispatcher
     * @param variablesInExpression map, that describes connection between variables names
     *                              and positions of their values in array of variables values,
     *                              can be null, if there is no variable in expression present
     * @return array of MutableDoubleList of variables values
     */
    public MutableDoubleList[] parse(List<String> equalities, MutableCharIntMap variablesInExpression) {
        MutableDoubleList[] variablesValues = null;
        if (equalities != null) {
            variablesValues = new MutableDoubleList[variablesInExpression.size()];
            log.debug("variablesInExpression: {}", variablesInExpression);
            log.debug("equalities: {}", equalities);

            for (String equality : equalities) {
                String[] equalityAsArray = equality.split(EQUALITY_SIGN);
                log.debug("equalityAsArray: {}", Arrays.toString(equalityAsArray));
                Validator.validateVariableEquality(equalityAsArray);
                char variableKey = equalityAsArray[0].charAt(0);
                if (variablesInExpression.containsKey(variableKey)) {
                    int slotNumber = variablesInExpression.get(variableKey);
                    if (variablesValues[slotNumber] == null)
                        variablesValues[slotNumber] = new DoubleArrayList();
                    variablesValues[slotNumber].add(Double.parseDouble(equalityAsArray[1]));
                }
            }
            Validator.checkAllVariablesPresence(variablesInExpression, variablesValues);
            Validator.validateVariableValuesQuantities(variablesValues);
        }
        return variablesValues;
    }
}
