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
import { find, isNumber, isUndefined } from 'lodash'
import useRecentCategoryStore from '@/js/module/recent_category'
import useCategoryCacheStore from '@/js/module/category_cache'

const route = useRoute()
const router = useRouter()

const categoryId = ref(null)
const name = ref("")
const createTime = ref(new Date())
const avatar = ref(defaultAvatar)
const info = ref("")
const rule = ref("")

const membership = ref({})
const userCache = useUserCacheStore()
const userStore = useLoginUserStore()
const categoryCache = useCategoryCacheStore()
const history = useRouteHistoryStore()
const recentCategory = useRecentCategoryStore()

const {userId} = storeToRefs(userStore)
const {historyList} = storeToRefs(history)
const emit = defineEmits(['finish-load'])

const isFirstTime = computed( () => isUndefined( find( historyList.value, o =>
        o.name == 'category_index' && o.params['id'] == route.params['id']
))
)

watch(route, async () => {
    if ( route.name == 'category_index' ) {
        console.log( 'category: ' + isFirstTime.value)
        categoryId.value = Number(route.params['id'])
    }
    if (!isFirstTime.value) emit('finish-load')
}, {immediate: true})

watch(categoryId, async() => {
    if (isNumber (categoryId.value)) {
        let data = (await categoryCache.fetchCategory(categoryId.value))
        name.value = data.name
        avatar.value = data.avatar
        if (isFirstTime.value) load()
    }
}, {immediate: true})

async function load() {
    recentCategory.updateRecent(categoryId.value)
    if (userId.value != null) {
        membership.value = await userCache.fetchUserBriefView(categoryId.value, userId.value)
    }
}

async function subscribe() {
    await joinCategory(categoryId.value)
    membership.value = await userCache.fetchUserBriefView(categoryId.value, userId.value, true)
}

function createPost() {
    router.push({
        name: 'create_post',
        params: {
            id: categoryId.value
        }
    })
}


</script>

<template>
    <div class="category-index">
    <div class="header">
        <span class="left">
            <el-avatar class="avatar" :src="avatar"/>
            <el-text size="large">{{ name }}</el-text>
        </span>
        <span class="right">
            <el-button round icon="Plus" @click="createPost">发表新帖</el-button>
            <el-button @click="subscribe" :type="membership.role > 0 ? 'default' : 'primary'" round>
                {{ membership.role > 0 ? '已关注' : '关注' }}
            </el-button>
        </span>
    </div>
    <PostList
        @finish-load="emit('finish-load')"
        v-if="categoryId"
        :category-id="categoryId"
        :size="15"
    />
    </div>
</template>

<style lang="scss" scoped>
.category-index {
    height: calc(100% - 50px);
    overflow-y: hidden;
}
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