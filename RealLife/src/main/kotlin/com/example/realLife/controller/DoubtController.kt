package com.example.realLife.controller

import com.example.realLife.model.Doubt
import com.example.realLife.repository.DoubtRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/doubts")
@CrossOrigin(origins = ["*"])
class DoubtController(
    private val repository: DoubtRepository
) {
    @PostMapping
    fun saveDoubt(@RequestBody doubt: Doubt): ResponseEntity<Doubt> {
        return ResponseEntity.ok(repository.save(doubt))
    }
    @GetMapping
    fun getDoubt(): ResponseEntity<List<Doubt>> {
        return ResponseEntity.ok(repository.findAll())
    }
    @PutMapping("/{id}/answer")
    fun answerDoubt(
        @PathVariable id: String,
        @RequestBody body:Map<String,String>
    ): ResponseEntity<Doubt>{
        val doubt=repository.findById(id).orElse(null)?:return ResponseEntity.notFound().build()
        doubt.professorAnswer=body["answer"]?:return ResponseEntity.notFound().build()
        doubt.status="RESOLVED"
        return ResponseEntity.ok(repository.save(doubt))
    }
}