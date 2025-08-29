# speech-analyzer
Speech analyzer is an application that allows users to practice thier speeches with the help of AI.

## Tech stack

frontend - react, typescript
backend - springboot, aws transcribe, Google Gemini API

## How it works ?

1. You start recording your audio 
2. your audio is converted into PCM-16 bit in real-time to make it suitable for aws transcribe.
3. aws transcribe generates real time audio-to-text conversion
4. once the audio input is stopped, the entire transcript is sent to gemini with a prompt to analyze the speech and provide suggestions, rating etc.

## Setup

1. In order to setup speech analyzer locally, clone the repository
2. setup aws access_id, secret_id using ``aws configure``
3. generate and setup ``GOOGLE_API_KEY`` as environment variable
4. run the react app ``npm i && npm run dev``
5. run the backend `` mvn clean package && cd target && java - jar <jar file>``

![alt text](https://github.com/huzaifa1221/speech-analyzer/blob/revamp/readme-images/home.png?raw=true)
