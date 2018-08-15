# Android Coding Challenge
We want to know how you write **code** and build the application architecture, we want to know how you can use the existing libraries to solve the problems that we have to solve.

Your task is to create a very small Android application (written in Kotlin) which will give the user possibility to take a photo and will tell them what is on the photo. It's up to you to decide how exactly you want to approach this challenge - do you want to use the native image recognition or another online service with vision capabilities (e.g. Google Vision, Microsoft Computer Vision, etc.).

## What we'll look at
- Structure of the code - how you use activities, services, views. Keep it clean and reusable.
- Code formatting, included unit and Espresso tests.
- Using external APIs is cool, but you have to make sure the app will support errors if the API is down.
- Readme - we don't need documentation, but a small file explaining how to run the project will be useful.
- Overall user experience in the application.
- The application should be deployable to the phone.

## Time limit
It should take you between 4 to 6 hours to finish this task. If it takes you longer, it's okay to leave TODOs in the code, just provide an explanation what you would still finish there.

After sending the challenge we'll wait for 3 days to hear back from you. Feel free to ask us for any clarification if you need it.

## Process
When you're ready, please fork this repository and start writing code in your fork. You'll get extra points for committing often in small chunks, so we'll see the process of how you created the application.

# Solution
The app is based on MVI architecture using RxJava. It gives the user the possibility to select image from gallery or take one with camera and the provides image caption from Microsoft Computer Vision API. It can be run from command line using gradle tasks or by Android Studio.

