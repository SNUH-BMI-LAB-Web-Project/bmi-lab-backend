package com.bmilab.backend.domain.report.dto.response;

import com.bmilab.backend.domain.report.entity.UserReport;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UserReportDetail(
        Long userReportId,
        ReportSummary report,
        UserSummary user,
        String fileUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserReportDetail from(UserReport userReport) {
        return UserReportDetail
                .builder()
                .userReportId(userReport.getId())
                .report(ReportSummary.from(userReport.getReport()))
                .fileUrl(userReport.getFileUrl())
                .createdAt(userReport.getCreatedAt())
                .updatedAt(userReport.getUpdatedAt())
                .build();
    }
}