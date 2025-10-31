package com.edcapplication.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edcapplication.dao.TestBedEntryDao;
import com.edcapplication.exception.BadRequestException;
import com.edcapplication.exception.MailSendException;
import com.edcapplication.exception.ResourceNotFoundException;
import com.edcapplication.model.Project;
import com.edcapplication.model.TestBed;
import com.edcapplication.model.TestBedEntry;
import com.edcapplication.model.TestBedEntryEmbeddedId;
import com.edcapplication.repository.ProjectRepository;
import com.edcapplication.repository.TestBedEntryRepository;
import com.edcapplication.repository.TestBedRepository;

@Service
public class TestBedEntryService {

    private final TestBedEntryRepository testBedEntryRepository;
    private final TestBedRepository testBedRepository;
    private final ProjectRepository projectRepository;
    
    @Autowired
    private MailService mailService;  //<-- Mail service

    public TestBedEntryService(TestBedEntryRepository testBedEntryRepository,
                               TestBedRepository testBedRepository,
                               ProjectRepository projectRepository) {
        this.testBedEntryRepository = testBedEntryRepository;
        this.testBedRepository = testBedRepository;
        this.projectRepository = projectRepository;
    }

    public List<TestBedEntry> getAllTestBedEntries() {
        return testBedEntryRepository.findAll();
    }

    public TestBedEntry getTestBedEntryById(TestBedEntryEmbeddedId id) {
        return testBedEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestBedEntry not found for id: " + id));
    }

    public TestBedEntry createTestBedEntry(TestBedEntryDao dao) {
    	if (dao.getTestbedId() == null || dao.getRaisedOn() == null || dao.getShift() == null) {
            throw new BadRequestException("Missing required fields: testbedId, raisedOn, or shift");
        }
        TestBed testBed = testBedRepository.findById(dao.getTestbedId())
                .orElseThrow(() -> new ResourceNotFoundException("TestBed not found"));
        
        Project project = null;
        if (dao.getProjectId() != null) {
            project = projectRepository.findById(dao.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dao.getProjectId()));
        }

        TestBedEntry entry = new TestBedEntry();
        entry.setId(new TestBedEntryEmbeddedId(dao.getTestbedId(), dao.getRaisedOn(), dao.getShift()));
        entry.setTime(dao.getTime());
        entry.setRaisedBy(dao.getRaisedBy());
        entry.setTestBedUser(dao.getTestBedUser());
        entry.setProject(project);
        entry.setPlannedHours(dao.getPlannedHours());
        entry.setUptimeHours(dao.getUptimeHours());
        entry.setUtilizationHours(dao.getUtilizationHours());
        entry.setValidationHours(dao.getValidationHours());
        entry.setTestDescription(dao.getTestDescription());
        entry.setTestDescriptionHours(dao.getTestDescriptionHours());
        entry.setSetUpRemarks(dao.getSetUpRemarks());
        entry.setSetUpHours(dao.getSetUpHours());
        entry.setWorkonEngineRemarks(dao.getWorkonEngineRemarks());
        entry.setWorkonEngineHours(dao.getWorkonEngineHours());
        entry.setBreakDownRemarks(dao.getBreakDownRemarks());
        entry.setBreakDownHours(dao.getBreakDownHours());
        entry.setNoManPowerRemarks(dao.getNoManPowerRemarks());
        entry.setNoManPowerHours(dao.getNoManPowerHours());
        entry.setPowerCutRemarks(dao.getPowerCutRemarks());
        entry.setPowerCutHours(dao.getPowerCutHours());
        entry.setAnyOtherRemarks(dao.getAnyOtherRemarks());
        entry.setAnyOtherHours(dao.getAnyOtherHours());
        entry.setTotalSum(dao.getTotalSum());
        entry.setCoummulativeDescription(dao.getCoummulativeDescription());

        //return testBedEntryRepository.save(entry);
        
        //Save entry
        TestBedEntry savedEntry = testBedEntryRepository.save(entry);

        //Send notification mail
        try {
            sendCreationMail(testBed, dao);
        } catch (Exception e) {
            throw new MailSendException("Failed to send TestBedEntry creation email", e);
        }

        return savedEntry;
    }

    public TestBedEntry updateTestBedEntry(TestBedEntryDao dao) {
    	if (dao.getTestbedId() == null || dao.getRaisedOn() == null || dao.getShift() == null) {
            throw new BadRequestException("Missing required identifiers for update");
        }
        TestBedEntryEmbeddedId id = new TestBedEntryEmbeddedId(dao.getTestbedId(), dao.getRaisedOn(), dao.getShift());
        TestBedEntry existing = testBedEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestBedEntry not found for update"));

        Project project = null;
        if (dao.getProjectId() != null) {
            project = projectRepository.findById(dao.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dao.getProjectId()));
        }

        existing.setTime(dao.getTime());
        existing.setRaisedBy(dao.getRaisedBy());
        existing.setTestBedUser(dao.getTestBedUser());
        existing.setProject(project);
        existing.setPlannedHours(dao.getPlannedHours());
        existing.setUptimeHours(dao.getUptimeHours());
        existing.setUtilizationHours(dao.getUtilizationHours());
        existing.setValidationHours(dao.getValidationHours());
        existing.setTestDescription(dao.getTestDescription());
        existing.setTestDescriptionHours(dao.getTestDescriptionHours());
        existing.setSetUpRemarks(dao.getSetUpRemarks());
        existing.setSetUpHours(dao.getSetUpHours());
        existing.setWorkonEngineRemarks(dao.getWorkonEngineRemarks());
        existing.setWorkonEngineHours(dao.getWorkonEngineHours());
        existing.setBreakDownRemarks(dao.getBreakDownRemarks());
        existing.setBreakDownHours(dao.getBreakDownHours());
        existing.setNoManPowerRemarks(dao.getNoManPowerRemarks());
        existing.setNoManPowerHours(dao.getNoManPowerHours());
        existing.setPowerCutRemarks(dao.getPowerCutRemarks());
        existing.setPowerCutHours(dao.getPowerCutHours());
        existing.setAnyOtherRemarks(dao.getAnyOtherRemarks());
        existing.setAnyOtherHours(dao.getAnyOtherHours());
        existing.setTotalSum(dao.getTotalSum());
        existing.setCoummulativeDescription(dao.getCoummulativeDescription());

        return testBedEntryRepository.save(existing);
    }

    public void deleteTestBedEntry(TestBedEntryEmbeddedId id) {
        if (!testBedEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("TestBed Entry not found with ID: " + id);
        }
        testBedEntryRepository.deleteById(id);
    }
    
    //Mail sender
    private void sendCreationMail(TestBed testBed, TestBedEntryDao dao) {
    	String subject = "New TestBed Entry Created";

    	String htmlBody = """
    	<html>
    	  <body style="font-family: Arial, sans-serif; color: #333; background-color: #f8f9fa; padding: 20px;">
    	    <div style="max-width: 700px; margin: auto; background: #fff; border-radius: 8px;
    	                box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding: 20px;">
    	      <h2 style="color: #1F618D; border-bottom: 2px solid #1F618D; padding-bottom: 6px;">
    	        New TestBed Entry Created
    	      </h2>

    	      <p style="font-size: 15px;">Hello Team,</p>
    	      <p style="font-size: 15px;">
    	        A new <b>TestBed entry</b> has been created. Please find the details below:
    	      </p>

    	      <table border="1" cellspacing="0" cellpadding="8"
    	             style="border-collapse: collapse; width: 100%; font-size: 14px;">
    	        <tr style="background-color: #f2f2f2;">
    	          <th align="left" style="width: 30%;">Field</th>
    	          <th align="left">Details</th>
    	        </tr>
    	        <tr><td><b>TestBed</b></td><td>%s</td></tr>
    	        <tr><td><b>Raised By</b></td><td>%s</td></tr>
    	        <tr><td><b>Shift</b></td><td>%s</td></tr>
    	        <tr><td><b>Date</b></td><td>%s</td></tr>
    	      </table>

    	      <p style="margin-top: 20px; font-size: 14px;">
    	        Please log in to the <b>EDC System</b> for more information.
    	      </p>

    	      <p style="font-size: 14px;">Regards,<br><b>EDC System</b></p>
    	    </div>
    	  </body>
    	</html>
    	""".formatted(
    	    testBed.getName(),
    	    dao.getRaisedBy(),
    	    dao.getShift(),
    	    dao.getRaisedOn()
    	);

      //BCC recipients
        String[] bcc = new String[] {
            "rkraghuvanshi@vecv.in",
            "askushwah2@VECV.IN"
        };
        if (dao.getTestBedUser() != null) {
			//mailService.sendMail(new String[]{dao.getTestBedUser()}, subject, body, bcc);
			//mailService.sendMailHTMLFile("idmadmin@VECV.IN",new String[]{dao.getTestBedUser()},subject,htmlBody,new String[]{},bcc);
			mailService.sendMailHTMLFile("idmadmin@VECV.IN",new String[]{"rkraghuvanshi@vecv.in"},subject,htmlBody,new String[]{},bcc);
		}
    }
    
    //Fetch list of entries by testbedId
    public List<TestBedEntry> getTestBedEntriesByTestbedIdAndDateRange(Long testbedId, LocalDate startDate, LocalDate endDate) {
        return testBedEntryRepository.findById_TestbedIdAndId_RaisedOnBetween(testbedId, startDate, endDate);
    }

    public List<TestBedEntry> getEntriesByShiftAndDateRange(String shift, LocalDate startDate, LocalDate endDate) {
        return testBedEntryRepository.findById_ShiftAndId_RaisedOnBetween(shift, startDate, endDate);
    }

    public List<TestBedEntry> getEntriesByDateRange(LocalDate startDate, LocalDate endDate) {
        return testBedEntryRepository.findById_RaisedOnBetween(startDate, endDate);
    }
}
