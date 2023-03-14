<h1 align="center">Practical Work on Computação Móvel e Ubíqua (Mobile and Ubiquitous Computing)</h1>

<p>
  <img src="http://img.shields.io/static/v1?style=for-the-badge&label=School%20year&message=2022/2023&color=informational"/>
  <img src="http://img.shields.io/static/v1?style=for-the-badge&label=Discipline&message=CMU&color=informational"/>
  <img src="http://img.shields.io/static/v1?style=for-the-badge&label=Grade&message=19&color=sucess"/>
  
  <a href="https://github.com/oliveira1712/TravelAssistant/blob/main/documentation/Utterance.pdf" target="_blank">
    <img src="https://img.shields.io/badge/-Utterance-grey?style=for-the-badge"/>
  </a>
  <a href="https://github.com/oliveira1712/TravelAssistant/blob/main/documentation/Report.pdf" target="_blank">
    <img src="https://img.shields.io/badge/-Report-grey?style=for-the-badge"/>
  </a>
</p>

<h2>Travel Assistant</h2>
This project is an
application capable of accompanying and satisfying the needs of a user during
his personal travels.
The approach followed for this project was to develop functionalities such as:

- Management of the user's personal vehicles, with the possibility to choose which vehicle
will be used.
- Monitor distances traveled and locations visited
- View nearest/cheapest filling stations
- View nearest points of interest with various filters available
- View frequent routes
- Calculate routes between points
- View directions when driving a route

## Additional Functionalities

- Dark and Light Theme
- Multilanguage Support
- Responsive Layout
- Background Services


<h2>Languages</h2>
<p align="left"> 
  <img src="https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&amp;logo=android-studio&amp;logoColor=white" alt="Android Studio">
  <img src="https://img.shields.io/static/v1?style=for-the-badge&amp;message=Jetpack+Compose&amp;color=4285F4&amp;logo=Jetpack+Compose&amp;logoColor=FFFFFF&amp;label=" alt="Jetpack Compose">
  <img src="https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&amp;logo=kotlin&amp;logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&amp;logo=Firebase&amp;logoColor=white" alt="Firebase">
</p>


<h2>Installation</h2>

1. Importing Database Folder Files to Firebase using Firestore:
In order to import the files from the Database folder to Firebase, the Firestore service must be used. This can be done by logging into the Firebase console, creating a new database, and then using the import option to select the files from the Database folder.

2. Exporting the google-services.json File from Firebase:
The google-services.json file is crucial for connecting the project to Firebase. To export this file, log into the Firebase console, go to the project settings, and select the "Download google-services.json" option. Then, add the file to the android_app\app folder.

3. Modifying the AndroidManifest.xml File:
Finally, the AndroidManifest.xml file needs to be modified in order to change the value of the android:value attribute to the appropriate key for the Google Maps API. This can be done by opening the file in a text editor, locating the relevant line of code, and changing the value to the key that was obtained from the Google Maps API Console.

```
<meta-data
  android:name="com.google.android.geo.API_KEY"
  android:value="key" 
/>
```

4. To run the project in Android Studio, open the project, click on the "Run" button, or select "Run" from the "Run" menu, and verify the output to ensure the changes made to the project have taken effect.

5. The project also contains UI tests and unit tests to verify the visual appearance and behavior of the app and the individual units of code, respectively, ensuring the quality and reliability of the application.


## Screenshots

### Map Screen
![MapScreen](https://github.com/oliveira1712/TravelAssistant/blob/main/images/Map_Screen.png)

---

### Points Of Interest Screen
![POIScreen](https://github.com/oliveira1712/TravelAssistant/blob/main/images/Points_Of_Interest_Screen.png)

---

### Gas Stations Screen | More Screen
<p>
  <img height="600" width="300" src="https://github.com/oliveira1712/TravelAssistant/blob/main/images/Gas_Stations_Screen.png"/>
  <img height="600" width="500" src="https://github.com/oliveira1712/TravelAssistant/blob/main/images/More_Screen.png"/>
</p>

<h2>Authors</h2>

<h3>
  Gonçalo Oliveira
  <a href="https://github.com/oliveira1712?tab=followers">
    <img src="https://img.shields.io/github/followers/oliveira1712.svg?style=social&label=Follow" />
  </a>
</h3>

<h3>
  Bruno Ferreira
  <a href="https://github.com/brunoferreira0106?tab=followers">
    <img src="https://img.shields.io/github/followers/brunoferreira0106.svg?style=social&label=Follow" />
  </a>
</h3>
