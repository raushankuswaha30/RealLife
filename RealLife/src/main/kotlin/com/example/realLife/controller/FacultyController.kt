package com.example.realLife.controller
import com.example.realLife.model.FacultyProfile
import com.example.realLife.repository.FacultyRepository
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = ["*"])
class FacultyController(
    private val faculty: FacultyRepository
) {

    @GetMapping
    fun all(): List<FacultyProfile> = faculty.findAll(Sort.by(Sort.Direction.ASC, "name"))

    @GetMapping("/{id}")
    fun one(@PathVariable id: String): FacultyProfile = faculty.findById(id).orElseThrow {
        ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty member not found")
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody profile: FacultyProfile): FacultyProfile {
        validateFaculty(profile)
        val existing = profile.facultyCode.takeIf { it.isNotBlank() }?.let(faculty::findFirstByFacultyCode)
            ?: faculty.findFirstByNameIgnoreCaseAndDepartmentIgnoreCase(profile.name.trim(), profile.department.trim())

        if (existing != null) {
            return faculty.save(
                profile.copy(
                    id = existing.id,
                    name = profile.name.trim(),
                    department = profile.department.trim(),
                    email = profile.email.trim()
                )
            )
        }
        if (profile.email.isNotBlank() && faculty.existsByEmailIgnoreCase(profile.email.trim())) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "A faculty member with this email already exists")
        }
        return faculty.save(profile.copy(id = null, email = profile.email.trim()))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody profile: FacultyProfile): FacultyProfile {
        if (!faculty.existsById(id)) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty member not found")
        validateFaculty(profile)
        val duplicate = profile.email.isNotBlank() && faculty.findAll().any {
            it.id != id && it.email.equals(profile.email.trim(), ignoreCase = true)
        }
        if (duplicate) throw ResponseStatusException(HttpStatus.CONFLICT, "A faculty member with this email already exists")
        return faculty.save(profile.copy(id = id, email = profile.email.trim()))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String) {
        if (!faculty.existsById(id)) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty member not found")
        faculty.deleteById(id)
    }

    private fun validateFaculty(profile: FacultyProfile) {
        if (profile.name.isBlank() || profile.title.isBlank() || profile.department.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Name, title, and department are required")
        }
    }
}