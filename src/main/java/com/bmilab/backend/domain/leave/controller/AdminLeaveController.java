package com.bmilab.backend.domain.leave.controller;

import com.bmilab.backend.domain.leave.dto.request.RejectLeaveRequest;
import com.bmilab.backend.domain.leave.service.LeaveService;
import com.bmilab.backend.global.annotation.OnlyAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OnlyAdmin
@RestController
@RequestMapping("/admin/leaves")
@RequiredArgsConstructor
public class AdminLeaveController implements AdminLeaveApi {
    private final LeaveService leaveService;

    @PostMapping("/{leaveId}/approve")
    public ResponseEntity<Void> approveLeave(@PathVariable long leaveId) {
        leaveService.approveLeave(leaveId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{leaveId}/reject")
    public ResponseEntity<Void> rejectLeave(
            @PathVariable long leaveId,
            @RequestBody @Valid RejectLeaveRequest request
    ) {
        leaveService.rejectLeave(leaveId, request);
        return ResponseEntity.ok().build();
    }
}
