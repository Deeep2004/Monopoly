import pandas as pd

import numpy as np

from sklearn.model_selection import train_test_split

from sklearn.preprocessing import StandardScaler

from sklearn.utils import shuffle

import tensorflow as tf

from tensorflow.python.keras.models import Sequential

from tensorflow.python.keras.layers import Dense

 

# Load the dataset

df = pd.read_csv('america_bankruptcy.csv')

# Preprocess the data

df.drop(['company_name', 'year'], axis=1, inplace=True)

df['status_label'] = df['status_label'].apply(lambda x: 1 if x == 'alive' else 0)

df = df.rename(columns={'status_label': 'bankruptcy'})

 

# Separate features and target

x = df.drop('bankruptcy', axis=1)

y = df['bankruptcy']

 

# Split the data into training and test sets

X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=42)

 

# Standardize the features

scaler = StandardScaler()

X_train_scaled = scaler.fit_transform(X_train)

X_test_scaled = scaler.transform(X_test)

 

# Shuffle the training data

X_train_scaled, y_train = shuffle(X_train_scaled, y_train, random_state=42)

 

# Build the deep neural network model

model = Sequential([

    Dense(64, activation='relu', input_shape=(X_train_scaled.shape[1],)),

    Dense(32, activation='relu'),

    Dense(1, activation='sigmoid')

])

 

# Compile the model
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

 

# Train the model
model.fit(X_train_scaled, y_train, epochs=50, batch_size=32, validation_split=0.2)

 

# Evaluate the model on the test set

test_loss, test_accuracy = model.evaluate(X_test_scaled, y_test)
print(f"Test Accuracy: {test_accuracy}")

 

# Make predictions
y_pred = (model.predict(X_test_scaled) > 0.5).astype("int32")

 

# Print classification report

from sklearn.metrics import classification_report, confusion_matrix

print("Classification Report:")

print(classification_report(y_test, y_pred))

 

print("Confusion Matrix:")

print(confusion_matrix(y_test, y_pred))