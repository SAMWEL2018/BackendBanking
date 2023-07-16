package com.example.transcsystem.repository;

import com.example.transcsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String>  {



}
