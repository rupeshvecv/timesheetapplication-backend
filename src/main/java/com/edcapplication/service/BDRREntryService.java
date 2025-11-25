package com.edcapplication.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.edcapplication.dao.BDRREntryDao;
import com.edcapplication.exception.BadRequestException;
import com.edcapplication.exception.MailSendException;
import com.edcapplication.exception.ResourceNotFoundException;
import com.edcapplication.model.BDRREntry;
import com.edcapplication.model.BDRREntryAudit;
import com.edcapplication.model.Equipment;
import com.edcapplication.model.Problem;
import com.edcapplication.model.SubEquipment;
import com.edcapplication.model.TestBed;
import com.edcapplication.model.TestBedEntryAudit;
import com.edcapplication.projection.BDRREntryProjection;
import com.edcapplication.repository.BDRREntryAuditHistoryRepository;
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

    @Autowired
    private BDRREntryAuditHistoryRepository bdrrEntryAuditHistoryRepository;
    
    public List<BDRREntryProjection> getAllBDRREntriesProjected() {
        return bdrrEntryRepository.findAllBDRREntryProjected();
    }
    
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
        //Long count = bdrrEntryRepository.findMaxId().orElse(0L);
        
        LocalDateTime raisedOnDate = dao.getRaisedOn();

        if (raisedOnDate == null) {
            throw new BadRequestException("RaisedOn date is required to generate BDRR number");
        }

        //Count entries for this date
        //Long count = bdrrEntryRepository.countByRaisedOn(raisedOnDate);
        
        Long count = bdrrEntryRepository.countByRaisedOnMonthYear(raisedOnDate.getMonthValue(),raisedOnDate.getYear()
        );
        String generatedBdrrNumber = "BDRR-" + (count + 1);

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
            sendCreationBDRRMail(savedEntry, dao, testBed, equipment, subEquipment, problem);
        } catch (Exception e) {
            throw new MailSendException("Failed to send creation email", e);
        }

        return savedEntry;
    }
    
    public BDRREntry updateBDRREntry(Long id, BDRREntryDao dao) throws Exception {
    	
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
        
        String historyVal = "Status: "+dao.getStatus()+" :Attender: "+dao.getAttender()+" :SolutionBy: "+dao.getSolutionBy()+" :Problem: "+problem.getProblemName();
        saveTestBedAudit(dao.getBdrrNumber(),testBed.getName(), dao.getShift(),historyVal,"UPDATE");

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
            	sendClosingBDRRMail(savedEntry, dao, testBed, equipment, subEquipment, problem);
            } catch (Exception e) {
                throw new MailSendException("Failed to send closing email", e);
            }
        }
        return savedEntry;
    }

    public void deleteBDRREntry(Long id) throws Exception {
        if (!bdrrEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("BDRR Entry not found with ID: " + id);
        }
        
        BDRREntry existing = bdrrEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BDRR Entry not found with ID: " + id));
        
        TestBed testBed = testBedRepository.findById(existing.getTestBed().getId())
                .orElseThrow(() -> new ResourceNotFoundException("TestBed not found"));
        
        Problem problem = problemRepository.findById(existing.getProblem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found"));
        
        String historyVal = "Status: "+existing.getStatus()+" :Attender: "+existing.getAttender()+" :SolutionBy: "+existing.getSolutionBy()+" :Problem: "+problem.getProblemName();
        saveTestBedAudit(existing.getBdrrNumber(),testBed.getName(), existing.getShift(),historyVal,"DELETE");

        bdrrEntryRepository.deleteById(id);
    }
    
    private void sendCreationBDRRMail(BDRREntry entry, BDRREntryDao dao, TestBed testBed, Equipment equipment,SubEquipment subEquipment, Problem problem)
    {
    	String subject = "New BDRR Entry Created - #" + entry.getBdrrNumber();

    	String htmlBody =
    	    "<html>" +
    	    "<body style='font-family: Calibri, Arial, sans-serif; background-color:#f9f9f9; color:#333; padding:20px;'>" +
    	    "<div style='max-width:1000px; margin:auto; background:#fff; border-radius:8px; " +
    	    "box-shadow:0 2px 8px rgba(0,0,0,0.1); padding:20px;'>" +

    	    "<h2 style='color:#2E86C1; border-bottom:3px solid #2E86C1; padding-bottom:6px;'>New BDRR Entry Created</h2>" +

    	    "<p style='font-size:15px;'><b style='color:#C0392B;'>Dear Team,</b><br>" +
    	    "A new <b>BDRR entry</b> has been created in the <b>EDC System</b>. Please find the details below:</p>" +

    	    "<table border='1' cellspacing='0' cellpadding='8' " +
    	    "style='border-collapse:collapse;width:100%;font-size:14px;text-align:left;border:1px solid #ddd;'>" +
    	    "<thead>" +
    	    "<tr style='background-color:#34495E; color:white;'>" +
    	    "<th style='padding:8px;width:30%;'>Field</th>" +
    	    "<th style='padding:8px;'>Details</th>" +
    	    "</tr>" +
    	    "</thead>" +
    	    "<tbody>" +

    	    "<tr style='background-color:#f8f9fa;'><td><b>BDRR No</b></td><td>" + entry.getBdrrNumber() + "</td></tr>" +
    	    "<tr><td><b>Status</b></td><td>" + dao.getStatus() + "</td></tr>" +
    	    "<tr style='background-color:#f8f9fa;'><td><b>Test Bed</b></td><td>" + testBed.getName() + "</td></tr>" +
    	    "<tr><td><b>Equipment</b></td><td>" + equipment.getEquipmentName() + " - " + subEquipment.getSubequipmentName() + "</td></tr>" +
    	    "<tr style='background-color:#f8f9fa;'><td><b>Problem</b></td><td>" + problem.getProblemName() + "</td></tr>" +
    	    "<tr><td><b>Raised By</b></td><td>" + dao.getRaisedBy() + "</td></tr>" +
    	    "<tr style='background-color:#f8f9fa;'><td><b>Shift</b></td><td>" + dao.getShift() + "</td></tr>" +
    	    "<tr><td><b>Raised On</b></td><td>" + dao.getRaisedOn() + "</td></tr>" +

    	    "</tbody>" +
    	    "</table>" +

    	    "<p style='margin-top:20px;font-size:14px;'>Please log in to the <b>http://srpth1envapp01.vecvnet.com:8080/EmpowerEdge/login BDRR System</b> for full details and updates.</p>" +
    	    "<p style='font-size:14px;'>Regards,<br><b>EDC System</b></p>" +
    	    "</div>" +
    	    "</body>" +
    	    "</html>";
    	
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
    
    private void sendClosingBDRRMail(BDRREntry entry, BDRREntryDao dao, TestBed testBed, Equipment equipment, SubEquipment subEquipment, Problem problem) 
    {
		String subject = "BDRR Closed - #" + entry.getBdrrNumber();

		   // ----------------------------
	    // TIME DIFFERENCE CALCULATION
	    // ----------------------------
	    LocalDateTime raisedDate = dao.getRaisedOn();
	    LocalDateTime closedDate = dao.getClosingDate();

	    long totalMinutes = Duration.between(raisedDate, closedDate).toMinutes();
	    long hours = totalMinutes / 60;
	    long minutes = totalMinutes % 60;
	    
    	String htmlBody =
    	    "<html>" +
    	    "<body style='font-family: Calibri, Arial, sans-serif; background-color:#f9f9f9; color:#333; padding:20px;'>" +
    	    "<div style='max-width:1000px; margin:auto; background:#fff; border-radius:8px; " +
    	    "box-shadow:0 2px 8px rgba(0,0,0,0.1); padding:20px;'>" +

    	    "<h2 style='color:#2E86C1; border-bottom:3px solid #2E86C1; padding-bottom:6px;'>New BDRR Entry Closed</h2>" +

    	    "<p style='font-size:15px;'><b style='color:#C0392B;'>Dear Team,</b><br>" +
    	    "A new <b>BDRR entry</b> has been created in the <b>EDC System</b>. Please find the details below:</p>" +

    	    "<table border='1' cellspacing='0' cellpadding='8' " +
    	    "style='border-collapse:collapse;width:100%;font-size:14px;text-align:left;border:1px solid #ddd;'>" +
    	    "<thead>" +
    	    "<tr style='background-color:#34495E; color:white;'>" +
    	    "<th style='padding:8px;width:30%;'>Field</th>" +
    	    "<th style='padding:8px;'>Details</th>" +
    	    "</tr>" +
    	    "</thead>" +
    	    "<tbody>" +

    	    "<tr style='background-color:#f8f9fa;'><td><b>BDRR No</b></td><td>" + entry.getBdrrNumber() + "</td></tr>" +
    	    "<tr><td><b>Status</b></td><td>" + dao.getStatus() + "</td></tr>" +
    	    "<tr style='background-color:#f8f9fa;'><td><b>Test Bed</b></td><td>" + testBed.getName() + "</td></tr>" +
    	    "<tr><td><b>Equipment</b></td><td>" + equipment.getEquipmentName() + " - " + subEquipment.getSubequipmentName() + "</td></tr>" +
    	    "<tr style='background-color:#f8f9fa;'><td><b>Problem</b></td><td>" + problem.getProblemName() + "</td></tr>" +
    	    "<tr><td><b>Raised By</b></td><td>" + dao.getRaisedBy() + "</td></tr>" +
    	    "<tr style='background-color:#f8f9fa;'><td><b>Shift</b></td><td>" + dao.getShift() + "</td></tr>" +
    	    "<tr><td><b>Raised Date</b></td><td>" + dao.getRaisedOn() + "</td></tr>" +
    	    "<tr><td><b>Closing Date</b></td><td>" + dao.getClosingDate() + "</td></tr>" +
    	    "<tr><td><b>Time consumed in BDRR closure</b></td><td>" + hours +" HOURS -"+ minutes  + " MINUTES</td></tr>" +
    	    "<tr style='background-color:#f8f9fa;'><td><b>Breakdown Description</b></td><td>" + dao.getBreakDownDescription() + "</td></tr>" +
    	    "<tr><td><b>Initial Analysis</b></td><td>" + dao.getInitialAnalysis() + "</td></tr>" +
    	    "<tr><td><b>Work Done Description</b></td><td>" + dao.getWorkDoneDescription() + "</td></tr>" +
    	    "<tr><td><b>Root Cause</b></td><td>" + dao.getSolutionRootCause() + "</td></tr>" +
    	    "<tr><td><b>Action Taken</b></td><td>" + dao.getSolutionActionTaken() + "</td></tr>" +

    	    "</tbody>" +
    	    "</table>" +

    	    "<p style='margin-top:20px;font-size:14px;'>Please log in to the <b>http://srpth1envapp01.vecvnet.com:8080/EmpowerEdge/login BDRR System</b> for full details and updates.</p>" +
    	    "<p style='font-size:14px;'>Regards,<br><b>EDC System</b></p>" +
    	    "</div>" +
    	    "</body>" +
    	    "</html>";

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
			Long testBedId, LocalDateTime startDate, LocalDateTime endDate) {

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
	
	public List<BDRREntryProjection> getBDRREntriesByFilters(
	        String status,
	        String raisedBy,
	        String attender,
	        Long testbedId,
	        LocalDateTime startDate,
	        LocalDateTime endDate
	) {
	    return bdrrEntryRepository.findAllBDRREntryByFilters(
	            status,
	            raisedBy,
	            attender,
	            testbedId,
	            startDate,
	            endDate
	    );
	}

	private String getLoggedInUsername() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return "SYSTEM"; // fallback
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
	
	private void saveTestBedAudit(String bdrrNumber,String testBed, String shift,String historyVal,String operationType) throws Exception {
        BDRREntryAudit auditRecord = new BDRREntryAudit();
        String currentUser 				= getLoggedInUsername();// <--- FROM JWT
        
        auditRecord.setBdrrNumber(bdrrNumber);
        auditRecord.setTestBedName(testBed);
        auditRecord.setShift(shift);
        auditRecord.setFieldValues(historyVal);
        auditRecord.setOperationType(operationType);
        auditRecord.setChangedBy(currentUser);  // <--- store JWT USERNAME
        auditRecord.setChangedOn(LocalDateTime.now());

        bdrrEntryAuditHistoryRepository.save(auditRecord);
    }
}
