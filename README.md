This is an implementation for a Dog App to fetch and display Data from Dog CEO Api https://dog.ceo/dog-api/

The backend can be started as a Spring Boot application by running 'src/backend/src/main/java/demo/dogapp/DogApp.java'

The frontend can be started by running the command 'ng serve' in terminal after navigating to 'dog-app/src/frontend/dog-app'

For the frontend, the homepage shows the list of all breeds. Upon clicking on name of any breeds, the user is redirected to the detail view of that breed. 
Clicking on the name of the app "Dog App" next to the button for side navigation redirects the user back to the Homepage.
Through the side navigation menu on the left of the page, the user can navigate between Homepage and Analytics page.

Due to time limit, basic authentication was used to restrict Api Access to any endpoints at "/admin/**". Only users with ADMIN role can access to the endpoint. By default, the data for Analytics frontend page is already fetched with admin user. However, the api access restriction can be tested in Postman either with No Auth or Basic Auth (please check 'src/backend/src/main/java/demo/dogapp/configs/WebSecurityConfig.java' for all available users)

