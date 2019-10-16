import Cookies from 'js-cookie'

// 有效期 1 天
const expires = 1

const app = {
  state: {
    sidebar: {
      opened: !+Cookies.get('sidebarStatus', { expires: expires })
    }
  },
  mutations: {
    TOGGLE_SIDEBAR: state => {
      if (state.sidebar.opened) {
        Cookies.set('sidebarStatus', 1, { expires: expires })
      } else {
        Cookies.set('sidebarStatus', 0, { expires: expires })
      }
      state.sidebar.opened = !state.sidebar.opened
    }
  },
  actions: {
    ToggleSideBar: ({ commit }) => {
      commit('TOGGLE_SIDEBAR')
    }
  }
}

export default app
