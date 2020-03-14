export default {
  mode: "spa",
  /*
   ** Headers of the page
   */
  head: {
    titleTemplate: "%s",
    title: process.env.npm_package_name || "",
    meta: [
      { charset: "utf-8" },
      { name: "viewport", content: "width=device-width, initial-scale=1" },
      {
        hid: "description",
        name: "description",
        content: process.env.npm_package_description || "",
      },
    ]
  },
  /*
   ** Customize the progress-bar color
   */
  loading: { color: "#fff" },
  /*
   ** Global CSS
   */
  css: ["@mdi/font/css/materialdesignicons.css", "typeface-roboto/index.css"],
  /*
   ** Plugins to load before mounting the App
   */
  plugins: [],
  /*
   ** Nuxt.js dev-modules
   */
  buildModules: [
    // Doc: https://github.com/nuxt-community/eslint-module
    "@nuxtjs/eslint-module",
    "@nuxtjs/vuetify",
  ],
  /*
   ** Nuxt.js modules
   */
  modules: [
    // Doc: https://axios.nuxtjs.org/usage
    "@nuxtjs/axios",
    "@nuxtjs/proxy",
    "@nuxtjs/auth",
  ],
  /*
   ** Axios module configuration
   ** See https://axios.nuxtjs.org/options
   */
  axios: {},
  /*
   ** vuetify module configuration
   ** https://github.com/nuxt-community/vuetify-module
   */
  vuetify: {
    theme: {
      themes: {
        light: {
          primary: "#B20026",
          secondary: "#eb4a4e",
          accent: "#3F51B5",
        },
      },
    },
    defaultAssets: {
      icons: false,
      font: false,
    },
  },
  /*
   ** Build configuration
   */
  build: {
    /*
     ** You can extend webpack config here
     */
    extend(config, ctx) {},
  },
  proxy: {
    "/api": {
      target: "http://127.0.0.1:8080",
    },
  },
  auth: {
    cookie: false,
    strategies: {
      local: {
        autoLogout: true,
        _scheme: "refresh",
        token: {
          property: "accessToken",
          maxAge: 30,
        },
        refreshToken: {
          property: "refreshToken",
          data: "refreshToken",
          maxAge: 90,
        },
        endpoints: {
          login: { url: "/api/auth/login", method: "post" },
          refresh: { url: "/api/auth/refresh/", method: "post" },
          user: false,
          logout: false,
        },
        autoRefresh: true,
        redirect: {
          login: "/login",
          logout: "/login",
        },
      },
    },
  },
  router: {
    middleware: ["auth"],
  },
}
