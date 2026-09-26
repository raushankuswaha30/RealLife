package com.example.realLife.repository

import com.example.realLife.model.Note
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository: MongoRepository<Note, String> {
}