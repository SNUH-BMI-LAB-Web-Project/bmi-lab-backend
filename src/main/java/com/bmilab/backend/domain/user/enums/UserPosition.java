package com.bmilab.backend.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserPosition {
    PROFESSOR("교수"),
    CO_PRINCIPAL_INVESTIGATOR("공동연구책임자"),
    POSTDOCTORAL_RESEARCHER("박사후 연구원"),
    PHD_STUDENT("대학원생-박사과정"),
    MASTERS_STUDENT("대학원생-석사과정"),
    TRANSLATIONAL_MEDICINE_TRAINEE("융합의학연수생"),
    RESEARCHER_OR_INTERN("연구원 및 인턴"),
    ADMINISTRATIVE_STAFF("행정");

    private final String description;

    public static UserPosition fromString(String position) {
        for (UserPosition value : values()) {
            if (value.name().equalsIgnoreCase(position)) {
                return value;
            }
        }
        return null;
    }
}
