import Layout from "@/view/Layout.vue";
import Login from "@/view/Login.vue"
import Register from "@/view/Register.vue";

export const constantRoutes = [
    {
        path: '/login',
        name: 'login',
        component: Login,
        hidden: false,
        meta: { title: '登录'}
    },
    {
        path: '/register',
        name: 'register',
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
                path: '/commentDetail/:id',
                name: 'comment_detail',
                component: () => import('@/view/CommentDetail.vue')
            },
            {
                path: '/category/:id',
                name: 'category_index',
                component: () => import('@/view/CategoryIndex.vue')
            },
            {
                path: '/createPost/:id',
                name: 'create_post',
                component: () => import('@/view/CreatePost.vue')
            },
            {
                path: '/userProfile/:id',
                name: 'user_profile',
                component: () => import('@/view/UserProfile.vue')
            },
            {
                path: '/userEdit',
                name: 'user_edit',
                component: () => import('@/view/UserEdit.vue')
            },
            {
                path: '/createCategory',
                name: 'create_category',
                component: () => import('@/view/CreateCategory.vue')
            },
            {
                path: '/searchPost/:keyword',
                name: 'search_post',
                component: () => import('@/view/SearchPost.vue')
            }
        ]
    },
]
export default constantRoutes
