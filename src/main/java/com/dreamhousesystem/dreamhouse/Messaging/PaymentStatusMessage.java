package com.dreamhousesystem.dreamhouse.Messaging;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentStatusMessage {

    // ✅ correlationId (camelCase) est le nom principal
    // @JsonAlias accepte aussi correlation_id (snake_case) en entrée
    @JsonProperty("correlationId")
    @JsonAlias("correlation_id")
    private String correlationId;

    private String status;

    // ✅ publicationId (camelCase) est le nom principal
    // @JsonAlias accepte aussi publication_id (snake_case) en entrée
    @JsonProperty("publicationId")
    @JsonAlias("publication_id")
    private Integer publicationId;

    private String email;
    private Double amount;
    private String transactionId;

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