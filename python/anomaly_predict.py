"""
Anomaly Detection Script - Uses Isolation Forest for outlier detection
Called from Java via subprocess
"""

import sys
import pickle
import numpy as np

def get_anomaly_score(resume_text):
    """Detects anomalies using Isolation Forest"""
    try:
        # Load vectorizer and model
        with open('python/models/vectorizer.pkl', 'rb') as f:
            vectorizer = pickle.load(f)
        
        with open('python/models/isolation_forest.pkl', 'rb') as f:
            model = pickle.load(f)
        
        # Vectorize input text
        X = vectorizer.transform([resume_text])
        
        # Get anomaly score
        anomaly_score = model.score_samples(X)[0]
        
        # Normalize to 0-1 range
        normalized_score = 1 / (1 + np.exp(-anomaly_score))
        
        return normalized_score
        
    except Exception as e:
        print(f"Error: {str(e)}", file=sys.stderr)
        return 0.5

if __name__ == "__main__":
    if len(sys.argv) > 1:
        resume_text = sys.argv[1]
        score = get_anomaly_score(resume_text)
        print(f"{score:.4f}")
    else:
        print("0.5")
