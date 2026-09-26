package com.example.realLife.controller

import com.example.realLife.model.Note
import com.example.realLife.repository.NoteRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = ["*"])
class NoteController(
    private val repository: NoteRepository
) {
    @PostMapping
    fun uploadNoteLink(@RequestBody note: Note): ResponseEntity<Note> {
        return ResponseEntity.ok(repository.save(note))
    }
    @GetMapping
    fun getAllNotes(): ResponseEntity<List<Note>> {
        return ResponseEntity.ok(repository.findAll())
    }
    @DeleteMapping("/{id}")
    fun deleteNote(@PathVariable id: String): ResponseEntity<Void> {
        repository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}