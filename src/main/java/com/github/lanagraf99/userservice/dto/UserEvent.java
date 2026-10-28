package com.github.lanagraf99.userservice.dto;

public class UserEvent {

    private String email;
    private Operation operation;

    public UserEvent() { }

    public UserEvent(String email, Operation operation) {
        this.email = email;
        this.operation = operation;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Operation getOperation() { return operation; }
    public void setOperation(Operation operation) { this.operation = operation; }
}
