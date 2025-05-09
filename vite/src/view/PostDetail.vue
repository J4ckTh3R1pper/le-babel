<template>
    <div class="post-detail" v-if="data">
        <BackButton :category-id="data.categoryId"/>
        <PostDetailedView
            :post-id="data.id"
            :user-id="data.userId"
            :title="data.title"
            :category-id="data.categoryId"
            :comment-count="data.commentCount"
            :content="data.content"
            :create-time="data.createTime"
            :tags="data.tags"
            :like-count="data.likeCount"
            :view-count="data.viewCount"
            :liked="data.liked"
            :last-update-time="data.lastUpdateTime"
        />
        <el-divider/>
        <h1><el-text>评论</el-text></h1>
        <CommentList :post-id="data.id" ref="commentList"/>
        <el-divider/>
        <CreateComment v-if="isLoggedIn" :post-id="data.id" @comment-created="onCommentCreated"/>
        <div class="login" v-else><el-text>请先</el-text><el-link @click="router.push({name: 'login'})" type="primary">登录</el-link></div>
    </div>
</template>

<script setup>
import BackButton from '@/components/Category/BackButton.vue';
import CommentList from '@/components/Comment/CommentList.vue';
import CreateComment from '@/components/Comment/CreateComment.vue';
import PostDetailedView from '@/components/Post/PostDetailedView.vue';
import { getPostDetails } from '@/js/api/post';
import useLoginUserStore from '@/js/module/login_user';
import useRecentCategoryStore from '@/js/module/recent_category';
import useRecentPostStore from '@/js/module/recent_post';
import useRouteHistoryStore from '@/js/module/route_history';
import { find, initial, isEmpty, isUndefined } from 'lodash';
import { storeToRefs } from 'pinia';
import { onMounted, useTemplateRef } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()

const recentCategory = useRecentCategoryStore()
const routeHistory = useRouteHistoryStore()
const recentPost = useRecentPostStore()
const loginUserStore = useLoginUserStore()

const {historyList} = storeToRefs(routeHistory)
const {isLoggedIn} = storeToRefs(loginUserStore)

const id = ref(null)
const data = ref(null)

const emit = defineEmits(['finish-load', 'category-change'])

const isFirstTime = computed(() => isUndefined( find( historyList.value, o =>
        o.name == 'post_detail' && o.params['id'] == id.value
)))

const commentListRef = useTemplateRef('commentList')

function onCommentCreated() {
    commentListRef.value.load(true)
}

watch(route, async (newRoute) => {
    if ( newRoute.name == 'post_detail' ) {
        console.log('post: ' + isFirstTime.value)
        id.value = Number(newRoute.params['id'])
    }
    if (!isFirstTime.value) emit('finish-load')
}, {immediate: true})

watch(id, async () => {
    if (id.value && isFirstTime.value) {
        data.value = await getPostDetails(id.value)
        recentCategory.updateRecent(data.value.categoryId)
        recentPost.updateRecent(id.value)
        emit('finish-load')
    }
}, {immediate: true})

watch (data, newData => {
    emit('category-change', newData['categoryId'])
})

</script>

<style lang="scss" scoped>
.post-detail {
    margin-bottom: 4em;
    .login {
        display: flex;
        justify-content: center;
    }
}
</style>