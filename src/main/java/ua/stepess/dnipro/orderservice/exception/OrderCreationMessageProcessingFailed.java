package ua.stepess.dnipro.orderservice.exception;

public class OrderCreationMessageProcessingFailed extends RuntimeException {
    public OrderCreationMessageProcessingFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
