package com.edcapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edcapplication.dao.ProblemDao;
import com.edcapplication.model.Problem;
import com.edcapplication.service.ProblemService;

@RestController
@CrossOrigin
@RequestMapping("/api/edcapplication")
public class ProblemController {

	@Autowired
	ProblemService problemService;
	
	/*
	 * @GetMapping("/problems") public List<ProblemDao> getAllProblems() { return
	 * problemService.getAllProblems(); }
	 */
	@GetMapping("/problems")
	 public ResponseEntity<List<ProblemDao>> getAllProblems() {
        List<ProblemDao> problems = problemService.getAllProblems();
        return ResponseEntity.ok(problems);
    }
	
	/*
	 * @GetMapping("/problems/{id}") public Problem getTeamById(@PathVariable("id")
	 * Long problemId) { return problemService.getProblemById(problemId); }
	 */
	
	 @GetMapping("/problems/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable("id") Long id) {
        Problem problem = problemService.getProblemById(id);
        return ResponseEntity.ok(problem);
    }
	
	/*
	 * @PostMapping(("/problems")) public ProblemDao createProblem(@RequestBody
	 * Problem problem) { return problemService.addProblem(problem); }
	 */
	
	@PostMapping(("/problems"))
    public ResponseEntity<ProblemDao> addProblem(@RequestBody ProblemDao dao) {
        ProblemDao saved = problemService.addProblem(dao);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
	
	@PutMapping("/problems/{id}")
    public ResponseEntity<Problem> updateProblem(@PathVariable("id") Long id, @RequestBody Problem problem) {
        Problem updated = problemService.updateProblem(id, problem);
        return ResponseEntity.ok(updated);
    }
	
	/*
	 * @PutMapping(("/problems")) public Problem updateProblem(@RequestBody Problem
	 * problem) { return problemService.updateProblem(problem); }
	 */
	
	/*
	 * @DeleteMapping("/problems/{id}") public void
	 * deleteProblem(@PathVariable("id") Long problemId) {
	 * System.out.println("ProblemController.deleteProblem(problemId) "+problemId);
	 * problemService.deleteProblem(problemId); }
	 */
	
	@DeleteMapping("/problems/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable("id") Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }
	
	 //Get all problems by Equipment ID
    @GetMapping("/problems/equipment/{equipmentId}")
    public ResponseEntity<List<ProblemDao>> getProblemsByEquipmentId(@PathVariable Long equipmentId) {
        List<ProblemDao> problems = problemService.getProblemsByEquipmentId(equipmentId);
        return ResponseEntity.ok(problems);
    }
}
