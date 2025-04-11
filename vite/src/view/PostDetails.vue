<template>
    <el-container>
        <el-main>
            <Suspense>
            <PostDetailedView v-if="!isEmpty(details)"
                :post-id="details.id"
                :user-id="details.userId"
                :title="details.title"
                :category-id="details.categoryId"
                :comment-count="details.commentCount"
                :content="details.content"
                :create-time="details.createTime"
                :tags="details.tags"
                :like-count="details.likeCount"
                :view-count="details.viewCount"
                :liked="details.liked"
                :last-update-time="details.lastUpdateTime"
            /></Suspense>
        </el-main>
        <el-aside></el-aside>
    </el-container>
</template>

<script setup>
import PostDetailedView from '@/components/Post/PostDetailedView.vue';
import { getPostDetails } from '@/js/api/post';
import { isEmpty } from 'lodash';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()

const id = route.params['id']

const details = ref(undefined)
getPostDetails(id).then(res => {
    details.value = res
})

</script>

<style lang="scss" scoped>

</style>