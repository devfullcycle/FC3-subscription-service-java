package com.fullcycle.codeflix.subscription.domain.payment;

public record Transaction(String transactionId, String errorMessage) {

    public boolean isSuccess() {
        return errorMessage != null;
    }

    public static Transaction success(String transactionId) {
        return new Transaction(transactionId, null);
    }

    public static Transaction failure(String transactionId, String errorMessage) {
        return new Transaction(transactionId, errorMessage);
    }
}
