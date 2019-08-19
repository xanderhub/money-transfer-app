package service;

import entities.StandardResponse;
import java.math.BigDecimal;

public interface TransferService {
    StandardResponse transfer(final int from, final int to, final BigDecimal amount);
}
