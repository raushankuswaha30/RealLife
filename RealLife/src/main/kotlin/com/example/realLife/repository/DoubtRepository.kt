package com.example.realLife.repository

import com.example.realLife.model.Doubt
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DoubtRepository: MongoRepository<Doubt, String> {

}