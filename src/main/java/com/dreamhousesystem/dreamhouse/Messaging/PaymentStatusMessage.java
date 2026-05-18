package com.dreamhousesystem.dreamhouse.Messaging;

public class PaymentStatusMessage {

    private String correlationId;   // ex-paymentId → correspond à correlationId du payment-service
    private String status;          // SUCCESS / FAILED / PENDING
    private Integer publicationId;  // ✅ AJOUT : id du bien à activer/rejeter
    private String email;
    private Double amount;
    private String transactionId;   // provider_reference Campay

    public PaymentStatusMessage() {}

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getPublicationId() { return publicationId; }
    public void setPublicationId(Integer publicationId) { this.publicationId = publicationId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}