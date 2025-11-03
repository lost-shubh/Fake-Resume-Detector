"""
Fake Resume Detector - ML Model Training Script
Trains models using TF-IDF + Logistic Regression
Saves trained models as pickle files for Java integration
"""

import pickle
import json
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import IsolationForest
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score
import os

# Create models directory if not exists
os.makedirs('models', exist_ok=True)

# ================== SAMPLE DATA ==================
REAL_RESUMES = [
    """
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
    """,
    
    """
    Alice Johnson
    Email: alice@company.com | Phone: +1-555-0102
    
    EDUCATION:
    Bachelor of Science, Stanford University (2019)
    
    EXPERIENCE:
    Data Scientist, Amazon (2021 - Present, 3 years)
    Data Analyst, Deloitte (2019 - 2021, 2 years)
    
    SKILLS: Python, SQL, Data Analysis, Machine Learning, Tableau
    """,
    
    """
    Bob Wilson
    Email: bob.wilson@email.com | Phone: +1-555-0103
    
    EDUCATION:
    B.Tech in Information Technology, Delhi University (2017)
    
    EXPERIENCE:
    Full Stack Developer, Flipkart (2020 - Present, 4 years)
    Frontend Developer, Accenture (2017 - 2020, 3 years)
    
    SKILLS: JavaScript, React, Node.js, MongoDB, Express
    """,
    
    """
    Sarah Davis
    Email: sarah.davis@email.com | Phone: +1-555-0104
    
    EDUCATION:
    Master of Science, MIT (2018)
    Bachelor of Engineering, Berkeley (2016)
    
    EXPERIENCE:
    Solutions Architect, IBM (2019 - Present, 5 years)
    Systems Engineer, Cisco (2018 - 2019, 1 year)
    
    SKILLS: Cloud Architecture, AWS, Docker, System Design
    """,
    
    """
    Michael Brown
    Email: michael@tech.com | Phone: +1-555-0105
    
    EDUCATION:
    B.Tech, IIT Kharagpur (2020)
    
    EXPERIENCE:
    DevOps Engineer, Uber (2021 - Present, 3 years)
    Operations Engineer, Zoho (2020 - 2021, 1 year)
    
    SKILLS: DevOps, CI/CD, Jenkins, Docker, Kubernetes
    """
]

FAKE_RESUMES = [
    """
    Super Developer X
    Email: superhacker@fake.com | Phone: 999-999-9999
    
    EDUCATION:
    PhD in Everything from Fake University (2015)
    Master's in Unicorn Startup Management (2013)
    
    EXPERIENCE:
    Expert in Telekinesis Programming, Time Travel Corp (50 years)
    Guru Ninja 10x Developer, Imaginary Company (100 years)
    
    SKILLS: Time Travel, Telepathy Programming, Mind Reading, Expert in Everything,
    Knows All Programming Languages, Telekinetic Debugging, Artificial General Intelligence
    """,
    
    """
    Fake Resume
    Email: lorem@ipsum.com
    
    EDUCATION:
    Lorem Ipsum University (2020)
    
    EXPERIENCE:
    Lorem Ipsum Company (15 years)
    Dummy Text Industries (10 years)
    
    SKILLS: Placeholder Skills, Filler Expertise, Dummy Competencies
    """,
    
    """
    Expert in Everything
    Phone: 000-0000
    
    EDUCATION:
    Started at age 5 with PhD
    
    EXPERIENCE:
    Working since age 8 with 60 years of experience
    
    SKILLS: Rockstar Engineer, Expert in Quantum Computing, Consciousness Transfer
    """,
    
    """
    John Fake
    Contact: unknown@unknown.com
    
    EDUCATION:
    Unknown University (2025)
    
    EXPERIENCE:
    Worked 200% time at multiple companies
    
    SKILLS: X-ray Vision, Mind Control Programming, Unicorn Startup Creator
    """,
    
    """
    Bot McSpammer
    
    EDUCATION:
    Dummy University (2030)
    
    EXPERIENCE:
    Experience since age 3
    Worked 150 years in tech
    
    SKILLS: Telekinesis, Time Management 200%, Artificial General Intelligence
    """
]

# ================== TRAINING FUNCTION ==================
def train_models():
    print("=" * 60)
    print("FAKE RESUME DETECTOR - ML MODEL TRAINING")
    print("=" * 60)
    
    # Prepare training data
    X_train = REAL_RESUMES + FAKE_RESUMES
    y_train = [0] * len(REAL_RESUMES) + [1] * len(FAKE_RESUMES)  # 0 = Real, 1 = Fake
    
    print(f"\nTraining data prepared:")
    print(f"  Real resumes: {len(REAL_RESUMES)}")
    print(f"  Fake resumes: {len(FAKE_RESUMES)}")
    print(f"  Total samples: {len(X_train)}\n")
    
    # ========== 1. Train TF-IDF Vectorizer ==========
    print("Training TF-IDF Vectorizer...")
    vectorizer = TfidfVectorizer(
        max_features=100,
        lowercase=True,
        stop_words='english',
        ngram_range=(1, 2)
    )
    X_tfidf = vectorizer.fit_transform(X_train)
    print(f"  Vocabulary size: {len(vectorizer.get_feature_names_out())}")
    print("  TF-IDF Vectorizer trained successfully!\n")
    
    # Save vectorizer
    with open('models/vectorizer.pkl', 'wb') as f:
        pickle.dump(vectorizer, f)
    print("  Saved: models/vectorizer.pkl")
    
    # ========== 2. Train Logistic Regression ==========
    print("\nTraining Logistic Regression Classifier...")
    lr_model = LogisticRegression(max_iter=200, random_state=42)
    lr_model.fit(X_tfidf, y_train)
    
    # Predictions on training data
    lr_pred = lr_model.predict(X_tfidf)
    lr_accuracy = accuracy_score(y_train, lr_pred)
    print(f"  Accuracy: {lr_accuracy:.4f}")
    
    # Save model
    with open('models/lr_classifier.pkl', 'wb') as f:
        pickle.dump(lr_model, f)
    print("  Saved: models/lr_classifier.pkl")
    
    # ========== 3. Train Isolation Forest (Anomaly Detection) ==========
    print("\nTraining Isolation Forest (Anomaly Detection)...")
    iso_forest = IsolationForest(contamination=0.3, random_state=42)
    iso_forest.fit(X_tfidf)
    
    iso_pred = iso_forest.predict(X_tfidf)
    iso_accuracy = accuracy_score(y_train, [1 if x == -1 else 0 for x in iso_pred])
    print(f"  Anomaly Detection Accuracy: {iso_accuracy:.4f}")
    
    # Save model
    with open('models/isolation_forest.pkl', 'wb') as f:
        pickle.dump(iso_forest, f)
    print("  Saved: models/isolation_forest.pkl")
    
    # ========== 4. Save Training Metadata ==========
    metadata = {
        "vectorizer_features": len(vectorizer.get_feature_names_out()),
        "real_samples": len(REAL_RESUMES),
        "fake_samples": len(FAKE_RESUMES),
        "lr_accuracy": float(lr_accuracy),
        "iso_accuracy": float(iso_accuracy),
        "training_date": str(np.datetime64('today'))
    }
    
    with open('models/metadata.json', 'w') as f:
        json.dump(metadata, f, indent=2)
    print("\n  Saved: models/metadata.json")
    
    print("\n" + "=" * 60)
    print("TRAINING COMPLETE!")
    print("=" * 60)
    print("\nModels saved in 'models/' directory:")
    print("  1. vectorizer.pkl - TF-IDF vectorizer")
    print("  2. lr_classifier.pkl - Logistic Regression model")
    print("  3. isolation_forest.pkl - Anomaly detection model")
    print("  4. metadata.json - Training metadata")
    print("\nYou can now use these models in Java!")
    print("=" * 60 + "\n")

if __name__ == "__main__":
    train_models()
