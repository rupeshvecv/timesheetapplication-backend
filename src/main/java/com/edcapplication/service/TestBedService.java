package com.edcapplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.edcapplication.exception.BadRequestException;
import com.edcapplication.exception.ResourceNotFoundException;
import com.edcapplication.model.TestBed;
import com.edcapplication.repository.TestBedRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TestBedService {

private TestBedRepository testBedRepository;
	
	public TestBedService(TestBedRepository testBedRepository) {
		this.testBedRepository = testBedRepository;
	}
	
	public List<TestBed> getAllTestBeds() {
		//return (List<TestBed>) testBedRepository.findAll();
		
		List<TestBed> testBeds = (List<TestBed>)testBedRepository.findAll();
        if (testBeds.isEmpty()) {
            throw new ResourceNotFoundException("No TestBeds found in the system");
        }
        return testBeds;
		
		/*
		 * List<TestBed> testBeds = (List<TestBed>) testBedRepository.findAll();
		 * testBeds.forEach(tb -> tb.getBdrrEntries().size());
		 *return testBeds;
		 */
		
		//return testBedRepository.findAll();
	}
	
	/*public TestBed getTestBedById(Integer id) {
		Optional<TestBed> TestBedResponse =  testBedRepository.findById(id);
		TestBed TestBed = TestBedResponse.get();
		return TestBed;
	}*/
	
	public TestBed getTestBedById(Long id) {
		 if (id == null) {
	            throw new BadRequestException("TestBed ID must not be null");
	        }
        return testBedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestBed not found with ID: " + id));
    }
	
	public TestBed addTestBed(TestBed testBed) {
		/*
		 * testBed.setId(0); if (testBed.getBdrrEntries() != null) {
		 * testBed.getBdrrEntries().forEach(entry -> entry.setTestBed(testBed)); }
		 * return testBedRepository.save(testBed);
		 */
		 if(testBed == null){
			 throw new BadRequestException("TestBed data cannot be null");
	     }
	     if(testBed.getName() == null || testBed.getName().trim().isEmpty()) {
	         throw new BadRequestException("TestBed name is required");
	     }
		return testBedRepository.save(testBed);
	}
	
	//Update an existing TestBed
    public TestBed updateTestBed(Long id, TestBed updatedTestBed) {
    	 if (id == null) {
             throw new BadRequestException("TestBed ID is required for update");
         }
         if (updatedTestBed == null) {
             throw new BadRequestException("Updated TestBed data cannot be null");
         }
        TestBed existing = testBedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestBed not found with ID: " + id));

        if (updatedTestBed.getName() != null && !updatedTestBed.getName().trim().isEmpty()) {
            existing.setName(updatedTestBed.getName());
        } else {
            throw new BadRequestException("TestBed name cannot be empty");
        }

		/*
		 * //Update bdrrEntries safely if (updatedTestBed.getBdrrEntries() != null) { //
		 * Remove old ones existing.getBdrrEntries().clear();
		 * 
		 * // Add new ones and link back updatedTestBed.getBdrrEntries().forEach(entry
		 * -> entry.setTestBed(existing));
		 * existing.getBdrrEntries().addAll(updatedTestBed.getBdrrEntries()); }
		 */

        return testBedRepository.save(existing);
    }
    
	public void deleteTestBed(Long id) {
		if (id == null) {
            throw new BadRequestException("TestBed ID is required for deletion");
        }

        if (!testBedRepository.existsById(id)) {
            throw new ResourceNotFoundException("TestBed not found with ID: " + id);
        }
        testBedRepository.deleteById(id);
    }
}
