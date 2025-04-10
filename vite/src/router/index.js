import Layout from "@/components/Layout.vue";
import Login from "@/view/login.vue"
import Register from "@/view/register.vue";

export const constantRoutes = [
    {
        path: '/login',
        component: Login,
        hidden: false,
        meta: { title: '登录'}
    },
    {
        path: '/register',
        component: Register,
        hidden: false,
        meta: { title: '注册'}
    },
    {
        path: '',
        component: Layout,
        redirect: '/index',
        children: [
            {
                path: '',
                component: () => import('@/components/index.vue'),
                name: 'Index',
                meta: { title: '首页', icon: 'dashboard', affix: true }
            }
        ]
    },
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
