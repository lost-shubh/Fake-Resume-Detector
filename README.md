# Fake Resume Detector
## Java OOPs + Machine Learning Integration

A hybrid system that detects fraudulent resumes using both rule-based validation and ML models. Built entirely in Java with Python ML backend.

---

## Project Structure

\`\`\`
fake-resume-detector/
├── src/
│   ├── models/
│   │   ├── Resume.java           # Resume data model
│   │   └── ValidationResult.java  # Validation result model
│   ├── validators/
│   │   ├── BaseValidator.java       # Abstract base class
│   │   ├── KeywordValidator.java    # Suspicious keyword detection
│   │   ├── ExperienceValidator.java # Experience validation
│   │   ├── DegreeValidator.java     # Degree validation
│   │   └── RuleBasedValidator.java  # Orchestrates validators
│   ├── ml/
│   │   ├── MLPredictor.java       # ML model loader and predictor
│   │   └── TextPreprocessor.java  # Text preprocessing
│   ├── parser/
│   │   └── ResumeParser.java      # Resume parsing and extraction
│   ├── storage/
│   │   └── ResultDatabase.java    # Results storage
│   ├── ui/
│   │   └── CLIInterface.java      # Terminal interface
│   └── Main.java                   # Entry point
├── python/
│   ├── train_model.py             # Model training script
│   ├── predict.py                 # ML prediction
│   ├── anomaly_predict.py         # Anomaly detection
│   ├── requirements.txt           # Python dependencies
│   └── models/                    # Trained models (generated)
├── data/
│   └── results/                   # Detection results storage
└── README.md                       # This file
\`\`\`

---

## System Architecture

### Phase 1: Rule-Based Validation
- **KeywordValidator**: Detects suspicious keywords and fake universities
- **ExperienceValidator**: Validates years of experience for inconsistencies
- **DegreeValidator**: Checks educational qualifications
- **RuleBasedValidator**: Orchestrates all validators using Strategy pattern

### Phase 2: Machine Learning Detection
- **TF-IDF Vectorization**: Converts text to numerical features
- **Logistic Regression**: Classifies resumes as real/fake
- **Isolation Forest**: Detects anomalies in resume patterns
- **MLPredictor**: Loads models and makes predictions

### Phase 3: Hybrid Detection
- Combines rule-based and ML scores
- Final confidence score = (ML Score × 0.6) + (Rule Score × 0.4)

---

## Key Features

### OOPs Principles Demonstrated
1. **Encapsulation** - Private fields with public getters/setters
2. **Inheritance** - BaseValidator abstract class extended by specific validators
3. **Polymorphism** - Abstract validate() method implemented differently
4. **Abstraction** - Complex validation logic hidden behind clean interfaces
5. **Composition** - RuleBasedValidator uses multiple validators
6. **Strategy Pattern** - Different validation strategies used interchangeably

### Detection Capabilities
- Suspicious keyword detection (fake universities, impossible skills)
- Years of experience validation
- Educational qualification verification
- ML-based pattern recognition
- Anomaly detection
- Hybrid scoring system

---

## Prerequisites

### System Requirements
- **Java**: JDK 11 or higher
- **Python**: 3.8 or higher
- **Git** (optional, for cloning)

### Installation

#### 1. Install Java (if not already installed)

**Windows:**
\`\`\`bash
# Download from: https://www.oracle.com/java/technologies/downloads/
# Install and verify
java -version
javac -version
\`\`\`

**macOS:**
\`\`\`bash
brew install openjdk@11
\`\`\`

**Linux (Ubuntu):**
\`\`\`bash
sudo apt-get install openjdk-11-jdk
\`\`\`

#### 2. Install Python (if not already installed)

**Windows/macOS/Linux:**
Download from https://www.python.org/downloads/
During installation, CHECK: "Add Python to PATH"

Verify installation:
\`\`\`bash
python --version
pip --version
\`\`\`

---

## Setup Instructions

### Step 1: Download/Clone Project
\`\`\`bash
# If cloning
git clone <repository-url>
cd fake-resume-detector

# Or extract the downloaded ZIP file
\`\`\`

### Step 2: Install Python Dependencies
\`\`\`bash
cd python
pip install -r requirements.txt
cd ..
\`\`\`

This installs:
- scikit-learn (ML library)
- numpy (numerical computing)

### Step 3: Train ML Models
\`\`\`bash
cd python
python train_model.py
cd ..
\`\`\`

**Expected output:**
\`\`\`
============================================================
FAKE RESUME DETECTOR - ML MODEL TRAINING
============================================================
Training data prepared:
  Real resumes: 5
  Fake resumes: 5
  Total samples: 10

Training TF-IDF Vectorizer...
  Vocabulary size: 100
  TF-IDF Vectorizer trained successfully!
  Saved: models/vectorizer.pkl

Training Logistic Regression Classifier...
  Accuracy: 0.9000
  Saved: models/lr_classifier.pkl

Training Isolation Forest (Anomaly Detection)...
  Anomaly Detection Accuracy: 0.9000
  Saved: models/isolation_forest.pkl

  Saved: models/metadata.json

============================================================
TRAINING COMPLETE!
============================================================
\`\`\`

**Models generated:**
- `python/models/vectorizer.pkl` - TF-IDF vectorizer
- `python/models/lr_classifier.pkl` - Logistic Regression model
- `python/models/isolation_forest.pkl` - Isolation Forest model
- `python/models/metadata.json` - Training metadata

### Step 4: Compile Java Code
\`\`\`bash
# Compile all Java files
javac -d bin src/**/*.java src/*.java

# Or on Windows:
javac -d bin src\**\*.java src\*.java
\`\`\`

**If `bin` directory doesn't exist, create it first:**
\`\`\`bash
mkdir bin
javac -d bin src/**/*.java src/*.java
\`\`\`

### Step 5: Run Application
\`\`\`bash
java -cp bin Main
\`\`\`

**Expected output:**
\`\`\`
[STARTUP] Initializing Fake Resume Detector...

╔════════════════════════════════════════════════════════╗
║                                                        ║
║          FAKE RESUME DETECTOR                         ║
║     Using Java OOPs & Machine Learning                ║
║                                                        ║
╚════════════════════════════════════════════════════════╝


============================================================
MAIN MENU
============================================================
1. Analyze Resume (File)
2. Analyze Resume (Text Input)
3. View Detection History
4. View Statistics
5. Clear Database
6. Test with Sample Data
7. Exit
============================================================
Enter your choice (1-7):
\`\`\`

---

## Usage Guide

### Option 1: Analyze Resume from File
\`\`\`
1. Select option: 1
2. Enter file path: /path/to/resume.txt
3. Application displays detection results
\`\`\`

### Option 2: Analyze Resume from Text Input
\`\`\`
1. Select option: 2
2. Enter resume name or press Enter for auto-generated name
3. Paste resume text
4. Type "END" on new line to finish
5. Application displays detection results
\`\`\`

### Option 3: Test with Sample Data
\`\`\`
1. Select option: 6
2. Automatically analyzes:
   - Real resume sample
   - Fake resume sample
3. View results immediately
\`\`\`

### Sample Resume for Testing

**Real Resume:**
\`\`\`
John Smith
Email: john.smith@email.com | Phone: +1-555-0101

EDUCATION:
B.Tech in Computer Science, IIT Delhi (2018)
M.Tech in Artificial Intelligence, IIT Bombay (2020)

EXPERIENCE:
Senior Software Engineer, Google (2022 - Present, 2 years)
Software Engineer, Microsoft (2020 - 2022, 2 years)
Junior Developer, TCS (2018 - 2020, 2 years)

SKILLS: Python, Java, Machine Learning, TensorFlow, Kubernetes
\`\`\`

**Fake Resume:**
\`\`\`
Super Developer X
Email: superhacker@fake.com

EDUCATION:
PhD in Everything from Fake University (2015)

EXPERIENCE:
Expert in Telepathy Programming, Time Travel Corp (50 years)
Guru Ninja 10x Developer, Imaginary Company (100 years)

SKILLS: Time Travel, Telekinesis Programming, Expert in Everything
\`\`\`

---

## Output Explanation

### Validation Report Format
\`\`\`
============================================================
RESUME VALIDATION REPORT
============================================================
File: resume.txt
Date: Thu Dec 12 10:30:45 IST 2024
Validation Type: HYBRID
------------------------------------------------------------
STATUS: FAKE
Confidence Score: 85.50%
Rule-Based Score: 75.00%
ML-Based Score: 92.00%
------------------------------------------------------------
RED FLAGS DETECTED:
  • Suspicious keyword detected: 'telepathy programming'
  • Unrealistic years of experience: 50 (more than 70 years)
  
SUSPICIOUS KEYWORDS:
  • time travel
  • telepathy programming
============================================================
\`\`\`

### Score Interpretation
- **0.0 - 0.33**: Likely Genuine (Green)
- **0.33 - 0.66**: Uncertain (Yellow)
- **0.66 - 1.0**: Likely Fake (Red)

---

## Troubleshooting

### Problem: Python not found
\`\`\`
Error: 'python' is not recognized
\`\`\`
**Solution:** 
- Check Python installation
- Add Python to PATH
- Use `python3` instead on macOS/Linux

### Problem: Models not loading
\`\`\`
[WARNING] Models not found at: python/models
\`\`\`
**Solution:**
\`\`\`bash
cd python
python train_model.py
cd ..
\`\`\`

### Problem: Java compilation error
\`\`\`
error: directory not found: src/**/*.java
\`\`\`
**Solution:** On Windows, use:
\`\`\`bash
javac -d bin src\models\*.java src\validators\*.java src\ml\*.java src\parser\*.java src\storage\*.java src\ui\*.java src\Main.java
\`\`\`

### Problem: Permission denied on macOS/Linux
\`\`\`bash
chmod +x python/train_model.py
\`\`\`

---

## Project Statistics

### Detection Metrics
- **Training Samples**: 10 (5 real, 5 fake)
- **Vectorizer Vocabulary**: 100 features
- **Logistic Regression Accuracy**: 90%
- **Isolation Forest Accuracy**: 90%

### Detection Features
- **Keywords Checked**: 30+
- **Universities Database**: 20+
- **Known Companies**: 25+
- **Skills Database**: 45+

---

## Future Enhancements

1. **Advanced NLP**
   - Named Entity Recognition (NER)
   - Deep Learning models (LSTM, Transformers)
   - Semantic analysis

2. **Database Integration**
   - MySQL/SQLite for persistent storage
   - User authentication
   - Batch processing

3. **Web Interface**
   - REST API with Spring Boot
   - Web dashboard
   - Real-time monitoring

4. **Improved ML**
   - Larger training dataset
   - Ensemble methods
   - Cross-validation

---

## OOPs Design Patterns Used

1. **Strategy Pattern** - Different validation strategies
2. **Abstract Factory** - Validator creation
3. **Singleton** - Database and ML predictor
4. **Observer** - Result notifications
5. **Decorator** - Score calculation enhancements

---

## References

- Machine Learning: https://scikit-learn.org/
- Java OOPs: https://docs.oracle.com/javase/tutorial/
- NLP: https://www.nltk.org/
- Regex Patterns: https://regexone.com/

---

## Team Members
- Aakash Bansal (2401200001)
- Shubh Gupta (2401200014)

---

## License
This project is for educational purposes.

---

## Quick Start Cheatsheet

\`\`\`bash
# 1. Install dependencies
cd python && pip install -r requirements.txt && cd ..

# 2. Train models
cd python && python train_model.py && cd ..

# 3. Create bin directory
mkdir bin

# 4. Compile Java
javac -d bin src/**/*.java src/*.java

# 5. Run application
java -cp bin Main
\`\`\`

---

For support or questions, contact: 2401200001@mail.jiit.ac.in

# Fake-Resume-Detector