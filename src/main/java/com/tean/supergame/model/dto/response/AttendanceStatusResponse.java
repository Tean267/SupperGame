package com.tean.supergame.model.dto.response;

import com.tean.supergame.model.enums.AttendanceStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatusResponse {
    private String username;
    private AttendanceStatusEnum attendanceStatusEnum;
}