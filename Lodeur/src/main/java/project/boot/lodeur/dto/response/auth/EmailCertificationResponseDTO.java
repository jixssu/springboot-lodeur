package project.boot.lodeur.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import project.boot.lodeur.common.ResponseCode;
import project.boot.lodeur.common.ResponseMessage;
import project.boot.lodeur.dto.response.ResponseDTO;

@Getter

public class EmailCertificationResponseDTO extends ResponseDTO{

	private EmailCertificationResponseDTO() {
		super();
	}

	public static ResponseEntity<EmailCertificationResponseDTO> success() {

		EmailCertificationResponseDTO responseBody = new EmailCertificationResponseDTO();
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);

	}

	public static ResponseEntity<ResponseDTO> duplicateId() {

		ResponseDTO responseBody = new ResponseDTO(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);

	}

	public static ResponseEntity<ResponseDTO> mailSendFail() {

		ResponseDTO responseBody = new ResponseDTO(ResponseCode.MAIL_FAIL, ResponseMessage.MAIL_FAIL);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);

	}

}
