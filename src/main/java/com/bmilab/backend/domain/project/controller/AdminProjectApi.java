package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.ExternalProfessorRequest;
import com.bmilab.backend.domain.project.dto.response.ExternalProfessorFindAllResponse;
import com.bmilab.backend.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "(Admin) External Professor", description = "(관리자용) 외부 교수 관리 API")
public interface AdminProjectApi {

    @Operation(summary = "외부 교수 등록", description = "외부 교수 정보를 등록하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "외부 교수 등록 성공"
                    )
            }
    )
    ResponseEntity<Void> createExternalProfessor(@RequestBody ExternalProfessorRequest request);

    @Operation(summary = "외부 교수 수정", description = "외부 교수 정보를 수정하는 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "외부 교수 정보 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "외부 교수 정보를 찾을 수 업습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> updateExternalProfessor(
            @PathVariable Long professorId,
            @RequestBody ExternalProfessorRequest request
    );

    @Operation(summary = "외부 교수 목록 조회", description = "등록된 외부 교수 목록을 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "외부 교수 목록 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "외부 교수 정보를 찾을 수 업습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<ExternalProfessorFindAllResponse> getAllExternalProfessors();

    @Operation(summary = "외부 교수 삭제", description = "외부 교수 정보를 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "외부 교수 정보 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "외부 교수 정보를 찾을 수 업습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteExternalProfessor(@PathVariable Long professorId);
}
