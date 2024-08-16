package project.boot.lodeur.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notice_num")
	@SequenceGenerator(name = "notice_num", sequenceName = "NOTICE_NUM", allocationSize = 1)
	private Integer notice_num;
	private String notice_title;
	private String notice_content;
	
	private String notice_writer;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate notice_registday;
	private Integer notice_hit;

	private String filename;
	private String filepath;

	
}
