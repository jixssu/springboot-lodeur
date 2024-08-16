package project.boot.lodeur.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.boot.lodeur.member.certificationEntity.CertificationEntity;

@Repository
public interface CertificationRepository extends JpaRepository<CertificationEntity, String> {

	CertificationEntity findByMemberId(String memberId);


}
