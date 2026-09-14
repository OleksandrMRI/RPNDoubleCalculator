package com.shpp.p2p.cs.ohololobov.assignment10;

import com.shpp.p2p.cs.ohololobov.assignment10.dto.InputRawDataDTO;

import java.io.IOException;

public interface Dispatcher {
    InputRawDataDTO route(String[] args) throws IOException;
}
