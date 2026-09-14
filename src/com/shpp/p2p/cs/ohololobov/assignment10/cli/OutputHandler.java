package com.shpp.p2p.cs.ohololobov.assignment10.cli;

import com.shpp.p2p.cs.ohololobov.assignment10.dto.ResultsDTO;

/**
 * interface contains contract for output data handlers from calculator
 */
public interface OutputHandler {
    void send(ResultsDTO result);
}
