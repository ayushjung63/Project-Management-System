package com.ayush.proms.repo;

import com.ayush.proms.model.User;
import com.ayush.proms.pojos.UserMinimalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    User findUserByEmail(String email);

    @Query(nativeQuery=true,value="select u.id as id, u.full_name as name from users u where u.faculty=?1 and u.semester=?2 and u.id != ?3")
    Optional<List<UserMinimalDetail>> findUserBySemesterAndFaculty(String faculty, String semester, Long id);
}
