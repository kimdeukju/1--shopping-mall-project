package org.spring.teamproject.dto;

import lombok.*;
import org.spring.teamproject.entity.BoardEntity;
import org.spring.teamproject.entity.ItemEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileDto {

    private Long no;
    private String fileName;
    private String newFileName;



}
