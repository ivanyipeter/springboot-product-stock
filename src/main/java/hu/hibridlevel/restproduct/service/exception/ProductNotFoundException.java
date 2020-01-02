package hu.hibridlevel.restproduct.service.exception;

public class ProductNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Product not found with requested id: ";

    public ProductNotFoundException(Integer id) {
        super(MESSAGE + id);
    }

}
