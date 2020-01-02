package hu.hibridlevel.restproduct.service.exception;

public class StockNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Stock not found with requested id: ";

    public StockNotFoundException(Integer id) {
        super(MESSAGE + id);
    }

}

