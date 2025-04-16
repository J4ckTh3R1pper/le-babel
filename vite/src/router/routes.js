import Layout from "@/view/Layout.vue";
import Login from "@/view/Login.vue"
import Register from "@/view/Register.vue";

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
        meta: {title: 'Le Babel'},
        children: [
            {
                path: '',
                component: () => import('@/view/Index.vue'),
                name: 'index',
                meta: { title: '首页', icon: 'dashboard', affix: true }
            },
            {
                path: '/postDetail/:id',
                name: 'post_detail',
                component: () => import('@/view/PostDetail.vue')
            },
            {
                path: '/category/:id',
                name: 'category_index',
                component: () => import('@/view/CategoryIndex.vue')
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
