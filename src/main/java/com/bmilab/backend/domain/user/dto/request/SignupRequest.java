package com.bmilab.backend.domain.user.dto.request;

public record SignupRequest(
        String name,
        String email,
        String password,
        String department
) {
}
