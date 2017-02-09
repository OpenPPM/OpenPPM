
Dear OpenPPM Users,


Thank you for choosing installing OpenPPM.


OpenPPM is an Open Source  product, used for Project Management and Project Portfolio Management, 
in accordance with the PMBOK® (Project Management Body of Knowledge) guide,from PMI® (Project Management Institute).



From a functional point of view: 


- We intended to create a solution for companies that need to manage a large amount of projects and need to have a single source to get an overview on what is happening.


- We tried to offer a solution that would be intuitive, simple to administer for the users with a quick learning curve.




From a more technical point of view


- Open PPM is a MAVEN project with Java version 1.7

- Install third party dependencies https://github.com/OpenPPM/OpenPPM/tree/master/thirdPartyLibs

- In order to compile the project you need to execute the following instruction:

mvn clean package -P production -DclientDepencency=community


- You can find the MySQL scheme in the following folder:

OpenPPM/schemas/CreateDB.sql


- In order to deploy the tool, please refer to our installation guide located in the installation guide folder. You will find there all the information you need to install the solution, 
do a first configuration set up and as well as a section with a little user tutorial.



We create a Demo environement that you can access using:

URL:http://community.talaia-openppm.com/openppm/

User: openppm

Password:openppm


We hope that you will enjoy this new version and look forward to your feedback in our community forums.


OpenPPM Team
