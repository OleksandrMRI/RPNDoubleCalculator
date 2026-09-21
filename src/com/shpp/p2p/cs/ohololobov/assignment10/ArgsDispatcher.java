package com.shpp.p2p.cs.ohololobov.assignment10;

import com.shpp.p2p.cs.ohololobov.assignment10.dto.InputRawDataDTO;
import com.shpp.p2p.cs.ohololobov.assignment10.service.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class routes args[] of main() to string expression and list of variable equalities
 */
public class ArgsDispatcher implements Dispatcher {
    /**
     * instance of ArgsDispatcher
     */
    private static ArgsDispatcher instance;

    /**
     * /**
     * singleton for creating instance of class ArgsDispatcher as Singleton
     *
     * @return instance of class ArgsDispatcher
     */
    public static ArgsDispatcher getInstance() {
        if (instance == null) {
            instance = new ArgsDispatcher();
        }
        return instance;
    }

    /**
     * The method routs arguments from args[] of main() to string expression and list of variables equalities
     *
     * @param dataArray args[] from main()
     * @return DTO to transfer routed data to parsing
     * @throws IOException if args[] from main is empty
     */
    @Override
    public InputRawDataDTO route(String[] dataArray) throws IOException {
        Validator.isArgsEmpty(dataArray);
        String expression = dataArray[0];
        List<String> equalities = null;
        if (dataArray.length > 1) {
            equalities = new ArrayList<>(Arrays.asList(dataArray).subList(1, dataArray.length));
        }
        return new InputRawDataDTO(expression, equalities);
    }
}
