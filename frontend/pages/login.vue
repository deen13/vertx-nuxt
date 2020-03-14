<template>
  <v-row justify="center">
    <v-col cols="12">
      <v-row justify="center">
        <v-col cols="3" class="px-0 pb-2">
          <v-img :src="logo" />
        </v-col>
      </v-row>
    </v-col>
    <v-col cols="12" sm="8" md="4" class="pb-0">
      <v-form v-model="valid" @submit.prevent="login">
        <v-text-field
          v-model="username"
          :rules="usernameRules"
          prepend-icon="mdi-account"
          type="text"
          placeholder="Username"
          outlined
          @keydown.enter="login"
        />
        <v-text-field
          v-model="password"
          :rules="passwordRules"
          prepend-icon="mdi-key"
          type="password"
          placeholder="Password"
          outlined
          @keydown.enter="login"
        />
      </v-form>
    </v-col>
    <v-col cols="12" class="pt-0">
      <v-row justify="center" align="center">
        <v-btn :disabled="!valid" :loading="loading" color="primary" type="submit" @click="login">
          <span class="mx-2">Login</span>
        </v-btn>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
export default {
  middleware: ["auth"],
  data: () => ({
    valid: false,
    loading: false,
    logo: require("static/nuxt-logo.svg"),
    username: "",
    usernameRules: [(v) => !!v || "Please enter your username!"],
    password: "",
    passwordRules: [(v) => !!v || "Please enter your password!"],
  }),
  methods: {
    login() {
      this.loading = true

      return this.$auth
        .loginWith("local", {
          data: {
            username: this.username,
            password: this.password,
          },
        })
        .then(() => this.$router.push("/"))
        .finally(() => (this.loading = false))
    },
  },
  head: () => ({
    title: "Login",
  }),
}
</script>
