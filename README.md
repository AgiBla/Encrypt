# FileEncryption

As the amount of data in the world has increased exponentially,
data security and protection emerged to be one of the most important
challenges of our time. Nowadays, credit card billings, medical transcriptions or 
invoices etc. are often sent to a person via mail or can be accessed 
using mobile applications. Then this highly sensitive data is stored
unencrypted somewhere on the phones storage.
 
Hence we present **FileEncytpion** - An android application designed to
encrypt data on your android smartphone, safely storing it in a local
database and preventing unauthorized access to your sensitive files.


## How it works

![Encryption process concept](https://raw.githubusercontent.com/Nusserle/FileEncryption/master/app/src/main/res/images/encryption_concept.jpg)

## Core Blocks

- The application features multiple screen views. 
<p align="center">
<!-- ![Main screen](https://raw.githubusercontent.com/Nusserle/FileEncryption/master/app/src/main/res/images/1_MainScreen.png) -->
<img src="https://raw.githubusercontent.com/Nusserle/FileEncryption/master/app/src/main/res/images/1_MainScreen.png" width="300" height="500" />
<img src="https://raw.githubusercontent.com/Nusserle/FileEncryption/master/app/src/main/res/images/2_Encrypt_portrait.png" width="300" height="500" />
<img src="https://raw.githubusercontent.com/Nusserle/FileEncryption/master/app/src/main/res/images/4_Decrypt.png" width="300" height="500" />
</p>
- The application features a setting screen where a user is able to
delete completely the database.
<p align="center">
<img src="https://raw.githubusercontent.com/Nusserle/FileEncryption/master/app/src/main/res/images/5_Settings.png" width="300" height="500" />
</p>
- The application takes care of permission management by asking the user
for permission of accessing the phones internal storage and furthermore
handles lifecycle management allowing the application to be safely paused
and  terminated as well as used in
portrait mode and landscape mode.
<p align="center">
<img src="https://raw.githubusercontent.com/Nusserle/FileEncryption/master/app/src/main/res/images/0_Permissions.png" width="300" height="500" />
<img src="https://raw.githubusercontent.com/Nusserle/FileEncryption/master/app/src/main/res/images/2_Encrypt landscape.png" width="500" height="300" />
</p>


## Feature Blocks

- With [Room](https://developer.android.com/topic/libraries/architecture/room)
this application uses a local database to store and read the encrypted files


- The application enables three different languages using Android resource
 localization a& orientation localization as well as providing different 
 UI layouts for landscape and portrait modes for all views.
 
 
- FileEncryption uses two APIs
    - [OkHttp](https://square.github.io/okhttp/) - A reliable OpenSource
    HTTP client for HTTP request
    - [krypto](https://github.com/rs3vans/krypto) - A strong, two way 
    encryption wrapper library around the Java Cryptography Architecture 
    (JCA) API, making it easier to use from Kotlin
    
    
## Development Process

- The work was distributed among the team members considering the strengths of each of them
 to make sure everyone felt comfortable with their part. Tobias implemented the database and
 worked on the final report . On the other hand, Tasio implemented the file encryption and
 the Random.org request . Meanwhile Jon Ander worked on the app translation and the UI
 alongside Tasio.

- The initial idea we had in the presentation didn't change during the development of the project
 and we feel like the end result is a good reflection of the idea we exposed three weeks ago.
 We had to make some small changes because of some limitations of the programming language
 but we don't feel like any of these changes modified the user experience we had in mind.

- Which features would you implement, what would you do differently if 
you were to continue this project with more time?

 Supposing we want to release this app to the public there're a few changes we would make.

 First, we would not use Random.org for generating random numbers. There're java functions that 
 achieve almost as good randomness as Random.org that while working completely offline. Using this 
 method would allow the user to use the app without an active internet connection, not raising privacy 
 concerns about what data is the app sending or receiving during the encryption process.

 Another change would be to completely remove the database from our app, and instead, use a proprietary 
 file format where we can store all the data we're now storing in the database. This could allow users 
 to move encrypted files to other folders, change names, or even move them between devices without losing 
 access to their files.

 When talking about security, we realized that the salt used gives away the information of the length of 
 the PIN code used, and this could make an attacker easier to brute force. If we were to continue with this 
 project we would use a Salt with the same length for every file so no information about the PIN is public.
