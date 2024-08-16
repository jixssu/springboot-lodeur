package project.boot.lodeur.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.boot.lodeur.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	
//	  Page<Answer> findAllByQuestion(Question question, Pageable pageable);

}
