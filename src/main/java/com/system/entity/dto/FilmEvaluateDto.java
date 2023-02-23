package com.system.entity.dto;


import com.system.entity.FilmEvaluate;
import lombok.Data;

@Data
public class FilmEvaluateDto extends FilmEvaluate {
    private String uname;
    private String fname;
}