package com.example.realLife.repository

import com.example.realLife.model.Student
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : MongoRepository<Student, String> {
    fun findByPhone(phone: String): Student?
    fun findByEmail(email: String): Student?
}