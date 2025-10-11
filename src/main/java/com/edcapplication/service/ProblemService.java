package com.edcapplication.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.edcapplication.dao.EquipmentDao;
import com.edcapplication.dao.ProblemDao;
import com.edcapplication.exception.BadRequestException;
import com.edcapplication.exception.ResourceNotFoundException;
import com.edcapplication.model.Equipment;
import com.edcapplication.model.Problem;
import com.edcapplication.repository.EquipmentRepository;
import com.edcapplication.repository.ProblemRepository;

@Service
public class ProblemService {

private ProblemRepository problemRepository;
private final EquipmentRepository equipmentRepository;
	
public ProblemService(ProblemRepository problemRepository, EquipmentRepository equipmentRepository) {
    this.problemRepository = problemRepository;
    this.equipmentRepository = equipmentRepository;
}
	
	/*
	 * public List<Problem> getProblems() { return (List<Problem>)
	 * problemRepository.findAll(); }
	 */
	
	public List<ProblemDao> getAllProblems() {
		 List<Problem> problems = (List<Problem>) problemRepository.findAll();
		 if (problems.isEmpty()) {
	            throw new ResourceNotFoundException("No problems found in the database");
	        }
	        return problems.stream()
	                .map(p -> new ProblemDao(p.getId(), p.getProblemName(),p.getEquipment().getId()))
	                .collect(Collectors.toList());
    }
	
	/*
	 * public Problem getProblemById(Long id) { if (id == null) { throw new
	 * BadRequestException("Problem ID cannot be null"); } Optional<Problem>
	 * ProblemResponse = problemRepository.findById(id); Problem Problem =
	 * ProblemResponse.get(); return Problem; }
	 */
	public Problem getProblemById(Long id) {
        if (id == null) {
            throw new BadRequestException("Problem ID cannot be null");
        }

        return problemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with ID: " + id));
    }
	
	/*
	 * public ProblemDao addProblem(Problem p) { Problem saved =
	 * problemRepository.save(p); return new ProblemDao(saved.getId(),
	 * saved.getProblemName(), saved.getEquipment().getId()); }
	 */
	public ProblemDao addProblem(ProblemDao dao) {
        if (dao == null) {
            throw new BadRequestException("Problem data cannot be null");
        }
        if (dao.getProblemName() == null || dao.getProblemName().trim().isEmpty()) {
            throw new BadRequestException("Problem name is required");
        }
        if (dao.getEquipmentId() == null) {
            throw new BadRequestException("Equipment ID is required for creating a Problem");
        }

        Equipment equipment = equipmentRepository.findById(dao.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + dao.getEquipmentId()));

        Problem problem = new Problem();
        problem.setProblemName(dao.getProblemName());
        problem.setEquipment(equipment);

        Problem saved = problemRepository.save(problem);
        return new ProblemDao(saved.getId(), saved.getProblemName(), equipment.getId());
    }

	/*
	 * public Problem updateProblem(Problem problem) { return
	 * problemRepository.save(problem); }
	 */
	
	public Problem updateProblem(Long id, Problem updatedProblem) {
        if (id == null) {
            throw new BadRequestException("Problem ID is required for update");
        }

        Problem existing = problemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with ID: " + id));

        if (updatedProblem.getProblemName() != null && !updatedProblem.getProblemName().trim().isEmpty()) {
            existing.setProblemName(updatedProblem.getProblemName());
        }

        if (updatedProblem.getEquipment() != null) {
            Long eqId = updatedProblem.getEquipment().getId();
            Equipment equipment = equipmentRepository.findById(eqId)
                    .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + eqId));
            existing.setEquipment(equipment);
        }

        return problemRepository.save(existing);
    }

	
	public void deleteProblem(Long id) {
        if (id == null) {
            throw new BadRequestException("Problem ID cannot be null");
        }

        if (!problemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Problem not found with ID: " + id);
        }

        problemRepository.deleteById(id);
    }
	
	//Get problems by Equipment ID
    public List<ProblemDao> getProblemsByEquipmentId(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + equipmentId));

        List<Problem> problems = problemRepository.findByEquipment(equipment);

        return problems.stream()
                .map(p -> new ProblemDao(p.getId(), p.getProblemName(), equipment.getId()))
                .collect(Collectors.toList());
    }
}
