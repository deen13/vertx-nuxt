# nuxt-auth-refresh-token demo

This repository demonstrates the automatic jwt refresh implemented in [this](https://github.com/nuxt-community/auth-module/pull/361) 
pull request. The frontend currently uses an workaround to redirect to the login page when the tokens are 
absent or expired. The username is admin and the password is github. 


### Backend Setup
```bash
# build docker image
$ ./gradlew jibDockerBuild

# serve backend
$ docker run -p 127.0.0.1:8080:8080 backend run com.github.deen13.MainVerticle -conf /app/resources/config.json
```

### Frontend Setup
```bash
# install dependencies
$ yarn

# serve with hot reload at localhost:3000
$ yarn dev
```