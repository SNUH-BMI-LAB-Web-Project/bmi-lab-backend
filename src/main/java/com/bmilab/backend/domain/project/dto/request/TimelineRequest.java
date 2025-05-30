package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.domain.project.enums.TimelineType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record TimelineRequest(
        @Schema(description = "타임라인 제목", example = "AI 도입 전략 타임라인")
        String title,

        @Schema(description = "타임라인 날짜", example = "2025-05-10")
        LocalDate date,

        @Schema(type = "string", pattern = "HH:mm", example = "14:30")
        LocalTime startTime,
        
        @Schema(type = "string", pattern = "HH:mm", example = "14:30")
        LocalTime endTime,

        @Schema(description = "미팅 장소", example = "7층 회의실 1")
        String meetingPlace,

        @Schema(description = "타임라인 유형 (예: 정기 타임라인, 연구 발표)", example = "정기 타임라인")
        TimelineType type,

        @Schema(description = "타임라인 내용", example = "시장 조사 결과를 바탕으로 AI 기술 도입 로드맵 설정")
        String summary,

        @Schema(description = "타임라인 첨부파일 ID 목록")
        List<UUID> fileIds
) {
}
