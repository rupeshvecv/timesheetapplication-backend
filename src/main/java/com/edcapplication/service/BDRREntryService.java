package com.edcapplication.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edcapplication.dao.BDRREntryDao;
import com.edcapplication.exception.BadRequestException;
import com.edcapplication.exception.MailSendException;
import com.edcapplication.exception.ResourceNotFoundException;
import com.edcapplication.model.BDRREntry;
import com.edcapplication.model.Equipment;
import com.edcapplication.model.Problem;
import com.edcapplication.model.SubEquipment;
import com.edcapplication.model.TestBed;
import com.edcapplication.repository.BDRREntryRepository;
import com.edcapplication.repository.EquipmentRepository;
import com.edcapplication.repository.ProblemRepository;
import com.edcapplication.repository.SubEquipmentRepository;
import com.edcapplication.repository.TestBedRepository;

@Service
public class BDRREntryService {

    @Autowired
    private BDRREntryRepository bdrrEntryRepository;

    @Autowired
    private TestBedRepository testBedRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private SubEquipmentRepository subEquipmentRepository;

    @Autowired
    private ProblemRepository problemRepository;
    
    @Autowired
    private MailService mailService; // <-- Injected mail service

    public List<BDRREntry> getBDRREntries() {
        return bdrrEntryRepository.findAll();
    }

    public BDRREntry getBDRREntryById(Long id) {
        return bdrrEntryRepository.findById(id)
                //.orElseThrow(() -> new RuntimeException("BDRR Entry not found with ID: " + id));
        		.orElseThrow(() -> new ResourceNotFoundException("BDRR Entry not found with ID: " + id));
    }

    public BDRREntry createBDRREntry(BDRREntryDao dao) {
    	
    	if (dao.getTestbedId() == null || dao.getEquipmentId() == null) {
            throw new BadRequestException("Missing required fields in request");
        }
    	
        TestBed testBed = testBedRepository.findById(dao.getTestbedId())
                //.orElseThrow(() -> new RuntimeException("TestBed not found"));
        		.orElseThrow(() -> new ResourceNotFoundException("TestBed not found"));
        Equipment equipment = equipmentRepository.findById(dao.getEquipmentId())
                //.orElseThrow(() -> new RuntimeException("Equipment not found"));
        		.orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
        SubEquipment subEquipment = subEquipmentRepository.findById(dao.getSubEquipmentId())
                //.orElseThrow(() -> new RuntimeException("SubEquipment not found"));
        		.orElseThrow(() -> new ResourceNotFoundException("SubEquipment not found"));
        Problem problem = problemRepository.findById(dao.getProblemId())
                //.orElseThrow(() -> new RuntimeException("Problem not found"));
        		.orElseThrow(() -> new ResourceNotFoundException("Problem not found"));
        
        //Step 1: Generate dynamic BDRR number
        //long count = bdrrEntryRepository.count(); // total entries in the table
        //String generatedBdrrNumber = "BDRR-" + (count + 1);
        Long maxId = bdrrEntryRepository.findMaxId().orElse(0L);
        String generatedBdrrNumber = "BDRR-" + (maxId + 1);

        BDRREntry entry = new BDRREntry();
        //entry.setBdrrNumber(dao.getBdrrNumber());
        entry.setBdrrNumber(generatedBdrrNumber); // ✅ dynamic value
        
        entry.setStatus(dao.getStatus());
        entry.setRaisedOn(dao.getRaisedOn());
        entry.setRaisedBy(dao.getRaisedBy());
        entry.setShift(dao.getShift());
        entry.setTestBed(testBed);
        entry.setEquipment(equipment);
        entry.setSubEquipment(subEquipment);
        entry.setProblem(problem);
        entry.setTestAffected(dao.getTestAffected());
        entry.setAlternateArrangement(dao.getAlternateArrangement());
        entry.setSuggestion(dao.getSuggestion());
        entry.setAttender(dao.getAttender());
        entry.setSolutionRootCause(dao.getSolutionRootCause());
        entry.setSolutionActionTaken(dao.getSolutionActionTaken());
        entry.setSolutionBy(dao.getSolutionBy());
        entry.setSolutionGivenOn(dao.getSolutionGivenOn());
        entry.setBdrrOfDate(dao.getBdrrOfDate());
        entry.setAreaAttender(dao.getAreaAttender());
        entry.setTargetDate(dao.getTargetDate());
        entry.setClosingDate(dao.getClosingDate());
        entry.setPartUsed(dao.getPartUsed());
        entry.setPartNumber(dao.getPartNumber());
        entry.setPartDescriptions(dao.getPartDescriptions());
        entry.setQuantity(dao.getQuantity());
        entry.setBreakDownDescription(dao.getBreakDownDescription());
        entry.setInitialAnalysis(dao.getInitialAnalysis());
        entry.setWorkDoneDescription(dao.getWorkDoneDescription());

        //return bdrrEntryRepository.save(entry);
        //Save entry
        BDRREntry savedEntry = bdrrEntryRepository.save(entry);
        
        try {
            sendCreationMail(savedEntry, dao, testBed, equipment, subEquipment, problem);
        } catch (Exception e) {
            throw new MailSendException("Failed to send creation email", e);
        }

        return savedEntry;
    }
    
    public BDRREntry updateBDRREntry(Long id, BDRREntryDao dao) {
    	
    	if (dao.getTestbedId() == null || dao.getEquipmentId() == null || dao.getProblemId() == null) {
            throw new BadRequestException("Missing required IDs for TestBed, Equipment, or Problem");
        }
    	
    	BDRREntry existing = getBDRREntryById(id);

        TestBed testBed = testBedRepository.findById(dao.getTestbedId())
                .orElseThrow(() -> new ResourceNotFoundException("TestBed not found"));
        Equipment equipment = equipmentRepository.findById(dao.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
        SubEquipment subEquipment = subEquipmentRepository.findById(dao.getSubEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("SubEquipment not found"));
        Problem problem = problemRepository.findById(dao.getProblemId())
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found"));

        existing.setBdrrNumber(dao.getBdrrNumber());
        existing.setStatus(dao.getStatus());
        existing.setRaisedOn(dao.getRaisedOn());
        existing.setRaisedBy(dao.getRaisedBy());
        existing.setShift(dao.getShift());
        existing.setTestBed(testBed);
        existing.setEquipment(equipment);
        existing.setSubEquipment(subEquipment);
        existing.setProblem(problem);
        existing.setTestAffected(dao.getTestAffected());
        existing.setAlternateArrangement(dao.getAlternateArrangement());
        existing.setSuggestion(dao.getSuggestion());
        existing.setAttender(dao.getAttender());
        existing.setSolutionRootCause(dao.getSolutionRootCause());
        existing.setSolutionActionTaken(dao.getSolutionActionTaken());
        existing.setSolutionBy(dao.getSolutionBy());
        existing.setSolutionGivenOn(dao.getSolutionGivenOn());
        existing.setBdrrOfDate(dao.getBdrrOfDate());
        existing.setAreaAttender(dao.getAreaAttender());
        existing.setTargetDate(dao.getTargetDate());
        existing.setClosingDate(dao.getClosingDate());
        existing.setPartUsed(dao.getPartUsed());
        existing.setPartNumber(dao.getPartNumber());
        existing.setPartDescriptions(dao.getPartDescriptions());
        existing.setQuantity(dao.getQuantity());
        existing.setBreakDownDescription(dao.getBreakDownDescription());
        existing.setInitialAnalysis(dao.getInitialAnalysis());
        existing.setWorkDoneDescription(dao.getWorkDoneDescription());

        //return bdrrEntryRepository.save(existing);
        
        //Save
        BDRREntry savedEntry = bdrrEntryRepository.save(existing);
        
        //Send mail if status is Closed or closing date set
        if ("Closed".equalsIgnoreCase(dao.getStatus()) || dao.getClosingDate() != null) {
            try {
                sendClosingMail(savedEntry, dao, testBed, equipment, subEquipment, problem);
            } catch (Exception e) {
                throw new MailSendException("Failed to send closing email", e);
            }
        }
        return savedEntry;
    }

    public void deleteBDRREntry(Long id) {
        if (!bdrrEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("BDRR Entry not found with ID: " + id);
        }
        bdrrEntryRepository.deleteById(id);
    }
    
    private void sendCreationMail(BDRREntry entry, BDRREntryDao dao, TestBed testBed, Equipment equipment,
			SubEquipment subEquipment, Problem problem) {
		String subject = "New BDRR Entry Created - #" + entry.getBdrrNumber();
		String body = """
				Hello Team,
				A new BDRR entry has been created:
				BDRR No: %s
				Status: %s
				TestBed: %s
				Equipment: %s - %s
				Problem: %s
				Raised By: %s (Shift: %s)
				Raised On: %s

				Breakdown Description: %s
				Initial Analysis: %s

				Please log in for full details.
				Regards,
				BDRR System
				""".formatted(entry.getBdrrNumber(), dao.getStatus(), testBed.getName(), equipment.getEquipmentName(),
				subEquipment.getSubequipmentName(), problem.getProblemName(), dao.getRaisedBy(), dao.getShift(),
				dao.getRaisedOn(), dao.getBreakDownDescription(), dao.getInitialAnalysis());

		List<String> toList = new ArrayList<>();
		if (dao.getRaisedBy() != null)
			toList.add(dao.getRaisedBy());
		if (dao.getAreaAttender() != null)
			toList.add(dao.getAreaAttender());
		
		  //BCC recipients
        String[] bcc = new String[] {
            "rkraghuvanshi@vecv.in",
            "askushwah2@VECV.IN"
        };

		if (!toList.isEmpty()) {
			mailService.sendMail(toList.toArray(new String[0]), subject, body, bcc);
		}
	}
    
    private void sendClosingMail(BDRREntry entry, BDRREntryDao dao, TestBed testBed, Equipment equipment,
			SubEquipment subEquipment, Problem problem) {

		String subject = "BDRR Closed - #" + entry.getBdrrNumber();
		String body = """
				Hello Team,
				The following BDRR entry has been CLOSED:
				BDRR No: %s
				TestBed: %s
				Equipment: %s - %s
				Problem: %s
				Root Cause: %s
				Action Taken: %s
				Closed By: %s
				Closing Date: %s
				Remarks: %s

				Regards,
				BDRR System
				""".formatted(entry.getBdrrNumber(), testBed.getName(), equipment.getEquipmentName(),
				subEquipment.getSubequipmentName(), problem.getProblemName(), dao.getSolutionRootCause(),
				dao.getSolutionActionTaken(), dao.getSolutionBy(), dao.getClosingDate(), dao.getWorkDoneDescription());

		List<String> toList = new ArrayList<>();
		if (dao.getRaisedBy() != null)
			toList.add(dao.getRaisedBy());
		if (dao.getAreaAttender() != null)
			toList.add(dao.getAreaAttender());

		//BCC recipients
        String[] bcc = new String[] {
            "rkraghuvanshi@vecv.in",
            "askushwah2@VECV.IN"
        };

		if (!toList.isEmpty()) {
			mailService.sendMail(toList.toArray(new String[0]), subject, body, bcc);
		}
	}
}
