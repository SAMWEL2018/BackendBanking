package com.example.transcsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * @author Samwel Wafula
 * Created on July 08, 2023.
 * Time 6:41 PM
 */

@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessingResp {
    private String status;
    private String responseMessage;
    private String responseCode;
    private Object transaction;
    private Object balance;
    private Object user;

}
