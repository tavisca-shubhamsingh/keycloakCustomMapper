package com.keycloak.mapper.service.reposiotry;

import com.keycloak.mapper.service.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
}
