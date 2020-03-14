export const state = () => ({
  reviewers: [],
})

export const mutations = {
  update(state, data) {
    state.reviewers = data
  },
}

export const actions = {
  async reload({ commit }) {
    const { data } = await this.$axios.get("/api/data/reviewers")

    commit("update", data)
  },
}
