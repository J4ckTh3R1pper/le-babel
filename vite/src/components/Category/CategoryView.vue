<script setup>
import { getCategoryDto }from '@/js/api/category.js'
import { useRoute, useRouter } from 'vue-router'
import defaultAvatar from '@/assets/images/default_user_avatar.png'
import PostList from '../Post/PostList.vue'
import useUserCacheStore from '@/js/module/user_cache'
import useLoginUserStore from '@/js/module/login_user'
import { Plus } from '@element-plus/icons-vue/dist/types'
import InlineMarkdown from '../Markdown/InlineMarkdown.vue'
import UserBriefView from '../User/UserBriefView.vue'

const route = useRoute()
const router = useRouter()

const categoryId = ref(route.params['id'])
const name = ref("")
const createTime = ref(new Date())
const avatar = ref(defaultAvatar)
const info = ref("")
const rule = ref("")

const membership = ref({})
const userCache = useUserCacheStore()
const loginUser = useLoginUserStore()

getCategoryDto(categoryId).then(res => {
    let data = res.data
    categoryId.value = data.id
    name.value = data.name
    createTime.value = new Date(data.createTime)
    avatar.value = data.avatar
    info.value = data.info
    rule.value = data.rule
}).catch( res => {
    router.push('/404')
})

if (loginUser.id != null) {
    userCache.fetchUser(loginUser.id)
    userCache.fetchMember(categoryId, loginUser.id)
    membership.value = userCache.getUserBriefView(categoryId, loginUser.id)
}

</script>

<template>
    <el-container>
        <el-header class="header">
            <span class="left">
                <el-avatar class="avatar" :src="avatar"/>
                <span class="name">{{ name }}</span>
            </span>
            <span class="right">
                <el-button round :icon="Plus">发表新帖</el-button>
                <el-button :type="membership.role > 0 ? '' : 'primary'" round>
                    {{ membership.role > 0 ? '已关注' : '关注' }}
                </el-button>
            </span>
        </el-header>
        <el-main>
            <PostList
                :category-id="categoryId"
                :size="15"
            />
        </el-main>
        <el-aside class="info">
            <div>
                <InlineMarkdown :md-text="info"/>
            </div>
            <div>
                <InlineMarkdown :md-text="rule"/>
            </div>
            <div v-if="loginUser.id != null">
                <UserBriefView :category-id="categoryId" :user-id="loginUser.id"/>
            </div>
        </el-aside>
    </el-container>
</template>

<style lang="scss" scoped>
.el-container {
    height: 100%;
}

.el-main {
    overflow: hidden;
    padding: 0;
}
.header {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}
</style>