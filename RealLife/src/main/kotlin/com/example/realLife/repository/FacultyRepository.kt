package com.example.realLife.repository

import com.example.realLife.model.FacultyProfile
import org.springframework.data.mongodb.repository.MongoRepository

interface FacultyRepository : MongoRepository<FacultyProfile, String> {
    fun existsByEmailIgnoreCase(email: String): Boolean
    fun findFirstByFacultyCode(facultyCode: String): FacultyProfile?
    fun findFirstByNameIgnoreCaseAndDepartmentIgnoreCase(name: String, department: String): FacultyProfile?
}