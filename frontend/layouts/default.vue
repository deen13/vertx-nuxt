<template>
  <v-app dark>
    <v-app-bar v-if="isIndex" fixed app color="primary" dark>
      <v-icon class="mx-1">mdi-github</v-icon>
      <v-toolbar-title class="pr-4" v-text="title" />
    </v-app-bar>
    <v-content>
      <v-container fill-height fluid>
        <nuxt />
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      title: "Demo",
    }
  },
  computed: {
    isIndex() {
      return this.$route.name !== "login"
    },
  },
}
</script>
