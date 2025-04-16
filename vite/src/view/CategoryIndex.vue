<script setup>
import { getCategoryInfo, joinCategory }from '@/js/api/category.js'
import { useRoute, useRouter } from 'vue-router'
import defaultAvatar from '@/assets/images/default_user_avatar.png'
import PostList from '@/components/Post/PostList.vue'
import useUserCacheStore from '@/js/module/user_cache'
import useLoginUserStore from '@/js/module/login_user'
import InlineMarkdown from '@/components/Markdown/InlineMarkdown.vue'
import UserBriefView from '@/components/User/UserBriefView.vue'
import useRouteHistoryStore from '@/js/module/route_history'
import { storeToRefs } from 'pinia'
import { find, isUndefined } from 'lodash'
import useRecentCategoryStore from '@/js/module/recent_category'

const route = useRoute()

const categoryId = ref(Number(route.params['id']))
const name = ref("")
const createTime = ref(new Date())
const avatar = ref(defaultAvatar)
const info = ref("")
const rule = ref("")

const membership = ref({})
const userCache = useUserCacheStore()
const userStore = useLoginUserStore()
const history = useRouteHistoryStore()
const recentCategory = useRecentCategoryStore()

const {userId} = storeToRefs(userStore)
const {historyList} = storeToRefs(history)

const isFirstTime = () => isUndefined( find( historyList.value, o =>
        o.name == 'category_index' && o.params['id'] != route.params['id']
))

watch(route, () => {
    if ( route.name == 'category_index' )
        categoryId.value = Number(route.params['id'])
    if (isFirstTime()) load()
}, {immediate: true})


async function load() {
    await getCategoryInfo(categoryId.value).then(res => {
        // console.log(res)
        name.value = res.name
        createTime.value = new Date(res.createTime * 1000)
        avatar.value = res.avatar
        info.value = res.info
        rule.value = res.rule
    }).catch( res => {
        // console.log(res)
        // router.push('/404')
    })

    recentCategory.updateRecent(categoryId.value)

    if (userId.value != null) {
        membership.value = await userCache.fetchUserBriefView(categoryId.value, userId.value)
    }
}

async function subscribe() {
    await joinCategory(categoryId.value)
    membership.value = await userCache.fetchUserBriefView(categoryId.value, userId.value, true)
}


</script>

<template>
    <div class="header">
        <span class="left">
            <el-avatar class="avatar" :src="avatar"/>
            <el-text size="large">{{ name }}</el-text>
        </span>
        <span class="right">
            <el-button round icon="Plus">发表新帖</el-button>
            <el-button @click="subscribe" :type="membership.role > 0 ? 'default' : 'primary'" round>
                {{ membership.role > 0 ? '已关注' : '关注' }}
            </el-button>
        </span>
    </div>
    <PostList
        :category-id="categoryId"
        :size="15"
    />
</template>

<style lang="scss" scoped>
.header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    padding: 1em 0;
}
.left, .right {
    display:flex;
    flex-direction: inherit;
    align-items: center;
    .el-avatar {
        margin-right: 6px;
    }
}
</style>