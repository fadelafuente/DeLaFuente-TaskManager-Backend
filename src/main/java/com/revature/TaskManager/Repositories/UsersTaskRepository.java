package com.revature.TaskManager.Repositories;

import com.revature.TaskManager.Entities.UsersTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersTaskRepository extends JpaRepository<UsersTask, Long> {
}