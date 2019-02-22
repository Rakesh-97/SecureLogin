# SecureLogin

This Project is based on the two factor authentication for secure Login.
It have three parts:-
  - SecureLogin Android Application 
  - Backend_Scripts are scripts which are run on the server.
  - Web Sites are set of Web pages which are used as Service Provider site where we login using SecureLogin App.

Here I use Image stagnography for secure the login Procedure. Firstly every user register herself on the Andorid app where Unique Username, Password and PasswordImage are required. Hash of these informaiton will gone to server where script are running which is given an hash 512bit mobile token and we will store token in the app. Here Server store only the hash of Username in database.

After that when we want to login on the Service Provider site then just enter the Username and site is waiting for your mobile authentication, when you succesfully authenticate then site is redirected to the service page.

For authentication, user enter the Password and PasswordImage on the mobile app and hash of those information is compared to the 512bit mobile token if it is matched then you are succesfully authenticate.
Project Screenshots:
![Screenshot Image](https://github.com/Spider34/SecureLogin/blob/master/Screenshot_1542820641.png)
![Screenshot Image](https://github.com/Spider34/SecureLogin/blob/master/Screenshot_1542820951.png)
![Screenshot Image](https://github.com/Spider34/SecureLogin/blob/master/Screenshot_1542820966.png)
![Screenshot Image](https://github.com/Spider34/SecureLogin/blob/master/Screenshot_1542820681.png)
![Screenshot Image](https://github.com/Spider34/SecureLogin/blob/master/Screenshot_1542820686.png)
![Screenshot Image](https://github.com/Spider34/SecureLogin/blob/master/Screenshot_1542820694.png)
