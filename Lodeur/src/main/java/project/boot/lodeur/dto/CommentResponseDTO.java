package project.boot.lodeur.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentResponseDTO {
	private Long id;
	private String commentWriter;
	private String commentContents;
	private LocalDate commentCreatedTime;
}
