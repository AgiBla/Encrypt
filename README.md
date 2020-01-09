# Encrypt

As the amount of data in the world has increased exponentially,
data security and protection emerged to be one of the most important
challenges of our time. Nowadays, credit card billings, medical transcriptions or 
invoices etc. are often sent to a person via mail or can be accessed 
using mobile applications. Then this highly sensitive data is stored
unencrypted somewhere on the phones storage.
 
Hence we present **Encypt** - An android application designed to
encrypt data on your android smartphone, safely storing it in a local
file and preventing unauthorized access to your sensitive files.


## How it works

When the user selects one file and a pin code to protect it, a random 16 Byte AES key (**key2**)
is generated and used to encrypt the file content. At the same time, by hashing the pin
code using MD5, a weaker key (**key1**) is generated which will be used to encrypt the more secure 
**key2** and the file name (used in decryption to check if the pin is correct).

All this information (Original file name, encrypted file name, encrypted key2 and encrypted content)
is stored in a file with extension **.crypt**. An entry in the app database is created to store the
file location, but it can also be accessed by manually finding the file in the file explorer.


## Feature Blocks

- With [Room](https://developer.android.com/topic/libraries/architecture/room)
this application uses a local database to store and read the list of encrypted files, 
so the user is able of quickly locating previously encrypted files.


- The application enables three different languages (English, Spanish and German) using Android resource
 localization & orientation localization as well as providing different 
 UI layouts for landscape and portrait modes for all views.
 
 
- The application uses the [krypto](https://github.com/rs3vans/krypto) API. A strong, two way 
    encryption wrapper library around the Java Cryptography Architecture 
    (JCA) API, making it easier to use from Kotlin
    
    
## Disclaimer

The author(s) of Encrypt make no claim of expert knowledge in cryptography and we 
are not responsible for any loss or leakage of data, sensitive or otherwise.

### [Link to releases](https://github.com/AgiBla/Encrypt/releases)
