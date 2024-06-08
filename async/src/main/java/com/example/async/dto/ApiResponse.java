package com.example.async.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    int records;
     Object response;
}
