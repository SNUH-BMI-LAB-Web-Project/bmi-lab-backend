package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.entity.Timeline;
import com.bmilab.backend.domain.project.enums.TimelineType;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

public record TimelineFindAllResponse(
        List<TimelineSummary> timelines
) {
    public static TimelineFindAllResponse of(List<Timeline> timelines) {
        return new TimelineFindAllResponse(
                timelines
                    .stream()
                    .map(TimelineSummary::from)
                    .toList()
        );
    }

    @Builder
    public record TimelineSummary(
            @Schema(description = "타임라인 ID", example = "101")
            Long timelineId,

            @Schema(description = "연구 ID", example = "55")
            Long projectId,

            @Schema(description = "타임라인 기록 작성자 정보")
            UserSummary recorder,

            @Schema(description = "타임라인 제목", example = "UX 개선 회의")
            String title,

            @Schema(description = "타임라인 날짜", example = "2025-05-10")
            LocalDate date,

            @Schema(description = "타임라인 시작 시간 (24시간제)", example = "14:00")
            LocalTime startTime,

            @Schema(description = "타임라인 종료 시간 (24시간제)", example = "15:30")
            LocalTime endTime,

            @Schema(description = "타임라인 유형", example = "연구 발표")
            TimelineType timelineType,

            @Schema(description = "타임라인 요약", example = "디자인 시스템 전면 개편 논의 및 일정 정리")
            String summary
    ) {
        public static TimelineSummary from(Timeline timeline) {
            return TimelineSummary
                    .builder()
                    .timelineId(timeline.getId())
                    .projectId(timeline.getProject().getId())
                    .recorder(UserSummary.from(timeline.getRecorder()))
                    .title(timeline.getTitle())
                    .date(timeline.getDate())
                    .startTime(timeline.getStartTime())
                    .endTime(timeline.getEndTime())
                    .timelineType(timeline.getType())
                    .summary(timeline.getSummary())
                    .build();
        }
    }
}
