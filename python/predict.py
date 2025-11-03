"""
Prediction Script - Uses trained ML model to predict fake resume probability
Called from Java via subprocess
"""

import sys
import pickle
import numpy as np
from io import StringIO

def predict_fake_score(resume_text):
    """Predicts fake resume score using trained model"""
    try:
        # Load vectorizer and model
        with open('python/models/vectorizer.pkl', 'rb') as f:
            vectorizer = pickle.load(f)
        
        with open('python/models/lr_classifier.pkl', 'rb') as f:
            model = pickle.load(f)
        
        # Vectorize input text
        X = vectorizer.transform([resume_text])
        
        # Get prediction probability
        prediction = model.predict_proba(X)[0]
        fake_score = prediction[1]  # Probability of being fake
        
        return fake_score
        
    except Exception as e:
        print(f"Error: {str(e)}", file=sys.stderr)
        return 0.5

if __name__ == "__main__":
    if len(sys.argv) > 1:
        resume_text = sys.argv[1]
        score = predict_fake_score(resume_text)
        print(f"{score:.4f}")
    else:
        print("0.5")
