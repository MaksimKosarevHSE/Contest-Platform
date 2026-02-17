package com.maksim.standings_service.repository;

import com.maksim.standings_service.entity.ContestUser;
import com.maksim.standings_service.entity.key.ContestUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestUserRepository extends JpaRepository<ContestUser, ContestUserId> {
}
