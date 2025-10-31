package com.edcapplication.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
		String htmlBody = """
				<html>
				  <body style="font-family: Arial, sans-serif; color: #333; background-color: #f8f9fa; padding: 20px;">
				    <div style="max-width: 700px; margin: auto; background: #fff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding: 20px;">
				      <h2 style="color: #2E86C1; border-bottom: 2px solid #2E86C1; padding-bottom: 6px;">New BDRR Entry Created</h2>
				      <p style="font-size: 15px;">Hello Team,</p>
				      <p style="font-size: 15px;">A new <b>BDRR entry</b> has been created with the following details:</p>

				      <table border="1" cellspacing="0" cellpadding="8" style="border-collapse: collapse; width: 100%; font-size: 14px;">
				        <tr style="background-color: #f2f2f2;">
				          <th align="left" style="width: 30%;">Field</th>
				          <th align="left">Details</th>
				        </tr>
				        <tr><td><b>BDRR No</b></td><td>%s</td></tr>
				        <tr><td><b>Status</b></td><td>%s</td></tr>
				        <tr><td><b>Test Bed</b></td><td>%s</td></tr>
				        <tr><td><b>Equipment</b></td><td>%s - %s</td></tr>
				        <tr><td><b>Problem</b></td><td>%s</td></tr>
				        <tr><td><b>Raised By</b></td><td>%s</td></tr>
				        <tr><td><b>Shift</b></td><td>%s</td></tr>
				        <tr><td><b>Raised On</b></td><td>%s</td></tr>
				        <tr><td><b>Breakdown Description</b></td><td>%s</td></tr>
				        <tr><td><b>Initial Analysis</b></td><td>%s</td></tr>
				      </table>

				      <p style="margin-top: 20px; font-size: 14px;">Please log in to the <b>BDRR System</b> for full details and updates.</p>

				      <p style="font-size: 14px;">Regards,<br><b>BDRR System</b></p>
				    </div>
				  </body>
				</html>
				""".formatted(
				    entry.getBdrrNumber(),
				    dao.getStatus(),
				    testBed.getName(),
				    equipment.getEquipmentName(),
				    subEquipment.getSubequipmentName(),
				    problem.getProblemName(),
				    dao.getRaisedBy(),
				    dao.getShift(),
				    dao.getRaisedOn(),
				    dao.getBreakDownDescription(),
				    dao.getInitialAnalysis()
				);
		/*List<String> toList = new ArrayList<>();
		if (dao.getRaisedBy() != null)
			toList.add(dao.getRaisedBy());
		if (dao.getAreaAttender() != null)
			toList.add(dao.getAreaAttender());*/
		
		/*String[] toList = new String[] {
				dao.getRaisedBy()+"@vecv.in",
				dao.getAreaAttender()+"@vecv.in"
	        };*/
		
		String[] toList = new String[] {
	            "rkraghuvanshi@vecv.in",
	            "askushwah2@VECV.IN"
	        };

		  //BCC recipients
        String[] bcc = new String[] {
            "rkraghuvanshi@vecv.in",
            "askushwah2@VECV.IN"
        };

		if (toList.length>0) {
			//mailService.sendMail(toList.toArray(new String[0]), subject, body, bcc);
        	mailService.sendMailHTMLFile("idmadmin@VECV.IN",toList,subject,htmlBody,new String[]{},bcc);
		}
	}
    
    private void sendClosingMail(BDRREntry entry, BDRREntryDao dao, TestBed testBed, Equipment equipment,
			SubEquipment subEquipment, Problem problem) {

		String subject = "BDRR Closed - #" + entry.getBdrrNumber();
		String htmlBody = """
				<html>
				  <body style="font-family: Arial, sans-serif; color: #333; background-color: #f8f9fa; padding: 20px;">
				    <div style="max-width: 700px; margin: auto; background: #fff; border-radius: 8px;
				                box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding: 20px;">
				      <h2 style="color: #117A65; border-bottom: 2px solid #117A65; padding-bottom: 6px;">
				        BDRR Entry Closed
				      </h2>

				      <p style="font-size: 15px;">Hello Team,</p>
				      <p style="font-size: 15px;">The following <b>BDRR entry</b> has been <b style='color:#117A65;'>CLOSED</b>:</p>

				      <table border="1" cellspacing="0" cellpadding="8"
				             style="border-collapse: collapse; width: 100%; font-size: 14px;">
				        <tr style="background-color: #f2f2f2;">
				          <th align="left" style="width: 30%;">Field</th>
				          <th align="left">Details</th>
				        </tr>
				        <tr><td><b>BDRR No</b></td><td>%s</td></tr>
				        <tr><td><b>Test Bed</b></td><td>%s</td></tr>
				        <tr><td><b>Equipment</b></td><td>%s - %s</td></tr>
				        <tr><td><b>Problem</b></td><td>%s</td></tr>
				        <tr><td><b>Root Cause</b></td><td>%s</td></tr>
				        <tr><td><b>Action Taken</b></td><td>%s</td></tr>
				        <tr><td><b>Closed By</b></td><td>%s</td></tr>
				        <tr><td><b>Closing Date</b></td><td>%s</td></tr>
				        <tr><td><b>Remarks</b></td><td>%s</td></tr>
				      </table>

				      <p style="margin-top: 20px; font-size: 14px;">
				        Thank you for your support.<br>
				        Please log in to the <b>BDRR System</b> for full closure details.
				      </p>

				      <p style="font-size: 14px;">Regards,<br><b>EDC Admin</b></p>
				    </div>
				  </body>
				</html>
				""".formatted(
				    entry.getBdrrNumber(),
				    testBed.getName(),
				    equipment.getEquipmentName(),
				    subEquipment.getSubequipmentName(),
				    problem.getProblemName(),
				    dao.getSolutionRootCause(),
				    dao.getSolutionActionTaken(),
				    dao.getSolutionBy(),
				    dao.getClosingDate(),
				    dao.getWorkDoneDescription()
				);

        /*String[] toList = new String[] {
				dao.getRaisedBy()+"@vecv.in",
				dao.getAreaAttender()+"@vecv.in"
	        };*/
        
        String[] toList = new String[] {
                "rkraghuvanshi@vecv.in",
                "askushwah2@VECV.IN"
            };
		  //BCC recipients
        String[] bcc = new String[] {
            "rkraghuvanshi@vecv.in",
            "askushwah2@VECV.IN"
        };

		if (toList.length>0) {
			//mailService.sendMail(toList.toArray(new String[0]), subject, body, bcc);
        	mailService.sendMailHTMLFile("idmadmin@VECV.IN",toList,subject,htmlBody,new String[]{},bcc);
		}
	}
    
	public List<BDRREntry> getBDRREntriesByDynamicFilters(String status, String raisedBy, String attender,
			Long testBedId, LocalDate startDate, LocalDate endDate) {

		boolean allPresent = (status != null && !status.isEmpty()) && (raisedBy != null && !raisedBy.isEmpty())
				&& (attender != null && !attender.isEmpty()) && (testBedId != null)
				&& (startDate != null && endDate != null);

		//If all parameters are provided → use direct method
		if (allPresent) {
			return bdrrEntryRepository.findByStatusAndRaisedByAndAttenderAndTestBed_IdAndRaisedOnBetween(status,
					raisedBy, attender, testBedId, startDate, endDate);
		}

		//Else → build dynamic specification
		Specification<BDRREntry> spec = Specification.where(null);

		if (status != null && !status.isEmpty()) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
		}

		if (raisedBy != null && !raisedBy.isEmpty()) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("raisedBy"), raisedBy));
		}

		if (attender != null && !attender.isEmpty()) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("attender"), attender));
		}

		if (testBedId != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("testBed").get("id"), testBedId));
		}

		if (startDate != null && endDate != null) {
			spec = spec.and((root, query, cb) -> cb.between(root.get("raisedOn"), startDate, endDate));
		} else if (startDate != null) {
			spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("raisedOn"), startDate));
		} else if (endDate != null) {
			spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("raisedOn"), endDate));
		}

		return bdrrEntryRepository.findAll(spec);
	}
}
