import Layout from "@/components/Layout.vue";
import Login from "@/view/login.vue"
import Register from "@/view/register.vue";

export const constantRoutes = [
    {
        path: '/',
        component: Login,
        hidden: true
    },
    {
        path: '/register',
        component: Register,
        hidden: true
    },
    // {
    //     path: '',
    //     component: Layout,
    //     redirect: '/index',
    //     children: [
    //         {
    //             path: '/index',
    //             component: () => import('@/view/index'),
    //             name: 'Index',
    //             meta: { title: '首页', icon: 'dashboard', affix: true }
    //         }
    //     ]
    // },
    // {
    //     path: '/user',
    //     component: Layout,
    //     hidden: true,
    //     redirect: 'noredirect',
    //     children: [
    //         {
    //             path: 'profile',
    //             component: () => import('@/views/system/user/profile/index'),
    //             name: 'Profile',
    //             meta: { title: '个人中心', icon: 'user' }
    //         }
    //     ]
    // }
]
export default constantRoutes
