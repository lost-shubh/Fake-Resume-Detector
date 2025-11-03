package models;

import java.io.Serializable;
import java.util.*;

/**
 * Resume Model - Represents a resume with personal, professional, and educational data
 * Follows OOPs principles: Encapsulation, Single Responsibility
 */
public class Resume implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String fileName;
    private String content;
    private String candidateName;
    private List<String> emails;
    private List<String> phoneNumbers;
    private List<String> universities;
    private List<String> companies;
    private List<String> skills;
    private int yearsOfExperience;
    private int degreeCount;
    private Date uploadedDate;
    
    public Resume(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.emails = new ArrayList<>();
        this.phoneNumbers = new ArrayList<>();
        this.universities = new ArrayList<>();
        this.companies = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.uploadedDate = new Date();
    }
    
    // Getters and Setters with encapsulation
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
    
    public List<String> getEmails() { return new ArrayList<>(emails); }
    public void addEmail(String email) { this.emails.add(email); }
    
    public List<String> getPhoneNumbers() { return new ArrayList<>(phoneNumbers); }
    public void addPhoneNumber(String phone) { this.phoneNumbers.add(phone); }
    
    public List<String> getUniversities() { return new ArrayList<>(universities); }
    public void addUniversity(String uni) { this.universities.add(uni); }
    
    public List<String> getCompanies() { return new ArrayList<>(companies); }
    public void addCompany(String company) { this.companies.add(company); }
    
    public List<String> getSkills() { return new ArrayList<>(skills); }
    public void addSkill(String skill) { this.skills.add(skill); }
    
    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int years) { this.yearsOfExperience = years; }
    
    public int getDegreeCount() { return degreeCount; }
    public void setDegreeCount(int count) { this.degreeCount = count; }
    
    public Date getUploadedDate() { return uploadedDate; }
    
    @Override
    public String toString() {
        return "Resume{" +
                "fileName='" + fileName + '\'' +
                ", candidateName='" + candidateName + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", degreeCount=" + degreeCount +
                '}';
    }
}
