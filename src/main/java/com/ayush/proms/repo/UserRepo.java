package com.ayush.proms.repo;

import com.ayush.proms.model.User;
import com.ayush.proms.pojos.UserMinimalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    @Query(nativeQuery=true,value="select  * from users u where u.email= ?1 ")
    Optional<User> findUserByEmail(String email);

    @Query(nativeQuery=true,value="select u.id as id, u.full_name as name from users u where u.faculty=?1 and u.semester=?2 and u.id != ?3 and u.role='STUDENT'")
    Optional<List<UserMinimalDetail>> findUserBySemesterAndFaculty(String faculty, String semester, Long id);

    @Query(nativeQuery=true,value="select u.id as id,u.full_name as name from users u where u.role=?1")
    List<UserMinimalDetail> findUsersByType(String userType);

    @Query(nativeQuery=true,value="select * from users where id=?1")
    User mygetById(Long userId);

    @Query(nativeQuery=true,value="select u.role,cast(count(u.id) as numeric) from users u group by u.role")
    List<Map<String,Integer>> findUserCountByType();
}
