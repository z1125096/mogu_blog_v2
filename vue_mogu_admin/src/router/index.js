import Vue from 'vue'
import Router from 'vue-router'
import store from '../store'
import { mapGetters } from "vuex";

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },
  { path: '/401', component: () => import('@/views/401'), hidden: true },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '首页',
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '仪表盘', icon: 'dashboard' }
    }]
  },

  {
    path: '/blog',
    component: Layout,
    redirect: '/blog/blog',
    name: '博客管理',
    meta: { title: '博客管理', icon: 'edit' },
    children: [
      {
        path: 'blog',
        name: '博客管理',
        component: () => import('@/views/blog/blog'),
        meta: { title: '博客管理', icon: 'edit' }
      },
      {
        path: 'blogTag',
        name: '标签管理',
        component: () => import('@/views/blog/blogTag'),
        meta: { title: '标签管理', icon: 'tag' }
      },
      {
        path: 'blogSort',
        name: '分类管理',
        component: () => import('@/views/blog/blogSort'),
        meta: { title: '分类管理', icon: 'sort' }
      },

      {
        path: 'collect',
        name: '收藏管理',
        component: () => import('@/views/blog/collect'),
        meta: { title: '收藏管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/authority',
    component: Layout,
    redirect: '/authority/admin',
    name: '权限管理',
    meta: { title: '权限管理', icon: 'authority' },
    children: [
      {
        path: 'admin',
        name: '管理员管理',
        component: () => import('@/views/authority/admin'),
        meta: { title: '管理员管理', icon: 'user' }
      },
      {
        path: 'role',
        name: '角色管理',
        component: () => import('@/views/authority/role'),
        meta: { title: '角色管理', icon: 'peoples' }
      },
      {
        path: 'categoryMenu',
        name: '菜单管理',
        component: () => import('@/views/authority/categoryMenu'),
        meta: { title: '菜单管理', icon: 'authority' }
      }
    ]
  },

  {
    path: '/resource',
    component: Layout,
    redirect: '/resource/resourceSort',
    name: '资源管理',
    meta: { title: '资源管理', icon: 'resource' },
    children: [
      {
        path: 'resourceSort',
        name: '分类管理',
        component: () => import('@/views/resource/resourceSort'),
        meta: { title: '分类管理', icon: 'table' }
      },
      {
        path: 'studyVideo',
        name: '视频管理',
        component: () => import('@/views/resource/studyVideo'),
        meta: { title: '视频管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/message',
    component: Layout,
    redirect: '/message/comment',
    name: '消息管理',
    meta: { title: '消息管理', icon: 'message1' },
    children: [
      {
        path: 'comment',
        name: '评论管理',
        component: () => import('@/views/message/comment'),
        meta: { title: '评论管理', icon: 'table' }
      },
      {
        path: 'feedback',
        name: '反馈管理',
        component: () => import('@/views/message/feedback'),
        meta: { title: '反馈管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/picture',
    component: Layout,
    redirect: '/picture/pictureSort',
    name: '图片管理',
    meta: { title: '图片管理', icon: 'example' },
    children: [
      {
        path: 'pictureSort',
        name: '图片类别管理',
        component: () => import('@/views/picture/pictureSort'),
        meta: { title: '图片管理', icon: 'picture' }
      },
      {
        path: 'picture',
        name: '图片管理',
        hidden: true,
        component: () => import('@/views/picture/picture'),
        meta: { title: '图片管理', icon: 'picture' }
      }
    ]
  },

  {
    path: '/user',
    component: Layout,
    redirect: '/user/user',
    name: '用户管理',
    meta: { title: '用户管理', icon: 'user1' },
    children: [
      {
        path: 'user',
        name: '用户管理',
        component: () => import('@/views/user/user'),
        meta: { title: '用户管理', icon: 'table' }
      },
      {
        path: 'visitor',
        name: '游客管理',
        component: () => import('@/views/user/visitor'),
        meta: { title: '游客管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/system',
    component: Layout,
    redirect: '/system/solrIndex',
    name: '系统管理',
    meta: { title: '系统管理', icon: 'system' },
    children: [
      {
        path: 'aboutMe',
        name: '关于我',
        component: () => import('@/views/system/aboutMe'),
        meta: { title: '关于我', icon: 'aboutMe' }
      },
      {
        path: 'blogLink',
        name: '友情链接',
        component: () => import('@/views/system/blogLink'),
        meta: { title: '友情链接', icon: 'blogLink' }
      },
      {
        path: 'webConfig',
        name: '网站配置',
        component: () => import('@/views/system/webConfig'),
        meta: { title: '网站配置', icon: 'web' }
      }
    ]
  },

  {
    path: '/log',
    component: Layout,
    redirect: '/log/log',
    name: '操作日志',
    meta: { title: '操作日志', icon: 'log' },
    children: [
      {
        path: 'log',
        name: '操作日志',
        component: () => import('@/views/log/log'),
        meta: { title: '操作日志', icon: 'log' }
      },
      {
        path: 'exceptionLog',
        name: '异常日志',
        component: () => import('@/views/log/exceptionLog'),
        meta: { title: '异常日志', icon: 'exception' }
      },
      {
        path: 'webVisit',
        name: '用户日志',
        component: () => import('@/views/log/webVisit'),
        meta: { title: '用户日志', icon: 'user1' }
      }
    ]
  },

  {
    path: '/restapi',
    component: Layout,
    redirect: '/restapi/adminRestApi',
    name: '接口管理',
    meta: { title: '接口管理', icon: 'restapi' },
    children: [
      {
        path: 'adminRestApi',
        name: 'Admin接口',
        component: () => import('@/views/restapi/adminRestApi'),
        meta: { title: 'Admin接口', icon: 'table' }
      },
      {
        path: 'pictureRestApi',
        name: 'Picture接口',
        component: () => import('@/views/restapi/pictureRestApi'),
        meta: { title: 'Picture接口', icon: 'table' }
      },
      {
        path: 'webRestApi',
        name: 'Web接口',
        component: () => import('@/views/restapi/webRestApi'),
        meta: { title: 'Web接口', icon: 'table' }
      }
    ]
  },

  {
    path: '/monitor',
    component: Layout,
    redirect: '/monitor/springBootAdmin',
    name: '监控中心',
    meta: { title: '监控中心', icon: 'log' },
    children: [
      {
        path: 'SpringBootAdmin',
        name: 'SpringBootAdmin',
        component: () => import('@/views/monitor/SpringBootAdmin'),
        meta: { title: 'SpringBootAdmin', icon: 'log' }
      },
      {
        path: 'Druid',
        name: 'Druid',
        component: () => import('@/views/monitor/Druid'),
        meta: { title: 'Druid', icon: 'exception' }
      },
      {
        path: 'Eureka',
        name: 'Eureka',
        component: () => import('@/views/monitor/Eureka'),
        meta: { title: 'Eureka', icon: 'user1' }
      },
      {
        path: 'RabbitMQ',
        name: 'RabbitMQ',
        component: () => import('@/views/monitor/RabbitMQ'),
        meta: { title: 'RabbitMQ', icon: 'user1' }
      },
      {
        path: 'Solr',
        name: 'Solr',
        component: () => import('@/views/system/solrIndex'),
        meta: { title: 'Solr', icon: 'index' }
      }
    ]
  },

  { path: '*', redirect: '/404', hidden: true }
]

const router = new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

// 全局路由守卫
router.beforeEach((to, from, next) => {
  // to: Route: 即将要进入的目标 路由对象
  // from: Route: 当前导航正要离开的路由
  // next: Function: 一定要调用该方法来 resolve 这个钩子。执行效果依赖 next 方法的调用参数。

  // if (to.path === '/login') {
  //   next()
  // }
  // if (to.path === '/404') {
  //   next()
  // }

  // if (store.getters.menu.sonList) {
  //   let sonList = store.getters.menu.sonList
  //   console.log("我进来了", sonList);

  //   for (let a = 0; a < sonList.length; a++) {
  //     if (to.path === sonList[a].url) {
  //       next()
  //     }
  //   }
  //   next({ path: '/404' })
  // }
  next()
  // store.dispatch('FedLogOut').then(() => {
  //   next({ path: '/' })
  // })

  // else {
  //   store.dispatch('FedLogOut').then(() => {
  //     next({ path: '/' })
  //   })
  // }

  // if (to.path === '/login') {
  //   next({ path: '/' })
  // }

  // store.dispatch('GetMenu').then(response => {
  //   console.log("come");
  //   if(response.code == "success") {
  //     const sonList = response.data.sonList
  //     for (let a = 0; a < sonList.length; a++) {
  //       if (to.path === sonList[a].url) {
  //         next()
  //       }
  //     }
  //   }
  // })
  // next({ path: '/login' })

  // 未登录状态；当路由到nextRoute指定页时，跳转至login
})

export default router
