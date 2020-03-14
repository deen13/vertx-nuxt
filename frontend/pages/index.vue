<template>
  <v-card class="mx-auto" max-width="400" tile>
    <v-list-item v-for="reviewer in reviewers" :key="reviewer">
      <v-list-item-content>
        <v-list-item-title>{{ reviewer }}</v-list-item-title>
      </v-list-item-content>
    </v-list-item>
  </v-card>
</template>

<script>
import { mapState } from "vuex"

export default {
  async fetch({ store }) {
    await store.dispatch("reload")
  },
  computed: mapState(["reviewers"]),
}
</script>
