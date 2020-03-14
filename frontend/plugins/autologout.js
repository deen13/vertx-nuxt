/**
 * Custom plugin for handling authentication errors in axios responses.
 */
export default function({ $axios, $auth, redirect }) {
  $axios.onError((error) => {
    /**
     * This is a workaround and should normally be done by the nuxt-auth-module
     * https://github.com/nuxt-community/auth-module/pull/361#issuecomment-598745600
     */
    if (error.response && (error.response.status === 401 || error.response.status === 400)) {
      $auth.logout()
      redirect("/login")
    }
  })
}
