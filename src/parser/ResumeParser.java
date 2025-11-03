package parser;

import models.Resume;
import java.util.regex.*;
import java.io.*;
import java.util.*;

/**
 * ResumeParser - Extracts data from resume text
 * Implements regex-based parsing for common resume formats
 */
public class ResumeParser {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("\\b(?:\\+?1[-.]?)?\\$?([0-9]{3})\\$?[-.]?([0-9]{3})[-.]?([0-9]{4})\\b");

    
    private static final Pattern YEARS_PATTERN =
        Pattern.compile("(\\d+)\\s*(?:year|yr)");
    
    private static final Pattern DEGREE_PATTERN =
        Pattern.compile("\\b(?:b\\.?(?:tech|e|sc|a)|m\\.?(?:tech|e|sc|a)|phd|m\\.?phil|diploma)\\b", 
                       Pattern.CASE_INSENSITIVE);
    
    /**
     * Parses resume text and extracts information
     */
    public static Resume parseResume(String fileName, String content) {
        Resume resume = new Resume(fileName, content);
        
        System.out.println("[PARSER] Parsing resume: " + fileName);
        
        // Extract basic information
        extractEmails(resume);
        extractPhoneNumbers(resume);
        extractCompanies(resume);
        extractUniversities(resume);
        extractSkills(resume);
        extractYearsOfExperience(resume);
        extractDegrees(resume);
        
        System.out.println("[PARSER] Parsing complete");
        return resume;
    }
    
    /**
     * Extracts email addresses
     */
    private static void extractEmails(Resume resume) {
        Matcher matcher = EMAIL_PATTERN.matcher(resume.getContent());
        while (matcher.find()) {
            resume.addEmail(matcher.group());
        }
    }
    
    /**
     * Extracts phone numbers
     */
    private static void extractPhoneNumbers(Resume resume) {
        Matcher matcher = PHONE_PATTERN.matcher(resume.getContent());
        while (matcher.find()) {
            resume.addPhoneNumber(matcher.group());
        }
    }
    
    /**
     * Extracts companies from resume
     */
    private static void extractCompanies(Resume resume) {
        String content = resume.getContent();
        String[] lines = content.split("\n");
        
        // Known companies
        String[] commonCompanies = {
            "Google", "Microsoft", "Amazon", "Apple", "Facebook", "Meta",
            "Tesla", "Netflix", "Uber", "Airbnb", "LinkedIn", "IBM",
            "Oracle", "Salesforce", "Adobe", "Intel", "Cisco", "VMware",
            "TCS", "Infosys", "Wipro", "Cognizant", "Accenture", "Deloitte",
            "McKinsey", "BCG", "Goldman", "JPMorgan", "Morgan Stanley"
        };
        
        for (String company : commonCompanies) {
            if (content.toLowerCase().contains(company.toLowerCase())) {
                resume.addCompany(company);
            }
        }
    }
    
    /**
     * Extracts universities from resume
     */
    private static void extractUniversities(Resume resume) {
        String content = resume.getContent();
        
        String[] universities = {
            "Harvard", "Stanford", "MIT", "Berkeley", "Yale", "Princeton",
            "Oxford", "Cambridge", "Caltech", "CMU",
            "IIT Delhi", "IIT Bombay", "IIT Madras", "IIT Kharagpur",
            "Delhi University", "JIIT", "Delhi Technological University",
            "Tsinghua", "Peking", "Tokyo", "Singapore", "Seoul"
        };
        
        for (String university : universities) {
            if (content.toLowerCase().contains(university.toLowerCase())) {
                resume.addUniversity(university);
            }
        }
    }
    
    /**
     * Extracts skills from resume
     */
    private static void extractSkills(Resume resume) {
        String content = resume.getContent();
        
        String[] skills = {
            "Java", "Python", "C++", "C#", "JavaScript", "TypeScript",
            "SQL", "NoSQL", "MongoDB", "PostgreSQL", "MySQL",
            "React", "Angular", "Vue", "Node.js", "Express",
            "Spring", "Django", "Flask", "FastAPI",
            "Machine Learning", "TensorFlow", "PyTorch", "Keras",
            "AWS", "Azure", "Google Cloud", "Kubernetes", "Docker",
            "DevOps", "CI/CD", "Jenkins", "Git", "GitHub",
            "Data Analysis", "Tableau", "Power BI", "Excel",
            "AI", "NLP", "Computer Vision", "Deep Learning",
            "Agile", "Scrum", "JIRA", "Linux", "Unix"
        };
        
        for (String skill : skills) {
            if (content.toLowerCase().contains(skill.toLowerCase())) {
                resume.addSkill(skill);
            }
        }
    }
    
    /**
     * Extracts years of experience
     */
    private static void extractYearsOfExperience(Resume resume) {
        Matcher matcher = YEARS_PATTERN.matcher(resume.getContent());
        int maxYears = 0;
        
        while (matcher.find()) {
            try {
                int years = Integer.parseInt(matcher.group(1));
                maxYears = Math.max(maxYears, years);
            } catch (NumberFormatException e) {
                // Skip invalid numbers
            }
        }
        
        resume.setYearsOfExperience(maxYears);
    }
    
    /**
     * Extracts degree count
     */
    private static void extractDegrees(Resume resume) {
        Matcher matcher = DEGREE_PATTERN.matcher(resume.getContent());
        int degreeCount = 0;
        
        while (matcher.find()) {
            degreeCount++;
        }
        
        resume.setDegreeCount(degreeCount);
    }
    
    /**
     * Loads resume from file
     */
    public static String loadResumeFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
