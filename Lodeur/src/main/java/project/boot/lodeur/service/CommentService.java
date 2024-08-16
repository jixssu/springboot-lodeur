package project.boot.lodeur.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.boot.lodeur.dto.CommentRequestDTO;
import project.boot.lodeur.dto.CommentResponseDTO;
import project.boot.lodeur.entity.Comment;
import project.boot.lodeur.entity.Notice;
import project.boot.lodeur.repository.CommentRepository;
import project.boot.lodeur.repository.NoticeRepository;

@Service
@Transactional
public class CommentService {
	private final CommentRepository commentRepository;
	private final NoticeRepository noticeRepository;

	@Autowired
	public CommentService(CommentRepository commentRepository, NoticeRepository noticeRepository) {
		this.commentRepository = commentRepository;
		this.noticeRepository = noticeRepository;
	}

	public CommentResponseDTO save(CommentRequestDTO requestDTO) {
		Notice notice = noticeRepository.findById(requestDTO.getNotice_num())
				.orElseThrow(() -> new IllegalArgumentException("Invalid notice_num"));

		Comment comment = new Comment();
		comment.setCommentWriter(requestDTO.getCommentWriter());
		comment.setCommentContents(requestDTO.getCommentContents());
		comment.setCommentCreatedTime(LocalDate.now());
		comment.setNotice(notice);
		commentRepository.save(comment);

		CommentResponseDTO responseDTO = new CommentResponseDTO();
		responseDTO.setId(comment.getId());
		responseDTO.setCommentWriter(comment.getCommentWriter());
		responseDTO.setCommentContents(comment.getCommentContents());
		responseDTO.setCommentCreatedTime(comment.getCommentCreatedTime());

		return responseDTO;
	}

	public List<CommentResponseDTO> findByNoticeNum(Integer notice_num) {
		List<Comment> comments = commentRepository.findByNoticeNum(notice_num);
		return comments.stream().map(comment -> {
			CommentResponseDTO dto = new CommentResponseDTO();
			dto.setId(comment.getId());
			dto.setCommentWriter(comment.getCommentWriter());
			dto.setCommentContents(comment.getCommentContents());
			dto.setCommentCreatedTime(comment.getCommentCreatedTime());
			return dto;
		}).collect(Collectors.toList());
	}
}
