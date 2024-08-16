package project.boot.lodeur.dto.request.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailCertificationRequestDTO {

	@NotBlank
	private String memberId;
	
	@Email
	@NotBlank
	private String memberEmail;
	
}
